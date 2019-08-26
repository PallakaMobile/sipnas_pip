package ps.sipnas.ui.profile

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ps.sipnas.base.BaseViewModel
import ps.sipnas.data.model.DataProfile
import ps.sipnas.data.rest.SipnasRepository
import ps.sipnas.ui.splash.SplashActivity
import ps.sipnas.utils.PrefManager

/**
 **********************************************
 * Created by ukie on 10/23/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class ProfileViewModel(private val sipnasRepository: SipnasRepository) : BaseViewModel() {
    var dataProfile = MutableLiveData<DataProfile.Data>()
    val loadingView = MutableLiveData<Boolean>()
    val disconnect = MutableLiveData<Boolean>()
    val content = MutableLiveData<Boolean>()

    fun getProfile(headers: LinkedHashMap<String, String>): LiveData<DataProfile.Data> {
        val liveDataProfile = MutableLiveData<DataProfile.Data>()
        composite {
            sipnasRepository.profile(headers)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { loadingView.value = true }
                    .doOnComplete { loadingView.value = false }
                    .subscribe({
                        content.value = true
                        liveDataProfile.postValue(it.body()?.data ?: throw NullPointerException())
                        dataProfile.postValue(it.body()?.data ?: throw NullPointerException())
                    }, {
                        //                        Logger.d(it.printStackTrace())
                        loadingView.value = false
                        disconnect.value = true
                        content.value = false
                    })
        }
        return liveDataProfile
    }

    fun logout(headers: LinkedHashMap<String, String>, activity: Activity) {
        composite {
            sipnasRepository.logout(headers)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { loadingView.value = true }
                    .doOnComplete { loadingView.value = false }
                    .subscribe({
                        content.value = true
                        Toast.makeText(activity, it.body()?.message, Toast.LENGTH_SHORT).show()
                        val prefManager = PrefManager(activity)
                        prefManager.userLogout()
                        activity.finishAffinity()
                        activity.startActivity(Intent(activity, SplashActivity::class.java))
                    }, {
                        loadingView.value = false
                        disconnect.value = true
                        content.value = false
                    })
        }
    }
}