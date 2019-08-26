package ps.sipnas.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 **********************************************
 * Created by ukie on 10/1/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
abstract class BaseFragment<B : ViewDataBinding> : Fragment() {
    protected lateinit var activity: AppCompatActivity
    protected lateinit var dataBinding: B

    protected abstract fun getLayoutResource(): Int
    protected abstract fun myCodeHere()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false)
        myCodeHere()
        return dataBinding.root
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

}