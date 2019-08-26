package ps.sipnas.ui.home.detail

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ps.sipnas.base.BaseViewModel
import ps.sipnas.data.model.DataDetailSPD
import ps.sipnas.data.rest.SipnasRepository

/**
 **********************************************
 * Created by ukie on 11/4/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class DetailSPDViewModel(private val sipnasRepository: SipnasRepository) : BaseViewModel() {

    val loadingView = MutableLiveData<Boolean>()
    val content = MutableLiveData<Boolean>()
    val detailSPD = MutableLiveData<DataDetailSPD>()

    fun getDetailSPD(headers: LinkedHashMap<String, String>) {
        composite {
            sipnasRepository.detailSPD(headers)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { loadingView.value = true }
                    .doOnComplete { loadingView.value = false }
                    .subscribe({
                        it.body()?.let { it1 -> detailSPD.postValue(it1) }
                        content.value = true
                    }, {
                        loadingView.value = false
                    })
        }
    }
}