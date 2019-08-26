package ps.sipnas.ui.home.upload.kegiatan

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ps.sipnas.R
import ps.sipnas.data.model.DataImageKegiatan
import ps.sipnas.databinding.FragmentGalleryKegiatanItemBinding
import ps.sipnas.ui.home.upload.kegiatan.detail.DetailKegiatanActivity


/**
 **********************************************
 * Created by ukie on 10/15/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class KegiatanAdapter : RecyclerView.Adapter<KegiatanAdapter.GridHolder>() {
    private lateinit var listDataKegiatan: List<DataImageKegiatan.Data>
    private var isDone = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridHolder =
            GridHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_gallery_kegiatan_item, parent, false))

    override fun onBindViewHolder(holder: GridHolder, position: Int) = holder.bind(listDataKegiatan[position], isDone)

    override fun getItemCount() = if (::listDataKegiatan.isInitialized) listDataKegiatan.size else 0

    fun updateDataKegiatan(listDataKegiatan: List<DataImageKegiatan.Data>, isDone: Boolean) {
        this.listDataKegiatan = listDataKegiatan
        this.isDone = isDone
        notifyDataSetChanged()
    }

    inner class GridHolder(private val binding: FragmentGalleryKegiatanItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataImageKegiatan.Data, isDone: Boolean) = with(itemView) {
            //TODO Move to Binding Adapter
            binding.url = data.imageUrl

            // TODO: Bind data dengan View
            setOnClickListener {
                KegiatanFragment.refresh = false
                val detail = Intent(it.context.applicationContext, DetailKegiatanActivity::class.java)
                detail.putExtra("image", data.imageUrl)
                detail.putExtra("title", data.ket)
                detail.putExtra("id", data.id)
                detail.putExtra("ket", data.ket)
                detail.putExtra("done", isDone)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(it.context as Activity, binding.ivKegiatan, "kegiatan")
                    (it.context as Activity).startActivity(detail, options.toBundle())
                } else {
                    (it.context as Activity).startActivity(detail)
                }
            }
        }
    }
}