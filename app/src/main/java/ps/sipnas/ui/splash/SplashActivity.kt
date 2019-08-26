package ps.sipnas.ui.splash

import android.content.Intent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ps.sipnas.R
import ps.sipnas.base.BaseActivity
import ps.sipnas.databinding.ActivitySplashBinding
import ps.sipnas.ui.MainActivity
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun getToolbarResource(): Int = 0

    override fun getLayoutResource(): Int = R.layout.activity_splash

    override fun myCodeHere() {
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
                .subscribe {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
    }

}
