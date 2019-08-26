package ps.sipnas.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 **********************************************
 * Created by ukie on 10/8/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */

class RxEditTextBinding {
    private var view: EditText? = null
    fun getTextWatcherObservable(view: EditText): Observable<String> {
        this.view = view
        val subject = PublishSubject.create<String>()
        this.view?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                subject.onNext(s.toString())
//                Logger.d(s.toString())
            }
        })
        return subject
    }
}