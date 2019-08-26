package ps.sipnas.ui.home.done

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ps.sipnas.base.BaseViewModel
import ps.sipnas.data.model.DataDone
import ps.sipnas.data.rest.SipnasRepository
import java.util.*

/**
 **********************************************
 * Created by ukie on 10/20/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class DoneViewModel(private val sipnasRepository: SipnasRepository) : BaseViewModel() {
    val adapterDone = DoneAdapter()
    val loadingView = MutableLiveData<Boolean>()
    val noDataView = MutableLiveData<Boolean>()
    val disconnect = MutableLiveData<Boolean>()
    val content = MutableLiveData<Boolean>()

    fun getDoneList(headers: LinkedHashMap<String, String>, isFirst: Boolean): LiveData<DataDone.Meta> {
        val pagination = MutableLiveData<DataDone.Meta>()
        composite {
            sipnasRepository.done(headers)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { if (isFirst) loadingView.value = true }
                    .doOnComplete { if (isFirst) loadingView.value = false }
                    .subscribe({
                        if (it.body()?.data?.isNotEmpty() ?: throw NullPointerException()) {
                            content.value = true
                            noDataView.value = false
                            adapterDone.updateAdapterDone(it.body()?.data as ArrayList<DataDone.Data>, isFirst)
                            pagination.postValue(it.body()?.meta)
                        } else {
                            content.value = false
                            noDataView.value = true
                        }
                    }, {
                        content.value = false
                        noDataView.value = false
                        loadingView.value = false
                        disconnect.value = true
                    })
        }
        return pagination
    }

}