package ps.sipnas.ui.aboutimport

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ps.sipnas.R
import ps.sipnas.databinding.FragmentAboutItemBinding
import ps.sipnas.ui.about.AboutFragment


/**
 **********************************************
 * Created by ukie on 10/24/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class AboutAdapter : RecyclerView.Adapter<AboutAdapter.AboutHolder>() {
    private lateinit var list: List<AboutFragment.About>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutHolder =
            AboutHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_about_item, parent, false))

    override fun onBindViewHolder(holder: AboutHolder, position: Int) = holder.bind(list[position], position)

    override fun getItemCount() = if (::list.isInitialized) list.size else 0

    fun updateAboutAdapter(list: List<AboutFragment.About>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class AboutHolder(private val binding: FragmentAboutItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: AboutFragment.About, position: Int) = with(itemView) {
            binding.dataAbout = data
            if (position == 2) binding.vDivider.visibility = View.GONE
            setOnClickListener {
                when (position) {
                    0 -> {
                        val gmmIntentUri = Uri.parse("geo:0,0?q=${data.desc}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        itemView.context.startActivity(mapIntent)
                    }
                    1 -> {
                        val phoneIntent = Intent(Intent.ACTION_DIAL)
                        phoneIntent.data = Uri.parse("tel:+624113616975")
                        itemView.context.startActivity(phoneIntent)
                    }
                    2 -> {
                        val mailIntent = Intent(Intent.ACTION_SENDTO)
                        mailIntent.data = Uri.parse("mailto:${data.desc}")
                        itemView.context.startActivity(mailIntent)
                    }
                }
            }
        }
    }
}