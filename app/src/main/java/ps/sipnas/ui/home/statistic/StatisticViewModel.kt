package ps.sipnas.ui.home.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ps.sipnas.base.BaseViewModel
import ps.sipnas.data.model.DataStatistik
import ps.sipnas.data.model.DataTahun
import ps.sipnas.data.rest.SipnasRepository

/**
 **********************************************
 * Created by ukie on 11/19/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class StatisticViewModel(private val sipnasRepository: SipnasRepository) : BaseViewModel() {
    val loadingView = MutableLiveData<Boolean>()
    val statistic = MutableLiveData<DataStatistik>()
    val disconnect = MutableLiveData<Boolean>()
    val content = MutableLiveData<Boolean>()

    fun getTahun(headers: LinkedHashMap<String, String>): LiveData<DataTahun> {
        val tahunLiveData = MutableLiveData<DataTahun>()
        composite {
            sipnasRepository.tahun(headers)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe { loadingView.value = true }
                    .doOnComplete { loadingView.value = false }
                    .subscribe({
                        content.value = true
                        tahunLiveData.value = it.body() ?: throw NullPointerException()
                    }, {
                        content.value = false
                        loadingView.value = false
                        disconnect.value = true
                    })
        }
        return tahunLiveData
    }

    fun getStatistic(headers: LinkedHashMap<String, String>): LiveData<DataStatistik> {
        composite {
            sipnasRepository.statistik(headers)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        statistic.postValue(it.body() ?: throw NullPointerException())
                    }, {
                        loadingView.value = false
                        disconnect.value = true
                    })
        }
        return statistic
    }
}