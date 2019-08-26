package ps.sipnas.ui.home.done

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ps.sipnas.R
import ps.sipnas.base.BaseFragment
import ps.sipnas.databinding.FragmentDoneBinding
import ps.sipnas.utils.Hai
import ps.sipnas.utils.PrefManager
import ps.sipnas.utils.RxEditTextBinding
import java.util.concurrent.TimeUnit

//TODO Bug on show hide in recyclerview
class DoneFragment : BaseFragment<FragmentDoneBinding>() {
    private val viewModel by viewModel<DoneViewModel>()
    private val rxBinding by inject<RxEditTextBinding>()

    private var currentPage = 1
    private var query = ""
    var lastPage = 0
    private lateinit var prefManager: PrefManager
    private val compositeDisposable = CompositeDisposable()

    private var isLoading = false

    override fun getLayoutResource(): Int = R.layout.fragment_done

    override fun myCodeHere() {
        dataBinding.setLifecycleOwner(this)
        dataBinding.viewModel = viewModel
        prefManager = PrefManager(activity)

        getDoneList("", true)

        //search listener
        compositeDisposable.add(rxBinding.getTextWatcherObservable(dataBinding.etSearch)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
                .subscribe {
                    query = it
                    currentPage = 1
                    getDoneList(query, true)
                }
        )
        dataBinding.rvDone.layoutManager = LinearLayoutManager(activity)

        //pagination
        dataBinding.rvDone.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
//                Logger.d("countItem: $countItem")
//                Logger.d("lastVisiblePosition: $lastVisiblePosition")
//                Logger.d("isLastPosition: $isLastPosition")
                if (!isLoading && isLastPosition && currentPage < lastPage) {
                    showLoading()
                    currentPage = currentPage.plus(1)
                    getDoneList(query, false)
//                    Logger.d("Load More $currentPage")
                }
            }
        })
    }

    private fun getDoneList(query: String, isFirst: Boolean) {
        viewModel.getDoneList(linkedMapOf(
                Hai.auth to prefManager.getAuthToken(),
                "page" to currentPage.toString(),
                "q" to query
        ), isFirst)
                .observe(this, Observer {
                    lastPage = it.last_page ?: 0
                    hideLoading()
                })
    }

    private fun showLoading() {
        isLoading = true
        dataBinding.llLoadMore.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        isLoading = false
        dataBinding.llLoadMore.visibility = View.GONE
    }
}
