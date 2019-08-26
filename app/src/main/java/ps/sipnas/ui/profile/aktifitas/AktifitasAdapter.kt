package ps.sipnas.ui.profile.aktifitas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ps.sipnas.R
import ps.sipnas.data.model.DataProfile
import ps.sipnas.databinding.FragmentAktifitasItemBinding

/**
 **********************************************
 * Created by ukie on 10/24/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class AktifitasAdapter : RecyclerView.Adapter<AktifitasAdapter.AktifitasHolder>() {
    private lateinit var listAktifitas: List<DataProfile.AktifitasItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AktifitasHolder =
            AktifitasHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_aktifitas_item, parent, false))

    override fun onBindViewHolder(holder: AktifitasHolder, position: Int) = holder.bind(listAktifitas[position], position)

    override fun getItemCount() = if (::listAktifitas.isInitialized) listAktifitas.size else 0

    fun updateAktifitasList(listAktifitas: List<DataProfile.AktifitasItem>) {
        this.listAktifitas = listAktifitas
        notifyDataSetChanged()
    }

    inner class AktifitasHolder(private val binding: FragmentAktifitasItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataProfile.AktifitasItem, position: Int) = with(itemView) {
            binding.tvAktifitasStatus.visibility = if (position == 0) View.VISIBLE else View.GONE
            binding.aktifitas = data
            setOnClickListener {
            }
        }
    }
}