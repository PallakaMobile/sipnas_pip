package ps.sipnas.ui.notification

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ps.sipnas.base.BaseViewModel
import ps.sipnas.data.rest.SipnasRepository

/**
 **********************************************
 * Created by ukie on 10/21/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class NotificationViewModel(private val sipnasRepository: SipnasRepository) : BaseViewModel() {
    val adapter = NotificationAdapter()
    val loadingView = MutableLiveData<Boolean>()
    val noDataView = MutableLiveData<Boolean>()
    val disconnect = MutableLiveData<Boolean>()
    val content = MutableLiveData<Boolean>()

    fun getNotification(headers: LinkedHashMap<String, String>) {
        composite {
            sipnasRepository.notification(headers)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { loadingView.value = true }
                    .doOnComplete { loadingView.value = false }
                    .subscribe({
                        if (it.body()?.data?.isNotEmpty() ?: throw NullPointerException()) {
                            noDataView.value = false
                            content.value = true
                            it.body()?.data?.let { it1 -> adapter.updateAdapterNotification(it1) }
                        } else {
                            content.value = false
                            noDataView.value = true
                        }
                    }, {
                        loadingView.value = false
                        disconnect.value = true
                        noDataView.value = false
                    })
        }
    }
}