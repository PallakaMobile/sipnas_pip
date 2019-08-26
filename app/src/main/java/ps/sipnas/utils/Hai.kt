package ps.sipnas.utils

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable

/**
 **********************************************
 * Created by ukie on 10/31/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
object Hai {
    const val auth = "Authorization"

    fun progressDrawable(context: Context): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 10f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        return circularProgressDrawable
    }
}