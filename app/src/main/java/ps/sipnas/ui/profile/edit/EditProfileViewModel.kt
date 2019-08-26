package ps.sipnas.ui.profile.edit

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ps.sipnas.base.BaseViewModel
import ps.sipnas.data.model.DataProfile
import ps.sipnas.data.rest.SipnasRepository
import ps.sipnas.utils.Hai
import ps.sipnas.utils.PrefManager
import java.io.File

/**
 **********************************************
 * Created by ukie on 12/10/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class EditProfileViewModel(private val sipnasRepository: SipnasRepository) : BaseViewModel() {
    var dataProfile = MutableLiveData<DataProfile.Data>()
    val loadingView = MutableLiveData<Boolean>()
    val loadingPicture = MutableLiveData<Boolean>()
    val disconnect = MutableLiveData<Boolean>()
    val content = MutableLiveData<Boolean>()
    val isVisible = MutableLiveData<Boolean>()

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
                        loadingView.value = false
                        disconnect.value = true
                        content.value = false
                    })
        }
        return liveDataProfile
    }

    fun profilePict(headers: LinkedHashMap<String, String>, image: File): LiveData<String> {
        val progress = MutableLiveData<String>()
        val requestBody = RequestBody.create(MediaType.parse("image/*"), image)
        val body = MultipartBody.Part.createFormData("image", image.name, requestBody)
        composite {
            sipnasRepository.profilPict(headers, body)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { loadingPicture.value = true }
                    .doOnComplete { loadingPicture.value = false }
                    .subscribe({
                        progress.postValue(it.body()?.status)
                        content.value = true
                    }, {
                        loadingPicture.value = false
                        disconnect.value = true
                        content.value = false
                    })
        }
        return progress
    }

    fun updatePass(context: Context, currentPass: String, newPass: String, reNewPass: String) {
        val prefManager = PrefManager(context)
        composite {
            sipnasRepository.resetPass(linkedMapOf(Hai.auth to prefManager.getAuthToken(),
                    "old-password" to currentPass,
                    "password" to newPass,
                    "password-confirm" to reNewPass))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { isVisible.value = true }
                    .doOnComplete { isVisible.value = false }
                    .subscribe({
                        content.value = true
                        Toast.makeText(context, it.body()?.msg, Toast.LENGTH_LONG).show()
                        (context as Activity).finish()
                    }, {
                        isVisible.value = false
                        disconnect.value = true
                        content.value = false
                    })
        }
    }
}