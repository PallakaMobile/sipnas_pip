package ps.sipnas.ui.about

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ps.sipnas.R
import ps.sipnas.base.BaseFragment
import ps.sipnas.databinding.FragmentAboutBinding
import ps.sipnas.ui.aboutimport.AboutAdapter

/**
 **********************************************
 * Created by ukie on 10/24/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class AboutFragment : BaseFragment<FragmentAboutBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_about
    override fun myCodeHere() {
        val aboutAdapter = AboutAdapter()
        dataBinding.rvAbout.layoutManager = LinearLayoutManager(activity)
        dataBinding.rvAbout.setHasFixedSize(true)
        dataBinding.rvAbout.adapter = aboutAdapter
        aboutAdapter.updateAboutAdapter(
                listOf(About(ContextCompat.getDrawable(activity, R.drawable.ic_about_location), "Alamat", "Jl.Tentara Pelajar No.173, Malimongan Tua, Wajo, Kota Makassar, Sulawesi Selatan 90165"),
                        About(ContextCompat.getDrawable(activity, R.drawable.ic_about_phone), "Telepon", "(0411) 3616975"),
                        About(ContextCompat.getDrawable(activity, R.drawable.ic_about_mail), "Alamat Email", "informatikapip@gmail.com"))
        )
    }

    data class About(
            val logo: Drawable? = null,
            val title: String? = "",
            val desc: String? = ""
    )
}