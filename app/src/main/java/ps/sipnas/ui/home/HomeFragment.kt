package ps.sipnas.ui.home

import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import ps.sipnas.R
import ps.sipnas.base.BaseFragment
import ps.sipnas.databinding.FragmentHomeBinding
import ps.sipnas.ui.home.done.DoneFragment
import ps.sipnas.ui.home.process.ProcessFragment
import ps.sipnas.ui.home.statistic.StatisticFragment

/**
 **********************************************
 * Created by ukie on 10/12/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_home
    override fun myCodeHere() {
        //setup custom layout tab
        setupTab(getString(R.string.process), R.drawable.ic_process, 0) //custom layout
        setupTab(getString(R.string.done), R.drawable.ic_done, 1) //custom layout
        setupTab(getString(R.string.statistic), R.drawable.ic_statistic, 2) //custom layout

        //set default
        openFragment(ProcessFragment())

        //set on click listener
        dataBinding.tabHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                when (p0?.position) {
                    0 -> {
                        openFragment(ProcessFragment())
                    }
                    1 -> {
                        openFragment(DoneFragment())
                    }
                    2 -> {
                        openFragment(StatisticFragment())
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
        dataBinding.tabHome.getTabAt(position)?.customView = tabLayout
    }

    private fun openFragment(fragment: Fragment) {
        // Begin the transaction
        val ft = childFragmentManager.beginTransaction()
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.container_home, fragment)
        // Complete the changes added above
        ft.commit()
    }
}