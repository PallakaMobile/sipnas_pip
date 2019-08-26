package ps.sipnas.ui.profile.aktifitas

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import ps.sipnas.R
import ps.sipnas.base.BaseFragment
import ps.sipnas.data.model.DataProfile
import ps.sipnas.databinding.FragmentAktifitasBinding

/**
 **********************************************
 * Created by ukie on 10/24/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class AktifitasFragment : BaseFragment<FragmentAktifitasBinding>() {

    override fun getLayoutResource(): Int = R.layout.fragment_aktifitas

    override fun myCodeHere() {
        dataBinding.rvAktifitas.setHasFixedSize(true)
        dataBinding.rvAktifitas.layoutManager = LinearLayoutManager(activity)

        val listAktifitas: List<DataProfile.AktifitasItem> = arguments?.getParcelableArrayList("aktifitas")
                ?: throw NullPointerException()

        Logger.d(listAktifitas)

        if (listAktifitas.isEmpty()) {
            dataBinding.tvNoData.visibility = View.VISIBLE
            dataBinding.rvAktifitas.visibility = View.GONE
        } else {
            val adapter = AktifitasAdapter()
            dataBinding.rvAktifitas.adapter = adapter
            dataBinding.rvAktifitas.layoutManager = LinearLayoutManager(activity)
            adapter.updateAktifitasList(listAktifitas)
        }


    }

}