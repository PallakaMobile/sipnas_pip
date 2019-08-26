package ps.sipnas.ui.home.upload

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import org.koin.androidx.viewmodel.ext.android.viewModel
import ps.sipnas.R
import ps.sipnas.base.BaseActivity
import ps.sipnas.data.model.DataUploadListSPJ
import ps.sipnas.databinding.ActivityUploadBinding
import ps.sipnas.databinding.LayerDialogUploadBinding
import ps.sipnas.ui.home.upload.kegiatan.KegiatanFragment
import ps.sipnas.ui.home.upload.kegiatan.SpjFragment
import ps.sipnas.utils.Exif
import ps.sipnas.utils.GlideApp
import ps.sipnas.utils.Hai
import ps.sipnas.utils.PrefManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UploadActivity : BaseActivity<ActivityUploadBinding>() {
    private val viewModel by viewModel<UploadViewModel>()
    private lateinit var rxPermissions: RxPermissions
    private lateinit var filePath: String
    private var imageFile: File? = null
    private lateinit var listUploadSPJ: List<DataUploadListSPJ.Data>
    private lateinit var dialogBinding: LayerDialogUploadBinding
    private lateinit var prefManager: PrefManager

    private val CAMERA = 1
    private val GALLERY = 2

    private var isDone = false
    private var ID = ""

    override fun getToolbarResource(): Int = R.id.toolbar
    override fun getLayoutResource(): Int = R.layout.activity_upload

    override fun myCodeHere() {
        dataBinding.setLifecycleOwner(this)
        title = getString(R.string.upload_proof)
        rxPermissions = RxPermissions(this)
        prefManager = PrefManager(this)
        isDone = intent.extras.getBoolean("done")
        ID = intent.extras.getString("id")

        viewModel.getListUploadSPJ(linkedMapOf(Hai.auth to prefManager.getAuthToken(), "id" to ID))
                .observe(this, androidx.lifecycle.Observer {
                    listUploadSPJ = it.data as MutableList<DataUploadListSPJ.Data>
                })

        //set upload
        if (!isDone) { //only show in process
            dataBinding.layerToolbar.ivAdd.visibility = View.VISIBLE
            dataBinding.layerToolbar.ivAdd.setOnClickListener {
                uploadDialog()
            }
        }

        setupTab(getString(R.string.proof_spj), R.drawable.ic_spj, 0) //custom layout
        setupTab(getString(R.string.photos_activities), R.drawable.ic_kegiatan, 1) //custom layout\

        //set default
        openFragment(SpjFragment())

        //set on click listener
        dataBinding.tabHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0?.position) {
                    0 -> {
                        openFragment(SpjFragment())
                    }
                    1 -> {
                        openFragment(KegiatanFragment())
                    }
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun uploadDialog() {
        var idRincian = ""
        var type = 0
        dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layer_dialog_upload, null, false)
        dialogBinding.setLifecycleOwner(this)
        dialogBinding.viewModel = viewModel
        //choose type
        val listMenu = listOf("Bukti SPJ", "Foto Kegiatan")
        val adapterType = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listMenu)
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dialogBinding.spTypeEvidence.adapter = adapterType
        dialogBinding.spTypeEvidence.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = position
                when (position) {
                    0 -> {
                        dialogBinding.etDescKegiatan.visibility = View.GONE
                        dialogBinding.spTypeSpj.visibility = View.VISIBLE
                    }
                    1 -> {
                        dialogBinding.spTypeSpj.visibility = View.GONE
                        dialogBinding.etDescKegiatan.visibility = View.VISIBLE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        if (::listUploadSPJ.isInitialized) {
            val adapterSPJ = ArrayAdapter<DataUploadListSPJ.Data>(this, android.R.layout.simple_spinner_item, listUploadSPJ)
            adapterSPJ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogBinding.spTypeSpj.adapter = adapterSPJ
            dialogBinding.spTypeSpj.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    idRincian = listUploadSPJ[position].id.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(false)
        dialog.show()
        dialog.window.attributes = lp

        dialogBinding.ivUpload.setOnClickListener {
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
        dialogBinding.btnUpload.setOnClickListener {
            //get last location
            RxPermissions(this)?.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    ?.subscribe { permission ->
                        if (permission) {
                            LocationServices.getFusedLocationProviderClient(this)
                                    .lastLocation.addOnSuccessListener {location->
                                Logger.d("${location.latitude} ${location.longitude}")
                                if (type == 0) {
                                    if (imageFile != null) {
                                        viewModel.postUpload(linkedMapOf(Hai.auth to prefManager.getAuthToken(),
                                                "idRincianBiaya" to idRincian,
                                                "lat" to location.latitude.toString(),
                                                "long" to location.longitude.toString()),
                                                imageFile
                                                        ?: throw NullPointerException()).observe(this, androidx.lifecycle.Observer { state ->
                                            if (state == "Upload")
                                                dialog.dismiss()
                                            if (dataBinding.tabHome.selectedTabPosition == 0)
                                                openFragment(SpjFragment())
                                        })
                                    } else
                                        Toast.makeText(this, getString(R.string.error_empty), Toast.LENGTH_SHORT).show()

                                } else {

                                    if (imageFile != null && dialogBinding.etDescKegiatan.text.toString().isNotEmpty()) {
                                        viewModel.postUpload(linkedMapOf(
                                                Hai.auth to prefManager.getAuthToken(),
                                                "id-spd" to ID,
                                                "proof" to "perjalanan",
                                                "ket" to dialogBinding.etDescKegiatan.text.toString(),
                                                "lat" to location.latitude.toString(),
                                                "long" to location.longitude.toString()
                                        ), imageFile
                                                ?: throw NullPointerException()).observe(this, androidx.lifecycle.Observer {
                                            if (it == "Upload")
                                                dialog.dismiss()
                                            if (dataBinding.tabHome.selectedTabPosition == 1)
                                                openFragment(KegiatanFragment())
                                        })
                                    } else Toast.makeText(this, getString(R.string.error_empty), Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            val builder = AlertDialog.Builder(this)
                            builder.setMessage(getString(R.string.access_location_not_allowed))
                            builder.setPositiveButton(getString(R.string.retry)) { dialog, _ -> dialog.dismiss() }
                            builder.setCancelable(false)
                            builder.show()
                        }

                    }

            viewModel.progress.observe(this, androidx.lifecycle.Observer {
                //                Logger.d("observer $it")
            })

        }
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun setupTab(title: String, icon: Int, position: Int) {
        val tabLayout = LayoutInflater.from(this).inflate(R.layout.layer_custom_tab, null)
        val textTab: TextView = tabLayout.findViewById(R.id.tv_tab)
        val iconTab: ImageView = tabLayout.findViewById(R.id.iv_tab)
        textTab.text = title
        iconTab.setImageResource(icon)
        dataBinding.tabHome.getTabAt(position)?.customView = tabLayout
    }

    private fun openFragment(fragment: Fragment) {
        //put id
        val bundle = Bundle()
        bundle.putString("id", ID)
        bundle.putBoolean("done", isDone)
        fragment.arguments = bundle
        // Begin the transaction
        val ft = supportFragmentManager.beginTransaction()
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.container_home, fragment)
        // Complete the changes added above
        ft.commit()
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
                            val resolvedIntentActivities: List<ResolveInfo> = packageManager?.queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)
                                    ?: throw Exception()
                            resolvedIntentActivities
                                    .map { it.activityInfo.packageName }
                                    .forEach { grantUriPermission(it, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION) }
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            if (cameraIntent.resolveActivity(packageManager) != null)
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
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
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
        GlideApp.with(this)
                .load(imageFile)
                .centerCrop()
                .placeholder(Hai.progressDrawable(this))
                .error(R.drawable.ic_profile)
                .into(dialogBinding.ivUpload)
        dialogBinding.tvInsertImage.visibility = View.GONE
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
