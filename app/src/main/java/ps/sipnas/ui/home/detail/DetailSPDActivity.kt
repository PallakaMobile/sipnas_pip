package ps.sipnas.ui.home.detail

import org.koin.androidx.viewmodel.ext.android.viewModel
import ps.sipnas.R
import ps.sipnas.base.BaseActivity
import ps.sipnas.databinding.ActivityDetailSpdBinding
import ps.sipnas.utils.Hai
import ps.sipnas.utils.PrefManager

class DetailSPDActivity : BaseActivity<ActivityDetailSpdBinding>() {
    private val viewModel by viewModel<DetailSPDViewModel>()

    override fun getToolbarResource(): Int = R.id.toolbar

    override fun getLayoutResource(): Int = R.layout.activity_detail_spd

    override fun myCodeHere() {
        title = getString(R.string.detail)
        dataBinding.setLifecycleOwner(this)
        dataBinding.viewModel = viewModel
        val prefManager = PrefManager(this)
        viewModel.getDetailSPD(
                linkedMapOf(
                        Hai.auth to prefManager.getAuthToken(),
                        "idSpd" to intent.extras.getString("id")
                )
        )
    }
}
