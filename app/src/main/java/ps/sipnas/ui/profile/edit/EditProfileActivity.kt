package ps.sipnas.ui.profile.edit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.tbruyelle.rxpermissions2.RxPermissions
import org.koin.androidx.viewmodel.ext.android.viewModel
import ps.sipnas.R
import ps.sipnas.base.BaseActivity
import ps.sipnas.databinding.ActivityEditProfileBinding
import ps.sipnas.utils.Exif
import ps.sipnas.utils.GlideApp
import ps.sipnas.utils.Hai
import ps.sipnas.utils.PrefManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>() {
    private val viewModel by viewModel<EditProfileViewModel>()
    private lateinit var rxPermissions: RxPermissions
    private lateinit var filePath: String
    private var imageFile: File? = null
    private lateinit var prefManager: PrefManager
    private val CAMERA = 1
    private val GALLERY = 2

    override fun getToolbarResource(): Int = R.id.toolbar

    override fun getLayoutResource(): Int = R.layout.activity_edit_profile

    override fun myCodeHere() {
        dataBinding.setLifecycleOwner(this)
        rxPermissions = RxPermissions(this)
        prefManager = PrefManager(this)
        title = getString(R.string.edit_profile)
        dataBinding.viewModel = viewModel
        viewModel.getProfile(linkedMapOf(Hai.auth to prefManager.getAuthToken()))

        dataBinding.etCurrentPass.transformationMethod = PasswordTransformationMethod()
        dataBinding.etNewPass.transformationMethod = PasswordTransformationMethod()
        dataBinding.etReNewPass.transformationMethod = PasswordTransformationMethod()

        dataBinding.ivChangePhoto.setOnClickListener {
            val choose = arrayOf<CharSequence>(getString(R.string.camera), getString(R.string.gallery))
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.choose_source))
            builder.setItems(choose) { _, i ->
                when (i) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }
            builder.show()
        }
    }


    /**
     * Pick Image method
     */

    @SuppressLint("CheckResult")
    private fun openCamera() {
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .doOnError { e -> Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show() }
                .subscribe { permission ->
                    if (permission) {
                        try {
                            imageFile = createImageFile()
                        } catch (ex: IOException) {
                            ex.printStackTrace()
                        }

                        if (imageFile != null) {
                            val photoURI: Uri = FileProvider.getUriForFile(this, "ps.sipnas.provider", imageFile
                                    ?: throw NullPointerException())
                            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            val resolvedIntentActivities: List<ResolveInfo> = this.packageManager?.queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
                                    ?: throw Exception()
                            resolvedIntentActivities
                                    .map { it.activityInfo.packageName }
                                    .forEach { this.grantUriPermission(it, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION) }
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            if (cameraIntent.resolveActivity(this.packageManager) != null)
                                startActivityForResult(cameraIntent, CAMERA)
                        }
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage(getString(R.string.camera_not_allowed))
                        builder.setPositiveButton(getString(R.string.retry)) { dialog, _ -> dialog.dismiss() }
                        builder.setCancelable(false)
                        builder.show()
                    }
                }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        filePath = image.absolutePath
        return image
    }

    @SuppressLint("CheckResult")
    private fun openGallery() {
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { permission ->
                    if (permission) {
                        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                        galleryIntent.type = "image/*"
                        startActivityForResult(galleryIntent, GALLERY)
                    } else {
                        val builder = AlertDialog.Builder(this)
                        builder.setMessage(getString(R.string.storage_not_allowed))
                        builder.setPositiveButton(getString(R.string.retry)) { dialog, _ -> dialog.dismiss() }
                        builder.setCancelable(false)
                        builder.show()

                    }
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {

            if (imageFile?.exists() ?: throw Exception()) {
                try {
                    var bitmap = BitmapFactory.decodeFile(imageFile?.absolutePath)  //fix orientation in samsung device
                    val orientation = Exif.getOrientation(imageFile?.readBytes())
                    when (orientation) {
                        90 -> bitmap = rotateImage(bitmap, 90F)
                        180 -> bitmap = rotateImage(bitmap, 180F)
                        270 -> bitmap = rotateImage(bitmap, 270F)
                    }
                    disableMirrorCamera(bitmap)
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    options.inSampleSize = 5
                    BitmapFactory.decodeFile(imageFile?.absolutePath ?: throw Exception(), options)

                    setPic(imageFile ?: throw Exception())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else if (requestCode == GALLERY && resultCode == Activity.RESULT_OK) {
            try {
                val selectedImage = data?.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursorImage = this.contentResolver.query(selectedImage,
                        filePathColumn, null, null, null)
                if (cursorImage != null) {
                    cursorImage.moveToFirst()
                    val columnIndex = cursorImage.getColumnIndex(filePathColumn[0])
                    val picturePath = cursorImage.getString(columnIndex)
                    imageFile = File(picturePath)
                    cursorImage.close()
                }

                val bitmap = BitmapFactory.decodeFile(imageFile?.absolutePath ?: throw Exception())
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, FileOutputStream(imageFile))
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    options.inSampleSize = 5
                    BitmapFactory.decodeFile(imageFile?.absolutePath ?: throw Exception(), options)
                    setPic(imageFile ?: throw Exception())
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                setPic(imageFile ?: throw Exception())

            } catch (e: Exception) {        //for xiaomi device
                val uri = data?.data
                imageFile = File(uri?.path)
                val selectedImage = getImageContentUri(this, imageFile ?: throw Exception())
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                val cursorImage = this.contentResolver.query(selectedImage,
                        filePathColumn, null, null, null)
                if (cursorImage != null) {
                    cursorImage.moveToFirst()
                    val columnIndex = cursorImage.getColumnIndex(filePathColumn[0])
                    val picturePath = cursorImage.getString(columnIndex)
                    imageFile = File(picturePath)
                    cursorImage.close()
                }

                val bitmap = BitmapFactory.decodeFile(imageFile?.absolutePath ?: throw Exception())
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, FileOutputStream(imageFile))
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    options.inSampleSize = 5
                    BitmapFactory.decodeFile(imageFile?.absolutePath ?: throw Exception(), options)
                    setPic(imageFile ?: throw Exception())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            filePath = imageFile?.absolutePath ?: throw Exception()
            imageFile = File(imageFile?.absolutePath ?: throw Exception())
        }
    }

    private fun setPic(imageFile: File) {
        viewModel.profilePict(linkedMapOf(Hai.auth to prefManager.getAuthToken()), imageFile)
                .observe(this, androidx.lifecycle.Observer {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                    GlideApp.with(this)
                            .load(imageFile)
                            .circleCrop()
                            .thumbnail(0.5F)
                            .placeholder(Hai.progressDrawable(this))
                            .error(R.drawable.ic_profile)
                            .into(dataBinding.ivProfilePicture)
                })
    }

    private fun disableMirrorCamera(source: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.preScale(-1.0f, 1.0f)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun getImageContentUri(context: Context, imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID),
                MediaStore.Images.Media.DATA + "=? ",
                arrayOf(filePath), null)

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            cursor.close()
            return Uri.withAppendedPath(baseUri, "" + id)
        } else {
            return if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                null
            }
        }
    }

}
