package ps.sipnas.utils

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ps.sipnas.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * *********************************************
 * Created by ukie on 9/27/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 * *********************************************
 * © 2018 | All Right Reserved
 */

@BindingAdapter("app:loadingVisibility")
fun loadingVisibility(view: View?, isVisible: Boolean?) {
    if (isVisible != null)
        if (isVisible)
            view?.visibility = View.VISIBLE
        else
            view?.visibility = View.GONE
}

@BindingAdapter("app:updateText")
fun updateText(view: Button?, text: String?) {
    if (text != null) {
//        Logger.d("binding adapter $text")
        view?.text = text
    }
}

@BindingAdapter("app:contentVisibility")
fun contentVisibility(view: View?, isVisible: Boolean?) {
//    Logger.d("content ${isVisible.toString()}")
    if (isVisible != null)
        if (isVisible)
            view?.visibility = View.VISIBLE
        else
            view?.visibility = View.GONE
}

@BindingAdapter("app:noDataVisibility")
fun noDataVisibility(view: View?, isVisible: Boolean?) {
    if (isVisible != null)
        if (isVisible)
            view?.visibility = View.VISIBLE
        else
            view?.visibility = View.GONE
}


@BindingAdapter("app:disconnect")
fun disconnect(view: View?, isVisible: Boolean?) {
//    Logger.d("disconnect ${isVisible.toString()}")
    if (isVisible != null)
        if (isVisible)
            view?.visibility = View.VISIBLE
        else
            view?.visibility = View.GONE
}

@BindingAdapter("app:isEnable")
fun setEnable(view: View?, isEnable: Boolean?) {
    if (isEnable != null)
        view?.isEnabled = !isEnable

}

@BindingAdapter("app:rvAdapter")
fun mutableRvAdapter(view: RecyclerView?, adapter: RecyclerView.Adapter<*>) {
    view?.adapter = adapter
}

@BindingAdapter("app:imageGrid")
fun imageGrid(view: ImageView?, url: String?) {
    if (url != null)
        GlideApp.with(view?.context ?: throw NullPointerException())
                .load(url)
                .placeholder(Hai.progressDrawable(view.context))
                .error(R.drawable.ic_profile)
                .centerCrop()
                .into(view)
}

@BindingAdapter("app:imageGridSmall")
fun imageGridSmall(view: ImageView?, url: String?) {
    if (url != null)
        GlideApp.with(view?.context ?: throw NullPointerException())
                .load(url)
                .placeholder(Hai.progressDrawable(view.context))
                .error(R.drawable.ic_profile)
                .centerCrop()
                .into(view)
}

@BindingAdapter("app:imageRound")
fun imageRound(view: ImageView?, url: String?) {
    if (url != null)
        GlideApp.with(view?.context ?: throw NullPointerException())
                .load(url)
                .placeholder(Hai.progressDrawable(view.context))
                .error(R.drawable.ic_profile)
                .circleCrop()
                .into(view)
}

@BindingAdapter("app:time", "app:date")
fun convertTime(view: TextView?, time: String?, tanggal: String?) {
    if (time != null && tanggal != null) {
        val clock24 = SimpleDateFormat("HH:mm:ss", Locale("id", "ID"))
        clock24.timeZone = TimeZone.getTimeZone("GMT+7")
        val date = clock24.parse(time)
        view?.text = "$tanggal ${SimpleDateFormat("K:mm a").format(date)}"
    }
}

@BindingAdapter("app:status")
fun statusProses(view: AppCompatTextView?, status: String?) {
    if (status != null) {
        if (view != null) {
            view.text = status
            if (status.toLowerCase() == "belum lengkap")
                view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_not_completed, 0, 0, 0)
            else view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_completed, 0, 0, 0)
        }
    }
}