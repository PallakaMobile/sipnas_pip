package ps.sipnas.ui.profile

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import ps.sipnas.R
import ps.sipnas.base.BaseFragment
import ps.sipnas.databinding.FragmentProfileBinding
import ps.sipnas.ui.profile.aktifitas.AktifitasFragment
import ps.sipnas.ui.profile.edit.EditProfileActivity
import ps.sipnas.ui.profile.galeri.GaleriFragment
import ps.sipnas.utils.Hai
import ps.sipnas.utils.PrefManager
import java.util.*

/**
 **********************************************
 * Created by ukie on 10/23/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val viewModel by viewModel<ProfileViewModel>()
    private lateinit var prefManager: PrefManager

    override fun getLayoutResource(): Int = R.layout.fragment_profile

    override fun myCodeHere() {
        dataBinding.setLifecycleOwner(this)


        //setup custom layout tab
        setupTab(getString(R.string.activity), R.drawable.ic_spj, 0) //custom layout
        setupTab(getString(R.string.gallery), R.drawable.ic_kegiatan, 1) //custom layout

        //set upload
        dataBinding.ivEditProfile.setOnClickListener {
            startActivity(Intent(activity.applicationContext, EditProfileActivity::class.java))
        }

        //set logout
        dataBinding.btnLogout.setOnClickListener {
            AlertDialog.Builder(activity)
                    .setMessage(getString(R.string.logout_message))
                    .setPositiveButton(getString(R.string.logout)) { dialog, _ ->
                        viewModel.logout(linkedMapOf(Hai.auth to prefManager.getAuthToken()), activity)
                        dialog.dismiss()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
        }

    }

    override fun onResume() {
        super.onResume()
        val bundleAktifitas = Bundle()
        val bundleGaleri = Bundle()
        prefManager = PrefManager(activity)
        viewModel.getProfile(linkedMapOf(Hai.auth to prefManager.getAuthToken()))
                .observe(this, androidx.lifecycle.Observer {
                    bundleAktifitas.putParcelableArrayList("aktifitas", it.aktifitas as ArrayList<out Parcelable>)
                    bundleGaleri.putParcelableArrayList("galeri", it.galeri as ArrayList<out Parcelable>)

                    //set default
                    val aktifitasFragment = AktifitasFragment()
                    aktifitasFragment.arguments = bundleAktifitas
                    openFragment(aktifitasFragment)
                })
        dataBinding.viewModel = viewModel

        //set on click listener
        dataBinding.tabProfile.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0?.position) {
                    0 -> {
                        val aktifitasFragment = AktifitasFragment()
                        aktifitasFragment.arguments = bundleAktifitas
                        openFragment(aktifitasFragment)
                    }
                    1 -> {
                        val galeriFragment = GaleriFragment()
                        galeriFragment.arguments = bundleGaleri
                        openFragment(galeriFragment)
                    }
                }
            }
        })
    }

    private fun setupTab(title: String, icon: Int, position: Int) {
        val tabLayout = LayoutInflater.from(activity).inflate(R.layout.layer_custom_tab, null)
        val textTab: TextView = tabLayout.findViewById(R.id.tv_tab)
        val iconTab: ImageView = tabLayout.findViewById(R.id.iv_tab)
        textTab.text = title
        iconTab.setImageResource(icon)
        dataBinding.tabProfile.getTabAt(position)?.customView = tabLayout
    }

    private fun openFragment(fragment: Fragment) {
        // Begin the transaction
        val ft = childFragmentManager.beginTransaction()
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.container_profile, fragment)
        // Complete the changes added above
        ft.commit()
    }

}