package ps.sipnas.ui.home.upload.spj.detail

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
import ps.sipnas.databinding.ActivitySpjDetailBinding
import ps.sipnas.utils.GlideApp
import ps.sipnas.utils.Hai
import ps.sipnas.utils.PrefManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DetailSpjActivity : BaseActivity<ActivitySpjDetailBinding>() {
    private val viewModel by viewModel<DetailSpjViewModel>()

    override fun getToolbarResource(): Int = R.id.toolbar
    override fun getLayoutResource(): Int = R.layout.activity_spj_detail

    @Suppress("DEPRECATION")
    override fun myCodeHere() {
        title = intent.extras.getString("ket")

        if (intent.extras.getBoolean("done"))
            dataBinding.ivDeleteSpj.visibility = View.GONE

        val prefManager = PrefManager(this)

        GlideApp.with(this)
                .asBitmap()
                .load(intent.extras.getString("image"))
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        dataBinding.ivSpj.setImageBitmap(resource)
                        dataBinding.ivSpj.fitImageToView()
                        dataBinding.ivSpj.setZoom(1F)
                        dataBinding.ivShareSpj.setOnClickListener {
                            val i = Intent(Intent.ACTION_SEND)
                            i.type = "image/*"
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource))
                            startActivity(Intent.createChooser(i, "Share Image"))
                        }
                    }
                })

        dataBinding.ivSpj.setOnTouchImageViewListener {
            if (dataBinding.ivSpj.isZoomed) {
                dataBinding.llFooter.visibility = View.GONE
            } else {
                dataBinding.llFooter.visibility = View.VISIBLE
            }
        }

        dataBinding.ivDeleteSpj.setOnClickListener {
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
