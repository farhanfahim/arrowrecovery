package com.tekrevol.arrowrecovery.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.kaopiz.kprogresshud.KProgressHUD
import com.tekrevol.arrowrecovery.BaseApplication
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.DaysSelectorAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants.*
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.receiving_model.*
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.utils.DayAxisValueFormatter
import com.tekrevol.arrowrecovery.utils.XYMarkerView
import com.tekrevol.arrowrecovery.widget.SwitchMultiButton
import com.tekrevol.arrowrecovery.widget.TitleBar
import io.objectbox.Box
import io.objectbox.query.QueryBuilder
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment(), OnItemClickListener {

    private var arrData: ArrayList<DummyModel> = ArrayList()
    private lateinit var daysSelectorAdapter: DaysSelectorAdapter
    var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    val materialHistoryBox: Box<MaterialHistoryModelDataBase> by lazy {
        requireNotNull(this) {
            "You can only initialize DB after onActivityCreated()"
        }

        BaseApplication.getApp().boxStore.boxFor(MaterialHistoryModelDataBase::class.java)
    }

    private var mDialog: KProgressHUD? = null

    var x: Int = 0
    var webCallCollection: Call<WebResponse<Any>>? = null
    private val materialHistoryModel = MaterialHistoryModel()

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

        bindGraphData()
        onBind()

    }

    private fun onBind() {
        arrData.clear()
        arrData.addAll(Constants.daysSelector())
        rvDays.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvDays.adapter = daysSelectorAdapter

        //var size : Int = getSize(materialHistoryBox);

        //materialHistoryBox.removeAll()
        if (materialHistoryBox.isEmpty) {
            fetchData(getStartingDate(), getCurrentDate())
        } else {
            updateData(getCurrentDate())
        }

        //getStartAndEndDate()
    }

    private fun updateData(date: String) {

        var endDate: Date = materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().findFirst()!!.date
        var currentDate: Date = dateFormat.parse(date)

        if (!currentDate.after(endDate)) {
            updateCurrentPrice()
        } else {
            var callStart: Calendar = Calendar.getInstance()
            var endCalendar: Calendar = Calendar.getInstance()
            callStart.time = currentDate
            var updatedDate: String = ""
            if (callStart.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                callStart.add(Calendar.DAY_OF_YEAR, -2)
                updatedDate = dateFormat.format(callStart.time)
                updateData(updatedDate)
            } else if (callStart.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                callStart.add(Calendar.DAY_OF_YEAR, -1)
                updatedDate = dateFormat.format(callStart.time)
                updateData(updatedDate)
            } else {
                endCalendar.time = endDate
                endCalendar.add(Calendar.DAY_OF_YEAR, 1)
                var updatedDt: String = ""
                updatedDt = dateFormat.format(endCalendar.time)
                val strCurrentDate = dateFormat.format(currentDate)

                if (strCurrentDate == updatedDt) {
                    updateCurrentPrice()
                } else {
                    fetchData(updatedDt, strCurrentDate, true)
                }
            }
        }


    }

    private fun getStartingDate(): String {
        val prevYear = Calendar.getInstance()
        prevYear.add(Calendar.YEAR, -5)
        var year: String = prevYear.get(Calendar.YEAR).toString()
        var month: String = prevYear.get(Calendar.MONTH).toString()
        var day: String = prevYear.get(Calendar.DATE).toString()
        var m: Int = month.toInt() + 1
        return "$year-$month-$day"
    }

    private fun getCurrentDate(): String {

        val prevYear = Calendar.getInstance()
        var year: String = prevYear.get(Calendar.YEAR).toString()
        var month: String = prevYear.get(Calendar.MONTH).toString()
        var day: String = prevYear.get(Calendar.DATE).toString()
        var m: Int = month.toInt() + 1
        return "$year-$m-$day"
    }

    private fun fetchData(startDate: String, endDate: String, isUpdated: Boolean = false) {

        mDialog = UIHelper.getProgressHUD(context)
        mDialog?.show()

//        var size: Int = 0
//        if (isUpdated) {
//            if (!materialHistoryBox.isEmpty) {
//                size = materialHistoryBox.all.size
//            }
//        }

        val queryMap = HashMap<String, Any>()
        queryMap[Q_KEY_FROM] = startDate
        queryMap[Q_KEY_TO] = endDate
        queryMap[Q_ORDER_BY] = Q_ORDER_BY_DATE
        queryMap[Q_SORTED] = Q_ASC

        webCallCollection = getBaseWebServices(false).getAPIAnyObject(KEY_MATERIAL_HISTORY, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : com.google.gson.reflect.TypeToken<java.util.ArrayList<MaterialHistoryModel?>?>() {}.type
                val arrayList: java.util.ArrayList<MaterialHistoryModel> = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type)

                if (webResponse.isSuccess) {

//                    for (item in arrayList) {
//                        val date: Date = dateFormat.parse(item.date)
//                        val priceDb: MaterialHistoryModelDataBase = MaterialHistoryModelDataBase(item.updated_at, item.created_at, item.rhodium_price, item.palladium_price, item.platinum_price, item.currency, date, item.id, item.getuId())
//                        materialHistoryBox.put(priceDb)
//
//
//
//                        if (isUpdated) {
//                            if (materialHistoryBox.isEmpty) {
//                            } else if (getSize(materialHistoryBox) == (arrayList.size + size)) {
//                                updateCurrentPrice(materialHistoryBox)
//
//                            }
//                        }
//
//                        if (getSize(materialHistoryBox) == arrayList.size) {
//                            updateCurrentPrice(materialHistoryBox)
//                            setDay()
//                        }
//
//                    }
//
//                    if (isUpdated) {
//                        if (!materialHistoryBox.isEmpty) {
//                            updateCurrentPrice(materialHistoryBox)
//                        }
//                    }


                    for (item in arrayList) {
                        val date: Date = dateFormat.parse(item.date)
                        val priceDb: MaterialHistoryModelDataBase = MaterialHistoryModelDataBase(item.updated_at, item.created_at, item.rhodium_price, item.palladium_price, item.platinum_price, item.currency, date, item.id, item.getuId())
                        materialHistoryBox.put(priceDb)

                    }


                    //if (arrayList.isNotEmpty()){
                    updateCurrentPrice()
                    setDay()
                    //}

                }

                if (imgArrow != null || progressRefresh != null) {
                    imgArrow.visibility = View.VISIBLE
                    progressRefresh.visibility = View.GONE
                    mDialog?.dismiss()
                }

            }

            override fun onError(`object`: Any?) {

                if (imgArrow != null || progressRefresh != null) {
                    imgArrow.visibility = View.VISIBLE
                    progressRefresh.visibility = View.GONE
                    mDialog?.dismiss()
                }
            }
        })
    }

    private fun updateCurrentPrice() {

        drawGraph()
        val date: Date? = materialHistoryBox.query()?.order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING)?.build()?.findFirst()!!.date
        var previousDate: Date? = getPreviousDate(date)

        previousDate = checkPreviousDateExistInDb(previousDate!!)
