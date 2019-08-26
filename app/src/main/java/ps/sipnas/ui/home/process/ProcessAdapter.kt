package ps.sipnas.ui.home.process

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ps.sipnas.R
import ps.sipnas.data.model.DataProses
import ps.sipnas.databinding.FragmentProcessItemBinding

/**
 **********************************************
 * Created by ukie on 10/31/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class ProcessAdapter : RecyclerView.Adapter<ProcessAdapter.ProcessHolder>() {
    private lateinit var dataDataRincianBiayaList: List<DataProses.RincianBiaya>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProcessHolder =
            ProcessHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_process_item, parent, false))

    override fun onBindViewHolder(holder: ProcessHolder, position: Int) = holder.bindDataRincianBiaya(dataDataRincianBiayaList[position])

    override fun getItemCount() = if (::dataDataRincianBiayaList.isInitialized) dataDataRincianBiayaList.size else 0

    fun updateProcessAdapter(dataDataRincianBiayaList: List<DataProses.RincianBiaya>) {
        this.dataDataRincianBiayaList = dataDataRincianBiayaList
        notifyDataSetChanged()
    }

    inner class ProcessHolder(private val binding: FragmentProcessItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindDataRincianBiaya(dataDataRincianBiaya: DataProses.RincianBiaya) = with(itemView) {
            //            Logger.d(dataDataRincianBiaya.biaya)
            binding.rincianBiaya = dataDataRincianBiaya
            // TODO: Bind data dengan View
            setOnClickListener {
                // TODO: Action ketika item di klik
            }
        }
    }
}