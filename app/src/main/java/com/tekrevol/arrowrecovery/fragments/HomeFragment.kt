package com.tekrevol.arrowrecovery.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.google.common.reflect.TypeToken
import com.tekrevol.arrowrecovery.BaseApplication
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.DaysSelectorAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants.*
import com.tekrevol.arrowrecovery.enums.BaseURLTypes
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.fragments.dialogs.CheckoutDialogFragment
import com.tekrevol.arrowrecovery.managers.DateManager.getCurrentFormattedDate
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.database.ObjectBoxManager
import com.tekrevol.arrowrecovery.models.receiving_model.CollectionModel
import com.tekrevol.arrowrecovery.models.receiving_model.DataPriceModel
import com.tekrevol.arrowrecovery.models.receiving_model.MaterialHistoryModel
import com.tekrevol.arrowrecovery.models.receiving_model.MaterialHistoryModel_
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import io.objectbox.Box
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
    val materialHistoryBox = boxStore.boxFor(MaterialHistoryModel::class.java)
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
        //materialHistoryBox.removeAll()

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

        //removeAll(materialHistoryBox)

        if (materialHistoryBox.isEmpty) {
            fetchData(getStartingDate(), "2020-02-19")
            Log.d("fetchData", "fetchData")
        } else {
            Updatedata("2020-02-22")
        }

        //getStartAndEndDate()
    }

    private fun Updatedata(date: String) {

        var dateMaterial: String = materialHistoryBox.query().order(MaterialHistoryModel_.date).build().findFirst() .toString()// all[0].date.toString()
        if (date <= dateMaterial) {
        } else if (date > dateMaterial) {
            var myDate: Date = dateFormat.parse(date)
            var callStart: Calendar = Calendar.getInstance()
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
                fetchData(dateMaterial, date)
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
        //priceRodium(startDate, endDate)
        /* pricePlatinum(startDate, endDate)
         pricePalladium(startDate, endDate)*/
    }

    private fun pricePalladium(startDate: String, endDate: String) {

        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_APIKEY] = KEY_PRICE
        queryMap[WebServiceConstants.Q_ENDDATE] = "2020-02-19"
        queryMap[WebServiceConstants.Q_STARTDATE] = startDate


        WebServices(activity, "", BaseURLTypes.PRICE_BASE_URL, true).getAPIPriceAnyObject(WebServiceConstants.PATH_PALL, queryMap,
                object : WebServices.IRequestWebResponseAnyObjectCallBack {
                    override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                        val type = object : TypeToken<java.util.ArrayList<DataPriceModel?>?>() {}.type
                        val arrayList: java.util.ArrayList<DataPriceModel> = GsonFactory.getSimpleGson()
                                .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                        , type)


                    }

                    override fun onError(`object`: Any?) {}
                })
    }

    private fun pricePlatinum(startDate: String, endDate: String) {

        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_APIKEY] = KEY_PRICE
        queryMap[WebServiceConstants.Q_STARTDATE] = startDate
        queryMap[WebServiceConstants.Q_ENDDATE] = endDate


        WebServices(activity, "", BaseURLTypes.PRICE_BASE_URL, true).getAPIPriceAnyObject(WebServiceConstants.PATH_PLAT, queryMap,
                object : WebServices.IRequestWebResponseAnyObjectCallBack {
                    override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                        val type = object : TypeToken<java.util.ArrayList<DataPriceModel?>?>() {}.type
                        val arrayList: java.util.ArrayList<DataPriceModel> = GsonFactory.getSimpleGson()
                                .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                        , type)

                    }

                    override fun onError(`object`: Any?) {}
                })
    }

    private fun priceRodium(startDate: String, endDate: String) {

        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_STARTDATE] = startDate
        queryMap[WebServiceConstants.Q_APIKEY] = KEY_PRICE
        queryMap[WebServiceConstants.Q_ENDDATE] = endDate

        WebServices(activity, "", BaseURLTypes.PRICE_BASE_URL, true).getAPIPriceAnyObject(WebServiceConstants.PATH_RHOD, queryMap,
                object : WebServices.IRequestWebResponseAnyObjectCallBack {
                    override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                        val type = object : TypeToken<java.util.ArrayList<DataPriceModel?>?>() {}.type
                        val arrayList: java.util.ArrayList<DataPriceModel> = GsonFactory.getSimpleGson()
                                .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                        , type)

                        Log.d("price", arrayList.toString())

                    }

                    override fun onError(`object`: Any?) {}
                })


    }

    private fun getStartingDate(): String {
        val prevYear = Calendar.getInstance()
        prevYear.add(Calendar.YEAR, -5)
        var year: String = prevYear.get(Calendar.YEAR).toString()
        var month: String = prevYear.get(Calendar.MONTH).toString()
        var day: String = prevYear.get(Calendar.DATE).toString()
        return "$year-$month-$day"
    }

    private fun getCurrentDate(): String {
        val prevYear = Calendar.getInstance()
        var year: String = prevYear.get(Calendar.YEAR).toString()
        var month: String = prevYear.get(Calendar.MONTH).toString()
        var day: String = prevYear.get(Calendar.DATE).toString()
        return "$year-$month-$day"
    }

    //If current date <= last available data date  then [No API Call because we have updated data]


    //If current date > last available data but and no above cases found true. We will call the data from last available data date till Current date. and append this data in current DB.


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
                    materialHistoryBox.put(arrayList)

                    for (it in materialHistoryBox.query().build().find()) {
                        Log.d("historyData", it.date)
                    }
                }
            }

            override fun onError(`object`: Any?) {

            }
        })
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

    override fun onDestroyView() {
        webCallCollection?.cancel()
        super.onDestroyView()
    }

    /**
     * remove records//////////////////////////////
     */

    fun removeAll(materialHistoryBox: Box<MaterialHistoryModel>) {
        materialHistoryBox.removeAll()
    }

    /**
     * get size//////////////////////////////
     */

    fun getSize(materialHistoryBox: Box<MaterialHistoryModel>): Int {
        materialHistoryBox.removeAll()
        var user = materialHistoryBox.all
        return user.size
    }


}