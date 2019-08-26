package ps.sipnas.ui.profile.galeri

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.core.content.FileProvider
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import ps.sipnas.R
import ps.sipnas.base.BaseActivity
import ps.sipnas.databinding.ActivityGaleriDetailBinding
import ps.sipnas.utils.GlideApp
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class GaleriDetailActivity : BaseActivity<ActivityGaleriDetailBinding>() {

    override fun getToolbarResource(): Int = R.id.toolbar
    override fun getLayoutResource(): Int = R.layout.activity_galeri_detail

    @Suppress("DEPRECATION")
    override fun myCodeHere() {
        title = intent.extras.getString("ket")

        GlideApp.with(this)
                .asBitmap()
                .load(intent.extras.getString("image"))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        dataBinding.ivGaleri.setImageBitmap(resource)
                        dataBinding.ivGaleri.fitImageToView()
                        dataBinding.ivGaleri.setZoom(1F)
                        dataBinding.ivShareGaleri.setOnClickListener {
                            val i = Intent(Intent.ACTION_SEND)
                            i.type = "image/*"
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource))
                            startActivity(Intent.createChooser(i, "Share Image"))
                        }
                    }
                })

        dataBinding.ivGaleri.setOnTouchImageViewListener {
            if (dataBinding.ivGaleri.isZoomed) {
                dataBinding.ivShareGaleri.visibility = View.GONE
            } else {
                dataBinding.ivShareGaleri.visibility = View.VISIBLE
            }
        }
    }

    fun getLocalBitmapUri(bmp: Bitmap): Uri? {
        var bmpUri: Uri? = null
        try {
            val fileStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val nameFile = File.createTempFile(
                    "share", /* prefix */
                    ".jpg", /* suffix */
                    fileStorage/* directory */
            )
            val out = FileOutputStream(nameFile)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", nameFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bmpUri
    }
}
