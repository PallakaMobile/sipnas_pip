package ps.sipnas.ui.profile.galeri

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ps.sipnas.R
import ps.sipnas.data.model.DataProfile
import ps.sipnas.databinding.FragmentGalleryItemBinding

/**
 **********************************************
 * Created by ukie on 10/24/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class GaleriAdapter : RecyclerView.Adapter<GaleriAdapter.GridHolder>() {
    private lateinit var listGallery: List<DataProfile.GaleriItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridHolder =
            GridHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_gallery_item, parent, false))

    override fun onBindViewHolder(holder: GridHolder, position: Int) = holder.bind(listGallery[position])

    override fun getItemCount() = if (::listGallery.isInitialized) listGallery.size else 0

    fun updateGallery(listGaleri: List<DataProfile.GaleriItem>) {
        this.listGallery = listGaleri
        notifyDataSetChanged()
    }

    inner class GridHolder(private val binding: FragmentGalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataProfile.GaleriItem) = with(itemView) {
            //TODO Move to Binding Adapter
            binding.url = data.imageUrl

            // TODO: Bind data dengan View
            setOnClickListener {
                val detail = Intent(it.context.applicationContext, GaleriDetailActivity::class.java)
                detail.putExtra("image", data.imageUrl)
                detail.putExtra("ket", data.ket)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(it.context as Activity, binding.ivGallery, "galeri")
                    (it.context as Activity).startActivity(detail, options.toBundle())
                } else {
                    (it.context as Activity).startActivity(detail)
                }
            }
        }
    }
}