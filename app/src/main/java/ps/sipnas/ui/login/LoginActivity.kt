package ps.sipnas.ui.login

import android.content.Intent
import android.text.InputType
import android.view.inputmethod.EditorInfo
import org.koin.androidx.viewmodel.ext.android.viewModel
import ps.sipnas.R
import ps.sipnas.base.BaseActivity
import ps.sipnas.databinding.ActivityLoginBinding
import ps.sipnas.ui.register.RegisterActivity


class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private val viewModel by viewModel<LoginViewModel>()

    override fun getToolbarResource(): Int = 0

    override fun getLayoutResource(): Int = R.layout.activity_login

    override fun myCodeHere() {
        dataBinding.setLifecycleOwner(this)
        dataBinding.viewModel = viewModel
        dataBinding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        dataBinding.etPassword.maxLines = 1
        dataBinding.etPassword.imeOptions = EditorInfo.IME_ACTION_DONE
        dataBinding.tvRegister.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }
    }

}
