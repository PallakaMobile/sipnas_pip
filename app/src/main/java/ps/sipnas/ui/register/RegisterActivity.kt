package ps.sipnas.ui.register

import ps.sipnas.R
import ps.sipnas.base.BaseActivity
import ps.sipnas.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    override fun getToolbarResource(): Int = R.id.toolbar

    override fun getLayoutResource(): Int = R.layout.activity_register

    override fun myCodeHere() {
        title = getString(R.string.user_information)
    }
}
