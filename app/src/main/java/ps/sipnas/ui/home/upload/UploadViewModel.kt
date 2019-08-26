package ps.sipnas.ui.home.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import ps.sipnas.base.BaseViewModel
import ps.sipnas.data.model.DataUploadListSPJ
import ps.sipnas.data.rest.SipnasRepository
import ps.sipnas.utils.ProgressRequestBody
import java.io.File

/**
 **********************************************
 * Created by ukie on 11/2/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class UploadViewModel(private val sipnasRepository: SipnasRepository) : BaseViewModel(), ProgressRequestBody.UploadCallbacks {
    val progress = MutableLiveData<String>()

    fun getListUploadSPJ(headers: LinkedHashMap<String, String>): LiveData<DataUploadListSPJ> {
        val uploadListSPJ = MutableLiveData<DataUploadListSPJ>()
        composite {
            sipnasRepository.listUploadSPJ(headers)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        uploadListSPJ.postValue(it.body() ?: throw NullPointerException())
                    }, {
                        it.printStackTrace()
                    })
        }
        return uploadListSPJ
    }

    fun postUpload(headers: LinkedHashMap<String, String>, image: File): LiveData<String> {

        val progressRequestBody = ProgressRequestBody(image, "image", this)
        val body = MultipartBody.Part.createFormData("image", image.name, progressRequestBody)
        composite {
            sipnasRepository.uploadSPJKegiatan(headers, body)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        onFinish()
                    }, {
                        it.printStackTrace()
                    })
        }
        return progress
    }

    override fun onProgressUpdate(percentage: Int) {
//        Logger.d(" progress $percentage")
        progress.value = "Uploading $percentage%"
    }

    override fun onError() {
    }

    override fun onFinish() {
        progress.value = "Upload"
    }
}