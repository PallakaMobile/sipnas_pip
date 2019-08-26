package ps.sipnas.ui.home.done

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ps.sipnas.R
import ps.sipnas.data.model.DataDone
import ps.sipnas.databinding.FragmentDoneRincianBiayaItemBinding

/**
 **********************************************
 * Created by ukie on 11/1/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class DoneRincianBiayaAdapter : RecyclerView.Adapter<DoneRincianBiayaAdapter.BiayaHolder>() {
    private lateinit var dataRincianBiayaList: List<DataDone.RincianBiaya>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BiayaHolder =
            BiayaHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_done_rincian_biaya_item, parent, false))

    override fun onBindViewHolder(holder: BiayaHolder, position: Int) = holder.bindRincianBiaya(dataRincianBiayaList[position])

    override fun getItemCount() = if (::dataRincianBiayaList.isInitialized) dataRincianBiayaList.size else 0

    fun updateDoneRincianBiayaAdapter(dataRincianBiayaList: List<DataDone.RincianBiaya>) {
        this.dataRincianBiayaList = dataRincianBiayaList
        notifyDataSetChanged()
    }

    inner class BiayaHolder(private val binding: FragmentDoneRincianBiayaItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindRincianBiaya(dataRincianBiaya: DataDone.RincianBiaya) = with(itemView) {
            // TODO: Bind data dengan View
            binding.rincianBiaya = dataRincianBiaya
            setOnClickListener {
                // TODO: Action ketika item di klik
            }
        }
    }
}