package ps.sipnas.ui.home.upload.spj.detail

import android.app.Activity
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ps.sipnas.base.BaseViewModel
import ps.sipnas.data.rest.SipnasRepository
import ps.sipnas.ui.home.upload.kegiatan.SpjFragment

/**
 **********************************************
 * Created by ukie on 11/6/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class DetailSpjViewModel(private val sipnasRepository: SipnasRepository) : BaseViewModel() {

    fun deleteImage(headers: LinkedHashMap<String, String>, id: String, activity: Activity) {
        composite {
            sipnasRepository.deleteImage(headers, id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Toast.makeText(activity, it.body()?.status, Toast.LENGTH_SHORT).show()
                        SpjFragment.refresh = true
                        activity.finish()
                    }, {
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    })
        }
    }
}