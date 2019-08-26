package ps.sipnas.ui.profile.galeri

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import ps.sipnas.R
import ps.sipnas.base.BaseFragment
import ps.sipnas.data.model.DataProfile
import ps.sipnas.databinding.FragmentGalleryBinding

/**
 **********************************************
 * Created by ukie on 10/24/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class GaleriFragment : BaseFragment<FragmentGalleryBinding>() {

    override fun getLayoutResource(): Int = R.layout.fragment_gallery

    override fun myCodeHere() {
        val listGallery: List<DataProfile.GaleriItem> = arguments?.getParcelableArrayList("galeri")
                ?: throw NullPointerException()
        if (listGallery.isEmpty()) {
            dataBinding.tvNoData.visibility = View.VISIBLE
            dataBinding.rvGallery.visibility = View.GONE
        } else {
            val adapter = GaleriAdapter()
            dataBinding.rvGallery.layoutManager = GridLayoutManager(activity, 3)
            dataBinding.rvGallery.setHasFixedSize(true)
            dataBinding.rvGallery.adapter = adapter
            adapter.updateGallery(listGallery)
        }
    }

}