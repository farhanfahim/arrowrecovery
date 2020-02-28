package com.tekrevol.arrowrecovery.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.tekrevol.arrowrecovery.BaseApplication
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.DaysSelectorAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants.*
import com.tekrevol.arrowrecovery.enums.DBType
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.receiving_model.*
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
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
    val boxStore = BaseApplication.getApp().boxStore
    val materialHistoryBox = boxStore.boxFor(MaterialHistoryModelDataBase::class.java)
    val historyList: java.util.ArrayList<MaterialHistoryModel> = java.util.ArrayList()

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

        onBind()
        bindGraphData()

    }

    private fun onBind() {
        arrData.clear()
        arrData.addAll(Constants.daysSelector())
        rvDays.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvDays.adapter = daysSelectorAdapter

        //var size : Int = getSize(materialHistoryBox);

        removeAll(materialHistoryBox)
        if (materialHistoryBox.isEmpty) {
            fetchData(getStartingDate(), getCurrentDate())
        } else {
            Updatedata(getCurrentDate())

        }

        //getStartAndEndDate()
    }

    private fun Updatedata(date: String) {

        var datee: Date = materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().findFirst()!!.date
        var dateMaterial: String = dateFormat.format(datee)
        if (date <= dateMaterial) {
            UpdateCurrentPrice(materialHistoryBox)
        } else if (date > dateMaterial) {
            var myDate: Date = dateFormat.parse(date)
            var callStart: Calendar = Calendar.getInstance()
            var endDate: Calendar = Calendar.getInstance()
            callStart.time = myDate
            var updatedDate: String = ""
            if (callStart.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                callStart.add(Calendar.DAY_OF_YEAR, -2)
                updatedDate = dateFormat.format(callStart.time)
                Updatedata(updatedDate)
            } else if (callStart.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                callStart.add(Calendar.DAY_OF_YEAR, -1)
                updatedDate = dateFormat.format(callStart.time)
                Updatedata(updatedDate)
            } else {
                var endDatee: Date = dateFormat.parse(dateMaterial)
                endDate.time = endDatee
                endDate.add(Calendar.DAY_OF_YEAR, 1)
                var updatedDt: String = ""
                updatedDt = dateFormat.format(endDate.time)

                if (date == updatedDt) {
                    UpdateCurrentPrice(materialHistoryBox)
                } else {
                    fetchData(updatedDt, date)

                }
            }
        }
    }

    private fun getStartAndEndDate() {
        var dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        var myDate: Date = dateFormat.parse("2020-02-02")
        var callStart: Calendar = Calendar.getInstance()
        var callEnd: Calendar = Calendar.getInstance()
        var callDate: Calendar = Calendar.getInstance()
        var startDate: String = ""
        var endDate: String = ""
        callStart.time = myDate
        callEnd.time = myDate

        if (callStart.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

            callEnd.add(Calendar.DAY_OF_YEAR, -2)
            endDate = dateFormat.format(callEnd.time)
            callStart.add(Calendar.DAY_OF_YEAR, -4)
            startDate = dateFormat.format(callStart.time)
            priceApi(startDate, endDate)

        } else if (callStart.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {

            callEnd.add(Calendar.DAY_OF_YEAR, -1)
            endDate = dateFormat.format(callEnd.time)
            callStart.add(Calendar.DAY_OF_YEAR, -3)
            startDate = dateFormat.format(callStart.time)
            priceApi(startDate, endDate)

        } else {
            endDate = "2020-02-01"
            callEnd.add(Calendar.DAY_OF_YEAR, -2)
            var date: Date = dateFormat.parse(dateFormat.format(callEnd.time))
            callDate.time = date

            if (callDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                callStart.add(Calendar.DAY_OF_YEAR, -4)
                startDate = dateFormat.format(callStart.time)
                priceApi(startDate, endDate)

            } else if (callDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                callStart.add(Calendar.DAY_OF_YEAR, -3)
                startDate = dateFormat.format(callStart.time)
                priceApi(startDate, endDate)

            } else {
                callStart.add(Calendar.DAY_OF_YEAR, -2)
                startDate = dateFormat.format(callStart.time)
                priceApi(startDate, endDate)
            }
        }
    }

    private fun priceApi(startDate: String, endDate: String) {
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

    private fun fetchData(startDate: String, endDate: String) {

        val queryMap = HashMap<String, Any>()
        queryMap[Q_KEY_FROM] = startDate
        queryMap[Q_KEY_TO] = endDate
        webCallCollection = getBaseWebServices(true).getAPIAnyObject(KEY_MATERIAL_HISTORY, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : com.google.gson.reflect.TypeToken<java.util.ArrayList<MaterialHistoryModel?>?>() {}.type
                val arrayList: java.util.ArrayList<MaterialHistoryModel> = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type)


                if (webResponse.isSuccess) {

                    for (arr in arrayList) {
                        var date: Date = dateFormat.parse(arr.date)
                        var priceDb: MaterialHistoryModelDataBase = MaterialHistoryModelDataBase(arr.updated_at, arr.created_at, arr.rhodium_price, arr.palladium_price, arr.platinum_price, arr.currency, date, arr.id, arr.getuId())
                        materialHistoryBox.put(priceDb)
                    }

                    //materialHistoryBox.put(arrayList)

                    /*  for (it in materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().find()) {
                         // Log.d("historyData", it.date.toString())
                      }*/

                    UpdateCurrentPrice(materialHistoryBox)

                }
            }

            override fun onError(`object`: Any?) {

            }
        })
    }

    private fun UpdateCurrentPrice(materialHistoryBox: Box<MaterialHistoryModelDataBase>?) {

        drawGraph()
        var date: Date? = materialHistoryBox?.query()?.order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING)?.build()?.findFirst()!!.date
        // var platanumCurrentDate: String = dateFormat.format(date)
        var platanumPreviousDate: Date? = getPreviousDate(date)
        prices(date, platanumPreviousDate)
    }

    fun prices(CurrentDate: Date?, PreviousDate: Date?) {

        var platanumCurrentPrice: Double = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, CurrentDate).build().findFirst()?.platinum_price!!.toDouble()
        var platanumPreviousPrice: Double = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, PreviousDate).build().findFirst()?.platinum_price!!.toDouble()

        var palladiumCurrentPrice: Double = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, CurrentDate).build().findFirst()?.palladium_price!!.toDouble()
        var palladiumPreviousPrice: Double = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, PreviousDate).build().findFirst()?.palladium_price!!.toDouble()

        var rhodiumCurrentPrice: Double = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, CurrentDate).build().findFirst()?.rhodium_price!!.toDouble()
        var rhodiumPreviousPrice: Double = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, PreviousDate).build().findFirst()?.rhodium_price!!.toDouble()

        var platinumDifference = ((platanumCurrentPrice - platanumPreviousPrice)) / platanumPreviousPrice
        var palladiumDifference = ((palladiumCurrentPrice - palladiumPreviousPrice)) / palladiumPreviousPrice
        var rhodiumDifference = ((rhodiumCurrentPrice - rhodiumPreviousPrice)) / rhodiumPreviousPrice

        var platinumEstimatedPrice = platinumDifference * 100
        var palladiumEstimatedPrice = palladiumDifference * 100
        var rhodiumEstimatedPrice = rhodiumDifference * 100

        txtPlatinumPerc.text = String.format("%.2f", platinumEstimatedPrice) + "%"
        txtPalladiumPerc.text = String.format("%.2f", palladiumEstimatedPrice) + "%"
        txtRhodiumPerc.text = String.format("%.2f", rhodiumEstimatedPrice) + "%"
        txtPlatinumPrice.text = "$platanumCurrentPrice $/Oz t"
        txtPalladiumPrice.text = "$palladiumCurrentPrice $/Oz t"
        txtRhodiumPrice.text = "$rhodiumCurrentPrice $/Oz t"

        if (platinumEstimatedPrice < 0) {

            imgPlatinumStatus.setImageResource(R.drawable.img_arrow_down)
            imgPlatinumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.colorAccent))
        } else {

            imgPlatinumStatus.setImageResource(R.drawable.img_arrow_up)
            imgPlatinumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.colorPrimary))
        }
        if (palladiumEstimatedPrice < 0) {

            imgPalladiumStatus.setImageResource(R.drawable.img_arrow_down)
            imgPalladiumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.colorAccent))
        } else {

            imgPalladiumStatus.setImageResource(R.drawable.img_arrow_up)
            imgPalladiumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.colorPrimary))
        }
        if (rhodiumEstimatedPrice < 0) {

            imgRhodiumStatus.setImageResource(R.drawable.img_arrow_down)
            imgRhodiumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.colorAccent))
        } else {

            imgRhodiumStatus.setImageResource(R.drawable.img_arrow_up)
            imgRhodiumStatus.background.setTint(ContextCompat.getColor(context!!, R.color.colorPrimary))
        }

    }

    private fun getPreviousDate(currentDate: Date?): Date? {

        var calendar: Calendar = Calendar.getInstance()
        // var currentDate: Date = dateFormat.parse(platanumCurrentDate)
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        var d: Date = calendar.time

        //var previousDate: String = dateFormat.format(calendar.time)
        var date: String = materialHistoryBox.query().equal(MaterialHistoryModelDataBase_.date, d).build().find().toString()
        if (date.isNullOrEmpty()) {
            getPreviousDate(currentDate)
            return null
        } else {
            return d
        }

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

    private fun setData(count: Int, range: ArrayList<Double>) {
        val values = java.util.ArrayList<Entry>()
        currentPrice.text = range[0].toString()
        for (a in 0 until count) {
            values.add(Entry(a.toFloat(), range[a].toFloat()))
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
                //setData((2 until 40).random(), (2 until 40).random().toFloat())
            }, 2000)
        }

        switchMultiButton.setOnSwitchListener(object: SwitchMultiButton.OnSwitchListener {
            override fun onSwitch(position:Int, tabText:String) {
                arrData.forEach { it.isSelected = false }
                arrData[0].isSelected = true

                daysSelectorAdapter.notifyDataSetChanged()
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

            var range: List<MaterialHistoryModelDataBase> = ArrayList()

            range = materialHistoryBox.query().between(MaterialHistoryModelDataBase_.date, currentDate, prevDate).build().find()


            Log.d("range", range.toString())
            var priceList: ArrayList<Double> = ArrayList()
            priceList.clear()
            for (arr in range) {

                if (switchMultiButton.selectedTab == 0) {
                    priceList.add(arr.platinum_price)
                }else if(switchMultiButton.selectedTab == 1){
                    priceList.add(arr.palladium_price)
                } else if(switchMultiButton.selectedTab == 2){
                    priceList.add(arr.rhodium_price)
                }

            }
            setData(priceList.size, priceList)

        } else if (position == 1) {
            var currentDate: Date = materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().findFirst()?.date!!
            var calendar: Calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.MONTH, -1)
            var prevDate = calendar.time


            var range: List<MaterialHistoryModelDataBase> = materialHistoryBox.query().between(MaterialHistoryModelDataBase_.date, currentDate, prevDate).build().find()
            Log.d("range", range.toString())
            var priceList: ArrayList<Double> = ArrayList()
            priceList.clear()

            for (arr in range) {


                if (switchMultiButton.selectedTab == 0) {
                    priceList.add(arr.platinum_price)
                }else if(switchMultiButton.selectedTab == 1){
                    priceList.add(arr.palladium_price)
                } else if(switchMultiButton.selectedTab == 2){
                    priceList.add(arr.rhodium_price)
                }
            }
            setData(priceList.size, priceList)

        } else if (position == 2) {

            var currentDate: Date = materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().findFirst()?.date!!
            var calendar: Calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.MONTH, -6)
            var prevDate = calendar.time


            var range: List<MaterialHistoryModelDataBase> = materialHistoryBox.query().between(MaterialHistoryModelDataBase_.date, currentDate, prevDate).build().find()
            Log.d("range", range.toString())
            var priceList: ArrayList<Double> = ArrayList()
            priceList.clear()

            for (arr in range) {

                if (switchMultiButton.selectedTab == 0) {
                    priceList.add(arr.platinum_price)
                }else if(switchMultiButton.selectedTab == 1){
                    priceList.add(arr.palladium_price)
                } else if(switchMultiButton.selectedTab == 2){
                    priceList.add(arr.rhodium_price)
                }
            }
            setData(priceList.size, priceList)


        } else if (position == 3) {

            var currentDate: Date = materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().findFirst()?.date!!
            var calendar: Calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.YEAR, -1)
            var prevDate = calendar.time


            var range: List<MaterialHistoryModelDataBase> = materialHistoryBox.query().between(MaterialHistoryModelDataBase_.date, currentDate, prevDate).build().find()
            Log.d("range", range.toString())
            var priceList: ArrayList<Double> = ArrayList()
            priceList.clear()

            for (arr in range) {

                if (switchMultiButton.selectedTab == 0) {
                    priceList.add(arr.platinum_price)
                }else if(switchMultiButton.selectedTab == 1){
                    priceList.add(arr.palladium_price)
                } else if(switchMultiButton.selectedTab == 2){
                    priceList.add(arr.rhodium_price)
                }
            }
            setData(priceList.size, priceList)
        } else if (position == 4) {
            var currentDate: Date = materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().findFirst()?.date!!
            var calendar: Calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.YEAR, -5)
            var prevDate = calendar.time


            var range: List<MaterialHistoryModelDataBase> = materialHistoryBox.query().between(MaterialHistoryModelDataBase_.date, currentDate, prevDate).build().find()
            Log.d("range", range.toString())
            var priceList: ArrayList<Double> = ArrayList()
            priceList.clear()

            for (arr in range) {

                if (switchMultiButton.selectedTab == 0) {
                    priceList.add(arr.platinum_price)
                }else if(switchMultiButton.selectedTab == 1){
                    priceList.add(arr.palladium_price)
                } else if(switchMultiButton.selectedTab == 2){
                    priceList.add(arr.rhodium_price)
                }
            }
            setData(priceList.size, priceList)

        }

        daysSelectorAdapter.notifyDataSetChanged()

        // contRefresh.performClick()

    }

    override fun onDestroyView() {
        webCallCollection?.cancel()
        super.onDestroyView()
    }

    /**
     * remove records//////////////////////////////
     */

    fun removeAll(materialHistoryBox: Box<MaterialHistoryModelDataBase>) {
        materialHistoryBox.removeAll()
    }

    /**
     * get size//////////////////////////////
     */

    fun getSize(materialHistoryBox: Box<MaterialHistoryModelDataBase>): Int {
        var user = materialHistoryBox.all
        return user.size
    }

    fun drawGraph(){
        var currentDate: Date = materialHistoryBox.query().order(MaterialHistoryModelDataBase_.date, QueryBuilder.DESCENDING).build().findFirst()?.date!!
        var calendar: Calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        var prevDate = calendar.time

        var range: List<MaterialHistoryModelDataBase> = ArrayList()

        range = materialHistoryBox.query().between(MaterialHistoryModelDataBase_.date, currentDate, prevDate).build().find()


        Log.d("range", range.toString())
        var priceList: ArrayList<Double> = ArrayList()
        priceList.clear()
        for (arr in range) {

            if (switchMultiButton.selectedTab == 0) {
                priceList.add(arr.platinum_price)
            }else if(switchMultiButton.selectedTab == 1){
                priceList.add(arr.palladium_price)
            } else if(switchMultiButton.selectedTab == 2){
                priceList.add(arr.rhodium_price)
            }

        }
        setData(priceList.size, priceList)
    }

}