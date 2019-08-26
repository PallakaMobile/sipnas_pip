package ps.sipnas.ui.home.process

import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ps.sipnas.base.BaseViewModel
import ps.sipnas.data.model.DataProses
import ps.sipnas.data.rest.SipnasRepository

/**
 **********************************************
 * Created by ukie on 10/31/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class ProcessViewModel(private val sipnasRepository: SipnasRepository) : BaseViewModel() {
    val adapter = ProcessAdapter()
    val dataProses = MutableLiveData<DataProses.Data>()
    val loadingView = MutableLiveData<Boolean>()
    val noDataView = MutableLiveData<Boolean>()
    val disconnect = MutableLiveData<Boolean>()
    val content = MutableLiveData<Boolean>()

    fun getProcess(headers: LinkedHashMap<String, String>): MutableLiveData<String> {
        val id = MutableLiveData<String>()
        composite {
            sipnasRepository.proses(headers)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { loadingView.value = true }
                    .doOnComplete { loadingView.value = false }
                    .subscribe({
                        if (it.body()?.data?.id != "") { // check if data empty
                            content.value = true
                            noDataView.value = false
                            dataProses.postValue(it.body()?.data)
                            it.body()?.data?.rincianBiaya?.let { it1 -> adapter.updateProcessAdapter(it1) }
                            id.postValue(it.body()?.data?.id)
                        } else {
                            noDataView.value = true
                        }
                    }, {
                        //                        Logger.d(it.printStackTrace())
                        disconnect.value = true
                        noDataView.value = false
                        content.value = false
                        loadingView.value = false
//                        Logger.d("error ${disconnect.value}")
                    })
        }
        return id
    }
}