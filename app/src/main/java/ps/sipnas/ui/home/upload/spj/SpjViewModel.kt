package ps.sipnas.ui.home.upload.spj

import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ps.sipnas.base.BaseViewModel
import ps.sipnas.data.rest.SipnasRepository

/**
 **********************************************
 * Created by ukie on 10/15/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class SpjViewModel constructor(private val sipnasRepository: SipnasRepository) : BaseViewModel() {

    val adapterSPJ: SpjAdapter = SpjAdapter()
    val loadingView = MutableLiveData<Boolean>()
    val noDataView = MutableLiveData<Boolean>()
    val content = MutableLiveData<Boolean>()

    fun getGallery(headers: LinkedHashMap<String, String>, isDone: Boolean) {
        composite {
            sipnasRepository.imageSPJ(headers)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { loadingView.value = true }
                    .doOnComplete { loadingView.value = false }
                    .subscribe({
                        //                        Logger.d("viewModel ${it.body()?.data?.size}")
                        if (it.body()?.data?.size != 0) {
                            noDataView.value = false
                            content.value = true
                            it.body()?.data?.let { it1 -> adapterSPJ.updateDataSPJ(it1, isDone) }
                        } else {
                            content.value = false
                            noDataView.value = true
                        }
                    }, {
                        loadingView.value = false
                    })
        }
    }

}