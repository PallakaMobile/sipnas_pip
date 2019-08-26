package ps.sipnas.ui.home.upload.kegiatan.detail

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.view.View
import androidx.core.content.FileProvider
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import org.koin.androidx.viewmodel.ext.android.viewModel
import ps.sipnas.R
import ps.sipnas.base.BaseActivity
import ps.sipnas.databinding.ActivityKegiatanDetailBinding
import ps.sipnas.utils.GlideApp
import ps.sipnas.utils.Hai
import ps.sipnas.utils.PrefManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DetailKegiatanActivity : BaseActivity<ActivityKegiatanDetailBinding>() {
    private val viewModel by viewModel<DetailKegiatanViewModel>()
    override fun getToolbarResource(): Int = R.id.toolbar

    override fun getLayoutResource(): Int = R.layout.activity_kegiatan_detail

    @Suppress("DEPRECATION")
    override fun myCodeHere() {
        title = intent.extras.getString("ket")
        if (intent.extras.getBoolean("done"))
            dataBinding.ivDeleteKegiatan.visibility = View.GONE
        val prefManager = PrefManager(this)

        GlideApp.with(this)
                .asBitmap()
                .load(intent.extras.getString("image"))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        dataBinding.ivKegiatan.setImageBitmap(resource)
                        dataBinding.ivKegiatan.fitImageToView()
                        dataBinding.ivKegiatan.setZoom(1F)
                        dataBinding.ivShareKegiatan.setOnClickListener {
                            val i = Intent(Intent.ACTION_SEND)
                            i.type = "image/*"
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource))
                            startActivity(Intent.createChooser(i, "Share Image"))
                        }
                    }
                })

        dataBinding.ivKegiatan.setOnTouchImageViewListener {
            if (dataBinding.ivKegiatan.isZoomed) {
                dataBinding.llFooter.visibility = View.GONE
            } else {
                dataBinding.llFooter.visibility = View.VISIBLE
            }
        }

        dataBinding.ivDeleteKegiatan.setOnClickListener {
            viewModel.deleteImage(linkedMapOf(
                    Hai.auth to prefManager.getAuthToken()), intent.extras.getString("id"), this
            )
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
