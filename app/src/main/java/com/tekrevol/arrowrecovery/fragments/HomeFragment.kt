package com.tekrevol.arrowrecovery.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.DaysSelectorAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.receiving_model.UserModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), OnItemClickListener {

    private var arrData: ArrayList<DummyModel> = ArrayList()
    private lateinit var daysSelectorAdapter: DaysSelectorAdapter

    companion object {

        fun newInstance(): HomeFragment {

            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        daysSelectorAdapter = DaysSelectorAdapter(context!!, arrData, this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBind()
        bindGraphData()

    }

    private fun onBind() {
        arrData.clear()
        arrData.addAll(Constants.daysSelector())
        rvDays.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvDays.adapter = daysSelectorAdapter

    }


    private fun bindGraphData() {
        chart.setViewPortOffsets(0f, 0f, 0f, 0f)
        //        chart.setBackgroundColor(Color.rgb(104, 241, 175))

        // no description text
        // no description text
        chart.description.isEnabled = false

        // enable touch gestures
        // enable touch gestures
        chart.setTouchEnabled(true)

        // enable scaling and dragging
        // enable scaling and dragging
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)
        chart.maxHighlightDistance = 300f

        val x = chart.xAxis
        x.isEnabled = false

        val y = chart.axisLeft
        //        y.typeface = tfLight
        y.setLabelCount(6, false)
        y.textColor = ContextCompat.getColor(context!!, R.color.txtDarkGrey)
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        y.setDrawGridLines(false)
        y.axisLineColor = Color.WHITE

        chart.axisRight.isEnabled = false

        chart.legend.isEnabled = false

        chart.animateXY(2000, 2000)

        // don't forget to refresh the drawing
        // don't forget to refresh the drawing
        chart.invalidate()
    }

    private fun setData(count: Int, range: Float) {
        val values = java.util.ArrayList<Entry>()
        for (i in 0 until count) {
            val `val` = (Math.random() * (range + 1)).toFloat() + 20
            values.add(Entry(i.toFloat(), `val`))
        }
        val set1: LineDataSet
        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else { // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.cubicIntensity = 0.2f
            set1.setDrawFilled(true)
            set1.setDrawCircles(false)
            set1.lineWidth = 3f
            set1.circleRadius = 4f
            set1.setCircleColor(Color.BLUE)
            set1.highLightColor = Color.rgb(244, 117, 117)
            set1.color = ContextCompat.getColor(context!!, R.color.colorPrimary)
            set1.fillColor = Color.BLACK
            set1.fillAlpha = 0
            set1.setDrawHorizontalHighlightIndicator(false)
            set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
            // create a data object with the data sets
            val data = LineData(set1)
//            data.setValueTypeface(tfLight)
            data.setValueTextSize(9f)
            data.setDrawValues(false)
            // set data
            chart.data = data
        }

        // redraw
        chart.invalidate()
    }

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_home
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {
        contRefresh.setOnClickListener {
            imgArrow.visibility = View.GONE
            progressRefresh.visibility = View.VISIBLE

            Handler().postDelayed({
                imgArrow.visibility = View.VISIBLE
                progressRefresh.visibility = View.GONE
                setData((2 until 40).random(), (2 until 40).random().toFloat())
            }, 2000)


        }
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, anyObject: Any?, view: View?, type: String?) {
        arrData.forEach { it.isSelected = false }
        arrData[position].isSelected = true
        daysSelectorAdapter.notifyDataSetChanged()

        contRefresh.performClick()

    }


}