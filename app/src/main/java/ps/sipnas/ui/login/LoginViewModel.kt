package ps.sipnas.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ps.sipnas.BuildConfig
import ps.sipnas.base.BaseViewModel
import ps.sipnas.data.rest.SipnasRepository
import ps.sipnas.ui.MainActivity
import ps.sipnas.utils.PrefManager

/**
 **********************************************
 * Created by ukie on 10/27/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class LoginViewModel(private val sipnasRepository: SipnasRepository) : BaseViewModel() {

    val isVisible = MutableLiveData<Boolean>()

    fun login(context: Context, username: String, password: String) {
        val prefManager = PrefManager(context)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            composite {
                sipnasRepository.login(linkedMapOf(
                        "grant_type" to BuildConfig.GRANT_TYPE,
                        "client_id" to BuildConfig.CLIENT_ID,
                        "client_secret" to BuildConfig.SECRET_KEY,
                        "username" to username,
                        "password" to password,
                        "regId" to it.token
                )).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe { isVisible.value = true }
                        .doOnComplete { isVisible.value = false }
                        .subscribe({
                            response->
                            if (response.code() == 200) {
                                //save token login
                                response.body()?.accessToken?.let { it1 -> prefManager.setAuthToken(it1) }
                                //change status login true
                                prefManager.setUserLogin(true)

                                context.startActivity(Intent(context.applicationContext, MainActivity::class.java))
                                (context as Activity).finish()
                            } else {
                                Toast.makeText(context, "NIP atau Katasandi tidak sesuai", Toast.LENGTH_SHORT).show()
                            }
                        }, { throwable ->
                            Toast.makeText(context, throwable.toString(), Toast.LENGTH_SHORT).show()
                            isVisible.value = false
                        })

            }
        }


    }
}