//        var previousDate: Date = dateFormat.parse("")
        date?.let {
            previousDate?.let {

                prices(date, previousDate)
            }
        }
    }

    fun prices(currentDate: Date, previousDate: Date) {

        val platanumCurrentPrice: Double = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, currentDate).build().findFirst()?.platinum_price!!.toDouble()
        val platanumPreviousPrice: Double = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, previousDate).build().findFirst()?.platinum_price!!.toDouble()

        val palladiumCurrentPrice: Double = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, currentDate).build().findFirst()?.palladium_price!!.toDouble()
        val palladiumPreviousPrice: Double = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, previousDate).build().findFirst()?.palladium_price!!.toDouble()

        val rhodiumCurrentPrice: Double = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, currentDate).build().findFirst()?.rhodium_price!!.toDouble()
        val rhodiumPreviousPrice: Double = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, previousDate).build().findFirst()?.rhodium_price!!.toDouble()

        val platinumDifference = ((platanumCurrentPrice - platanumPreviousPrice)) / platanumPreviousPrice
        val palladiumDifference = ((palladiumCurrentPrice - palladiumPreviousPrice)) / palladiumPreviousPrice
        val rhodiumDifference = ((rhodiumCurrentPrice - rhodiumPreviousPrice)) / rhodiumPreviousPrice

        val platinumEstimatedPrice = platinumDifference * 100
        val palladiumEstimatedPrice = palladiumDifference * 100
        val rhodiumEstimatedPrice = rhodiumDifference * 100

        if (platinumEstimatedPrice < 0) {
            txtPlatinumPerc.text = String.format("%.2f", platinumEstimatedPrice * -1) + "%"
        } else {
            txtPlatinumPerc.text = String.format("%.2f", platinumEstimatedPrice) + "%"
        }

        if (palladiumEstimatedPrice < 0) {
            txtPalladiumPerc.text = String.format("%.2f", palladiumEstimatedPrice * -1) + "%"
        } else {
            txtPalladiumPerc.text = String.format("%.2f", palladiumEstimatedPrice) + "%"
        }

        if (rhodiumEstimatedPrice < 0) {
            txtRhodiumPerc.text = String.format("%.2f", rhodiumEstimatedPrice * -1) + "%"
        } else {
            txtRhodiumPerc.text = String.format("%.2f", rhodiumEstimatedPrice) + "%"
        }
        txtPlatinumPrice.text = "$platanumCurrentPrice $/Oz t"
        txtPalladiumPrice.text = "$palladiumCurrentPrice $/Oz t"
        txtRhodiumPrice.text = "$rhodiumCurrentPrice $/Oz t"

        if (platinumEstimatedPrice == 0.0) {

            imgPlatinumStatus.setImageResource(R.drawable.minus)
            imgPlatinumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.quantum_grey))
        } else if (platinumEstimatedPrice < 0) {

            imgPlatinumStatus.setImageResource(R.drawable.img_arrow_down)
            imgPlatinumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.colorAccent))
        } else {

            imgPlatinumStatus.setImageResource(R.drawable.img_arrow_up)
            imgPlatinumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.colorPrimary))
        }

        if (palladiumEstimatedPrice == 0.0) {

            imgPalladiumStatus.setImageResource(R.drawable.minus)
            imgPalladiumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.quantum_grey))
        } else if (palladiumEstimatedPrice < 0) {

            imgPalladiumStatus.setImageResource(R.drawable.img_arrow_down)
            imgPalladiumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.colorAccent))
        } else {

            imgPalladiumStatus.setImageResource(R.drawable.img_arrow_up)
            imgPalladiumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.colorPrimary))
        }

        if (rhodiumEstimatedPrice == 0.0) {

            imgRhodiumStatus.setImageResource(R.drawable.minus)
            imgRhodiumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.quantum_grey))
        } else if (rhodiumEstimatedPrice < 0) {

            imgRhodiumStatus.setImageResource(R.drawable.img_arrow_down)
            imgRhodiumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.colorAccent))
        } else {

            imgRhodiumStatus.setImageResource(R.drawable.img_arrow_up)
            imgRhodiumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.colorPrimary))
        }

    }

    fun checkPreviousDateExistInDb(previousDate: Date): Date {

        var previousDateNew: Date
        val platanumPreviousPrice = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, previousDate).build().count()
        if (platanumPreviousPrice != null && platanumPreviousPrice > 0) {
            previousDateNew = previousDate
        } else {
            var calendar: Calendar = Calendar.getInstance()
            // var currentDate: Date = dateFormat.parse(platanumCurrentDate)
            calendar.time = previousDate
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            var d: Date = calendar.time

            previousDateNew= checkIfDayIsSundayOrSaturday(d)

            checkPreviousDateExistInDb(previousDateNew)
        }

        return previousDateNew

    }

    fun checkIfDayIsSundayOrSaturday(date: Date): Date {
        var calendar: Calendar = Calendar.getInstance()
        calendar.time = date

        var d: Date = calendar.time

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, -2)
            d = calendar.time
            //   getPreviousDate(updatedDate)
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            d = calendar.time
            //     Updatedata(updatedDate)
        }

        return d

    }

    private fun getPreviousDate(currentDate: Date?): Date? {

        var calendar: Calendar = Calendar.getInstance()
        // var currentDate: Date = dateFormat.parse(platanumCurrentDate)
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        var d: Date = calendar.time



        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, -2)
            d = calendar.time
            //   getPreviousDate(updatedDate)
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            d = calendar.time
            //     Updatedata(updatedDate)
        }
        //var previousDate: String = dateFormat.format(calendar.time)
        var date: String = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, d).build().find().toString()
        if (date.isNullOrEmpty()) {
            getPreviousDate(currentDate)
            return null
        } else {
            return d
        }

    }

    private fun getPreviousDate2(currentDate: Date?): Date? {

        var calendar: Calendar = Calendar.getInstance()
        // var currentDate: Date = dateFormat.parse(platanumCurrentDate)
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, -4)
        var d: Date = calendar.time

