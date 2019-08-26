package ps.sipnas.ui.home.done

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ps.sipnas.R
import ps.sipnas.data.model.DataDone
import ps.sipnas.databinding.FragmentDoneItemBinding
import ps.sipnas.ui.home.detail.DetailSPDActivity
import ps.sipnas.ui.home.upload.UploadActivity
import java.util.*

/**
 **********************************************
 * Created by ukie on 10/20/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class DoneAdapter : RecyclerView.Adapter<DoneAdapter.DoneHolder>() {
    private lateinit var listDone: ArrayList<DataDone.Data>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoneHolder =
            DoneHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_done_item, parent, false))

    override fun onBindViewHolder(holder: DoneHolder, position: Int) = holder.bind(listDone[position], position)

    override fun getItemCount() = if (::listDone.isInitialized) listDone.size else 0

    fun updateAdapterDone(listDoneData: ArrayList<DataDone.Data>, isFirst: Boolean) {
        if (::listDone.isInitialized && !isFirst) //update data when page >1
            listDone.addAll(listDoneData)
        else
            listDone = listDoneData
//        Logger.d("Size ${this.listDone.size}")
        notifyDataSetChanged()
    }

    inner class DoneHolder(private val binding: FragmentDoneItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataDone.Data, position: Int) = with(itemView) {
            //random background
            val listBackground = listOf(R.drawable.blue_gradient, R.drawable.green_gradient, R.drawable.pink_gradient)
            val random = listBackground.random()
            binding.rlRoot.setBackgroundResource(random ?: R.drawable.blue_gradient)
            binding.data = data

            val rincianBiayaAdapter = DoneRincianBiayaAdapter()
            rincianBiayaAdapter.updateDoneRincianBiayaAdapter(data.rincianBiaya
                    ?: throw NullPointerException())
            binding.rvDetailDone.adapter = rincianBiayaAdapter
            binding.rvDetailDone.layoutManager = LinearLayoutManager(itemView.context)

            binding.rlRoot.setOnClickListener {
                val detail = Intent(itemView.context.applicationContext, DetailSPDActivity::class.java)
                detail.putExtra("id", data.id)
                itemView.context.startActivity(detail)
            }

            binding.ivGallery.setOnClickListener {
                val uploadActivity = Intent(itemView.context.applicationContext, UploadActivity::class.java)
                uploadActivity.putExtra("id", data.id)
                uploadActivity.putExtra("done", true)
                itemView.context.startActivity(uploadActivity)
            }

            //set visibility detail rincian
            var isExpand = false
            rotateImage(180F)
            binding.llRincian.visibility = View.GONE
            binding.tvRincianBiayaTitle.setOnClickListener {
                if (isExpand) {
                    binding.llRincian.invalidate()
                    binding.llRincian.requestLayout()
                    rotateImage(180F)
                    binding.llRincian.visibility = View.GONE
                    notifyItemChanged(position)
                    isExpand = false
                } else {
                    rotateImage(0F)
                    binding.llRincian.visibility = View.VISIBLE
                    isExpand = true
                }
            }
            setOnClickListener {
            }
        }

        private fun <E> List<E>.random(): E? = if (size > 0) get(Random().nextInt(size)) else null

        private fun rotateImage(rotation: Float) {
            val icon = BitmapFactory.decodeResource(itemView.resources, R.drawable.ic_expand)
            val matrix = Matrix()
            matrix.postRotate(rotation)
            val targetBitmap = Bitmap.createBitmap(icon, 0, 0, icon.width, icon.height, matrix, true)

            binding.tvRincianBiayaTitle
                    .setCompoundDrawablesWithIntrinsicBounds(BitmapDrawable(itemView.resources, targetBitmap), null, null, null)
        }
    }
}