package ps.sipnas.ui.home.statistic

import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import ps.sipnas.R
import ps.sipnas.base.BaseFragment
import ps.sipnas.data.model.DataStatistik
import ps.sipnas.data.model.DataTahun
import ps.sipnas.databinding.FragmentStatisticBinding
import ps.sipnas.support.graphview.ValueDependentColor
import ps.sipnas.support.graphview.series.BarGraphSeries
import ps.sipnas.support.graphview.series.DataPoint
import ps.sipnas.utils.Hai
import ps.sipnas.utils.PrefManager


class StatisticFragment : BaseFragment<FragmentStatisticBinding>() {
    private val viewModel by viewModel<StatisticViewModel>()

    override fun getLayoutResource(): Int = R.layout.fragment_statistic

    override fun myCodeHere() {
        dataBinding.setLifecycleOwner(this)
        val prefManager = PrefManager(activity)


        dataBinding.viewModel = viewModel

        val observerStatistic = Observer<DataStatistik> {
            //create month list name
            val monthName = arrayListOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Ags", "Sep", "Okt", "Nov", "Des")

            val month = ArrayList<Int>()
            val size = it.statistik?.size ?: 0

            //get all month in statistik
            for (items in 0 until size) {
                month.add(it.statistik?.get(items)?.bulan ?: 0)
            }
            //remove duplicate month using distinct
            val monthDistinct = month.distinct()

            //create custom radio button for select month
            val params = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(16, 0, 16, 0)
            dataBinding.rgMonth.orientation = LinearLayout.HORIZONTAL
//            Logger.d("items ${monthDistinct.size}")

            if (monthDistinct.isNotEmpty()) { // if month > 0 try to handle null radio button
                for (i in 0 until monthDistinct.size) {
                    val rdBtn = AppCompatRadioButton(activity)
                    rdBtn.id = i
                    rdBtn.text = "${monthName[monthDistinct[i] - 1]} "
                    rdBtn.buttonDrawable = null
                    rdBtn.setBackgroundResource(R.drawable.month_bg)
                    rdBtn.setTextColor(ContextCompat.getColorStateList(activity, R.color.month_text))
                    rdBtn.layoutParams = params
                    dataBinding.rgMonth.addView(rdBtn)
                }

                (dataBinding.rgMonth.getChildAt(0) as RadioButton).isChecked = true
                //default checked
                //filter data by month from checked id
                val filterByMonth = it.statistik?.filter { filter -> filter.bulan == monthDistinct[0] }
                //insert data using filterByMonth
                val dataChart = filterByMonth?.size?.let { it1 -> arrayOfNulls<DataPoint>(it1) }
                dataBinding.gvStatistic.removeAllSeries()

                if (filterByMonth != null) {
                    for (item in 0 until filterByMonth.size) {

//                    Logger.d("loop $item")
                        dataChart?.set(item, DataPoint(
                                filterByMonth[item].minggu?.toDouble() ?: 0.0,
                                filterByMonth[item].jumlahOrang?.toDouble() ?: 0.0
                        ))

                    }
                    val series: BarGraphSeries<DataPoint?> = BarGraphSeries(dataChart)

                    series.spacing = 50 // 50% spacing between bars
                    series.isAnimated = true
                    // styling
                    series.valueDependentColor = ValueDependentColor {
                        ContextCompat.getColor(activity, R.color.blue)
                    }
                    series.dataWidth = 1.0

                    // draw values on top
                    series.isDrawValuesOnTop = true
                    series.valuesOnTopColor = ContextCompat.getColor(activity, R.color.blue)

                    dataBinding.gvStatistic.addSeries(series)
                }
            }


            //check on checked listener
            dataBinding.rgMonth.setOnCheckedChangeListener { _, checkedId ->

                //filter data by month from checked id
                val filterByMonth = it.statistik?.filter { filter -> filter.bulan == monthDistinct[checkedId] }
                //insert data using filterByMonth
                val dataChart = filterByMonth?.size?.let { it1 -> arrayOfNulls<DataPoint>(it1) }
                dataBinding.gvStatistic.removeAllSeries()

                if (filterByMonth != null) {
                    for (item in 0 until filterByMonth.size) {

//                        Logger.d("loop $item")
                        dataChart?.set(item, DataPoint(
                                filterByMonth[item].minggu?.toDouble() ?: 0.0,
                                filterByMonth[item].jumlahOrang?.toDouble() ?: 0.0
                        ))

                    }
                    val series: BarGraphSeries<DataPoint?> = BarGraphSeries(dataChart)

                    series.spacing = 50 // 50% spacing between bars
                    series.isAnimated = true
                    // styling
                    series.valueDependentColor = ValueDependentColor {
                        ContextCompat.getColor(activity, R.color.blue)
                    }
                    series.dataWidth = 1.0

                    // draw values on top
                    series.isDrawValuesOnTop = true
                    series.valuesOnTopColor = ContextCompat.getColor(activity, R.color.blue)

                    dataBinding.gvStatistic.addSeries(series)
                }
            }
        }

        viewModel.getTahun(linkedMapOf(Hai.auth to prefManager.getAuthToken())).observe(this, Observer {

            //            Logger.d("viewModel Year ${it.tahun}")
            val adapterYear = ArrayAdapter<DataTahun.TahunItem>(activity, android.R.layout.simple_spinner_item, it.tahun)
            adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dataBinding.spStatistic.adapter = adapterYear
            dataBinding.spStatistic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val tahun = it.tahun?.get(position)?.year ?: "2018"
//                    Logger.d(tahun)
                    viewModel.getStatistic(
                            linkedMapOf(
                                    Hai.auth to prefManager.getAuthToken(),
                                    "tahun" to tahun
                            )).observe(this@StatisticFragment, observerStatistic)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        })

        //series.setValuesOnTopSize(50);

        // set the viewport wider than the data, to have a nice view
        dataBinding.gvStatistic.viewport.setMinY(1.0)
        dataBinding.gvStatistic.viewport.setMinX(0.0)
        dataBinding.gvStatistic.viewport.setMaxX(6.0)
        dataBinding.gvStatistic.viewport.isXAxisBoundsManual = true
        dataBinding.gvStatistic.viewport.borderColor = ContextCompat.getColor(activity, R.color.white_soft)
        dataBinding.gvStatistic.gridLabelRenderer.setHumanRounding(true)
        dataBinding.gvStatistic.gridLabelRenderer.horizontalAxisTitle = "Minggu Ke"
        dataBinding.gvStatistic.gridLabelRenderer.verticalAxisTitle = "Jumlah Peserta"
        dataBinding.gvStatistic.gridLabelRenderer.numHorizontalLabels = 7
        dataBinding.gvStatistic.viewport.isScalable = true
        dataBinding.gvStatistic.viewport.isScrollable = true
        dataBinding.gvStatistic.gridLabelRenderer.gridColor = ContextCompat.getColor(activity, R.color.white_soft)
    }
}