//        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
//            calendar.add(Calendar.DAY_OF_YEAR, -2)
//            d = calendar.time
//            //   getPreviousDate(updatedDate)
//        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
//            calendar.add(Calendar.DAY_OF_YEAR, -1)
//            d = calendar.time
//            //     Updatedata(updatedDate)
//        }
        //var previousDate: String = dateFormat.format(calendar.time)
//        var date: String = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, d).build().find().toString()
//        if (date.isNullOrEmpty()) {
//            getPreviousDate(currentDate)
//            return null
//        } else {
//            return d
//        }
        return d
    }

    private fun bindGraphData() {
//        chart.setViewPortOffsets(0f, 0f, 0f, 0f)
        chart.setExtraOffsets(10f, 10f, 10f, 10f)
        // chart.setBackgroundColor(Color.rgb(104, 241, 175))
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
        chart.setPinchZoom(true)
        chart.isDoubleTapToZoomEnabled = false

        chart.setDrawGridBackground(false)
//        chart.maxHighlightDistance = 300f

        val x = chart.xAxis
        x.isEnabled = false

        val y = chart.axisLeft
        y.isEnabled = true
        y.setDrawLabels(true)
        y.setLabelCount(6, false)
        y.textColor = ContextCompat.getColor(context!!, R.color.txtDarkGrey)
//        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        y.setDrawGridLines(true)
        y.axisLineColor = Color.BLACK

        chart.axisRight.isEnabled = false
        chart.legend.isEnabled = false

        chart.animateXY(1000, 1000)

        // don't forget to refresh the drawing
        chart.invalidate()
    }

    /*private fun setData(count: Int, range: Float) {
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
    }*/

    private fun setData(range: List<MaterialHistoryModelDataBase>) {

        var priceList: ArrayList<Double> = ArrayList()
        var dateList: ArrayList<Date> = ArrayList()
        priceList.clear()
        dateList.clear()
        for (arr in range) {
            dateList.add(arr.date)

            if (switchMultiButton.selectedTab == 0) {
                priceList.add(arr.platinum_price)
            } else if (switchMultiButton.selectedTab == 1) {
                priceList.add(arr.palladium_price)
            } else if (switchMultiButton.selectedTab == 2) {
                priceList.add(arr.rhodium_price)
            }

        }

        val values = java.util.ArrayList<Entry>()
        currentPrice.text = priceList[range.size - 1].toString()
        for (a in range.indices) {
            values.add(Entry(a.toFloat(), priceList[a].toFloat()))
        }

        val set1: LineDataSet
        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
            // create marker to display box when values are selected
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
            //     set1.fillFormatter = IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
            // create a data object with the data sets
            val data = LineData(set1)
//            data.setValueTypeface(tfLight)
            data.setValueTextSize(9f)
            data.setDrawValues(false)
            // set data
            chart.data = data
        }

        val xAxisFormatter = DayAxisValueFormatter(chart, dateList)
        val mv = XYMarkerView(activity, xAxisFormatter)
        mv.chartView = chart
        chart.marker = mv


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
            materialHistoryBox.removeAll()
            imgArrow.visibility = View.GONE
            progressRefresh.visibility = View.VISIBLE
            fetchData(getStartingDate(), getCurrentDate())
        }

        switchMultiButton.setOnSwitchListener(object : SwitchMultiButton.OnSwitchListener {
            override fun onSwitch(position: Int, tabText: String) {
                setDay()
                drawGraph()
            }
        })
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, anyObject: Any?, view: View?, type: String?) {
        arrData.forEach { it.isSelected = false }
        arrData[position].isSelected = true
        if (position == 0) {

            var currentDate: Date = materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().findFirst()?.date!!
            var calendar: Calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.DAY_OF_YEAR, -7)
            var prevDate = calendar.time

            var range: List<MaterialHistoryModelDataBase> = materialHistoryBox.query().between(MaterialHistoryModelDataBase_.date, currentDate, prevDate).build().find()

            setData(range)

        } else if (position == 1) {
            var currentDate: Date = materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().findFirst()?.date!!
            var calendar: Calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.MONTH, -1)
            var prevDate = calendar.time


            var range: List<MaterialHistoryModelDataBase> = materialHistoryBox.query().between(MaterialHistoryModelDataBase_.date, currentDate, prevDate).build().find()

            setData(range)

        } else if (position == 2) {

            var currentDate: Date = materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().findFirst()?.date!!
            var calendar: Calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.MONTH, -6)
            var prevDate = calendar.time


            var range: List<MaterialHistoryModelDataBase> = materialHistoryBox.query().between(MaterialHistoryModelDataBase_.date, currentDate, prevDate).build().find()

            setData(range)


        } else if (position == 3) {

            var currentDate: Date = materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().findFirst()?.date!!
            var calendar: Calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.YEAR, -1)
            var prevDate = calendar.time


            var range: List<MaterialHistoryModelDataBase> = materialHistoryBox.query().between(MaterialHistoryModelDataBase_.date, currentDate, prevDate).build().find()

            setData(range)
        } else if (position == 4) {
            var currentDate: Date = materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().findFirst()?.date!!
            var calendar: Calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.YEAR, -5)
            var prevDate = calendar.time


            var range: List<MaterialHistoryModelDataBase> = materialHistoryBox.query().between(MaterialHistoryModelDataBase_.date, currentDate, prevDate).build().find()

            setData(range)

        }

        daysSelectorAdapter.notifyDataSetChanged()

        // contRefresh.performClick()

    }

    override fun onDestroyView() {
        webCallCollection?.cancel()
        mDialog?.dismiss()
        super.onDestroyView()
    }


    /**
     * get size//////////////////////////////
     */

    fun getSize(materialHistoryBox: Box<MaterialHistoryModelDataBase>): Int {
        return materialHistoryBox.all.size
    }

    fun drawGraph() {
        var currentDate: Date = materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().findFirst()?.date!!
        var calendar: Calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        var prevDate = calendar.time
        var range: List<MaterialHistoryModelDataBase> = ArrayList()
        range = materialHistoryBox.query().between(MaterialHistoryModelDataBase_.date, currentDate, prevDate).build().find()

        setData(range)
    }

    fun setDay() {
        arrData.forEach { it.isSelected = false }
        arrData[0].isSelected = true
        daysSelectorAdapter.notifyDataSetChanged()
    }

}
