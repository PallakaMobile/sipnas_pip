package ps.sipnas.ui

import android.content.Intent
import com.google.firebase.iid.FirebaseInstanceId
import ps.sipnas.R
import ps.sipnas.base.BaseActivity
import ps.sipnas.databinding.ActivityMainBinding
import ps.sipnas.ui.about.AboutFragment
import ps.sipnas.ui.home.HomeFragment
import ps.sipnas.ui.login.LoginActivity
import ps.sipnas.ui.notification.NotificationFragment
import ps.sipnas.ui.profile.ProfileFragment
import ps.sipnas.utils.PrefManager
import ps.sipnas.utils.ViewPagerAdapter

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getToolbarResource(): Int = 0
    override fun getLayoutResource(): Int = R.layout.activity_main
    private val tabIcons = intArrayOf(R.drawable.ic_home, R.drawable.ic_notification, R.drawable.ic_profile, R.drawable.ic_about)

    override fun myCodeHere() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            //            Logger.d("Fcm ${it.token}")
        }

        val prefManager = PrefManager(this)
        //go to login
        if (!prefManager.getUserLogin()) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
            return
        }

        //setup view pager
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(HomeFragment())
        viewPagerAdapter.addFragment(NotificationFragment())
        viewPagerAdapter.addFragment(ProfileFragment())
        viewPagerAdapter.addFragment(AboutFragment())
        dataBinding.container.adapter = viewPagerAdapter

        //setup tablayout
        dataBinding.tabs.setupWithViewPager(dataBinding.container)
        for (i in 0..3) {
            dataBinding.tabs.getTabAt(i)?.setIcon(tabIcons[i])
        }
    }
}
