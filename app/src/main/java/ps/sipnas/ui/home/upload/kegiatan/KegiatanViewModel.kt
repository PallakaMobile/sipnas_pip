package ps.sipnas.ui.home.upload.kegiatan

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
class KegiatanViewModel constructor(private val sipnasRepository: SipnasRepository) : BaseViewModel() {

    val adapterKegiatan = KegiatanAdapter()
    val loadingView = MutableLiveData<Boolean>()
    val noDataView = MutableLiveData<Boolean>()
    val content = MutableLiveData<Boolean>()

    fun getGallery(headers: LinkedHashMap<String, String>, isDone: Boolean) {
        composite {
            sipnasRepository.imageKegiatan(headers)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { loadingView.value = true }
                    .doOnComplete { loadingView.value = false }
                    .subscribe({
                        //                        Logger.d("viewModel ${it.body()?.data?.size}")
                        if (it.body()?.data?.size != 0) {
                            noDataView.value = false
                            content.value = true
                            it.body()?.data?.let { it1 -> adapterKegiatan.updateDataKegiatan(it1, isDone) }
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