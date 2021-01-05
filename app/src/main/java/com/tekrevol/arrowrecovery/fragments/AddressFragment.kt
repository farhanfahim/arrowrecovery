package com.tekrevol.arrowrecovery.fragments

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.fragments.abstracts.GenericContentFragment
import com.tekrevol.arrowrecovery.helperclasses.GooglePlaceHelper
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.Country
import com.tekrevol.arrowrecovery.models.SpinnerModel
import com.tekrevol.arrowrecovery.models.States
import com.tekrevol.arrowrecovery.models.receiving_model.Slug
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.searchdialog.SimpleSearchDialogCompat
import com.tekrevol.arrowrecovery.searchdialog.core.SearchResultListener
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_address.*
import retrofit2.Call
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class AddressFragment : BaseFragment() {
    var countryListAdapter: ListAdapter? = null
    private var selectedCountryIndex: Int = -1
    private var spinnerModelArrayList = ArrayList<SpinnerModel>()
    private var spinnerCountryArrayList = ArrayList<SpinnerModel>()
    var webCall: Call<WebResponse<Any>>? = null
    var aboutCall: Call<WebResponse<Any>>? = null

    private var locationClick: Long = 0
    var googlePlaceHelper: GooglePlaceHelper? = null
    override fun getDrawerLockMode(): Int {
        return 0

    }


    companion object {

        var arrData: ArrayList<States> = ArrayList()
        var arrCountryData: ArrayList<Country> = ArrayList()
        fun newInstance(): AddressFragment {
            val args = Bundle()

            val fragment = AddressFragment()
            fragment.arguments = args
            return fragment
        }

    }


    override fun getFragmentLayout(): Int {

        return R.layout.fragment_address

    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {

        contState.setOnClickListener {
            //showProvidersInDialog(arrData)
        }
        contCountry.setOnClickListener {
            //showCountrySelectDialog()
        }


        contTermsAndConditions.setOnClickListener {
            privacyAPI(AppConstants.KEY_TERMS)
        }

        tvAddress.setOnClickListener {

            if (SystemClock.elapsedRealtime() - locationClick < 2000) {
                return@setOnClickListener
            }
            locationClick = SystemClock.elapsedRealtime()

            googlePlaceHelper = GooglePlaceHelper(baseActivity, GooglePlaceHelper.PLACE_PICKER, object : GooglePlaceHelper.GooglePlaceDataInterface {
                override fun onPlaceActivityResult(longitude: Double, latitude: Double, locationName: String?) {
                    tvAddress.text = locationName
                    AppConstants.LAT = latitude
                    AppConstants.LNG = longitude

                    getCountryName(context, latitude, longitude)


                    var str: String = GooglePlaceHelper.getMapSnapshotURL(latitude, longitude)
                    ImageLoaderHelper.loadImageWithAnimations(imgMap, str, false)
                    map.visibility = View.VISIBLE

                }

                override fun onError(error: String?) {}
            }, this@AddressFragment, onCreated)

            googlePlaceHelper!!.openMapsActivity()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (googlePlaceHelper != null) {
            googlePlaceHelper!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getCountry()

    }

    fun showProvidersInDialog(insuranceProvidersList: ArrayList<States>) {
        val dialog = SimpleSearchDialogCompat(context, "Select State",
                "Search here...", null, insuranceProvidersList, SearchResultListener<States> { dialog, item, position ->
            dialog.dismiss()
            txtState.text = item.name
        })

        dialog.show()
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    private fun getCountry() {

        val query: MutableMap<String, Any> = HashMap()
        webCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.Q_PARAM_COUNTRY, query, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : com.google.gson.reflect.TypeToken<ArrayList<Country>>() {

                }.type

                arrCountryData = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result), type)

                spinnerCountryArrayList.clear()

                initCountryAdapter()


            }

            override fun onError(`object`: Any?) {}
        })

    }

    private fun getStates(statId: Int) {

        txtState.text = ""
        val query: MutableMap<String, Any> = HashMap()

        query[WebServiceConstants.Q_PARAM_COUNTRY_ID] = statId
        webCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.Q_PARAM_STATES, query, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : com.google.gson.reflect.TypeToken<ArrayList<States>>() {

                }.type

                arrData = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result), type)

                spinnerModelArrayList.clear()

                for (states in arrData) {
                    spinnerModelArrayList.add(SpinnerModel(states.name))
                }

                if (spinnerModelArrayList.isEmpty()) {
                    contState.visibility = View.GONE
                    Toast.makeText(context, "No State Available", Toast.LENGTH_SHORT).show()
                } else {
                    //contState.visibility = View.VISIBLE
                }
            }

            override fun onError(`object`: Any?) {}
        })

    }

    fun initCountryAdapter() {
        countryListAdapter = object : ArrayAdapter<Country?>(
                context!!,
                R.layout.dialog_item,
                android.R.id.text1,
                arrCountryData as List<Country>
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v: View = super.getView(position, convertView, parent)
                val tv = v.findViewById<TextView>(android.R.id.text1)
                val dp10 =
                        (10 * context.resources.displayMetrics.density + 0.5f).toInt()
                tv.compoundDrawablePadding = dp10
                return v
            }
        }
    }

    private fun showCountrySelectDialog() {
        val builder = android.app.AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
        builder.setTitle("Select Country")
        builder.setSingleChoiceItems(
                countryListAdapter, selectedCountryIndex
        ) { dialog, index ->
            selectedCountryIndex = index

            txtCountry.text = arrCountryData[selectedCountryIndex].name
            txtState.text = ""
            getStates(arrCountryData[selectedCountryIndex].id)
            dialog.dismiss()
        }
        builder.show()
    }

    private fun privacyAPI(slugId: String) {
        val queryMap: Map<String, Any> = HashMap()
        aboutCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_PAGES.toString() + "/" + slugId, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                val pagesModel: Slug = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , Slug::class.java)
                baseActivity.addFragment(GenericContentFragment.newInstance(pagesModel.title, pagesModel.content, true), false)
            }

            override fun onError(`object`: Any?) {}
        })
    }

    override fun onDestroyView() {
        webCall?.cancel()
        aboutCall?.cancel()
        super.onDestroyView()
    }

    fun getCountryName(context: Context?, latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address>? = null
        var addressResult = ""
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            var result: Address
            if (addresses != null && !addresses.isEmpty()) {
                if (addresses[0].countryName != null) {
                    addresses[0].countryName
                    addressResult = addresses[0].countryName

                    for (arr in arrCountryData) {
                        if (addresses[0].countryName == "United States") {
                            txtCountry.text = arrCountryData[0].name
                            getStates(arrCountryData[0].id)
                            for (arrState in arrData) {
                                if (addresses[0].adminArea == arrState.name) {
                                    contState.visibility = View.VISIBLE
                                    txtState.text = arrState.name
                                }
                            }
                            //txtState.text = addresses[0].adminArea
                            return ""

                        } else if (addresses[0].countryName == "Canada") {
                            txtCountry.text = arrCountryData[1].name
                            txtState.text = ""
                            getStates(arrCountryData[1].id)
                            for (arrState in arrData) {
                                if (addresses[0].adminArea == arrState.name) {
                                    contState.visibility = View.VISIBLE
                                    txtState.text = arrState.name
                                }
                            }
                            return ""

                        } else if (addresses[0].countryName == "Mexico") {
                            txtCountry.text = arrCountryData[2].name
                            txtState.text = ""
                            getStates(arrCountryData[2].id)
                            for (arrState in arrData) {
                                if (addresses[0].adminArea == arrState.name) {
                                    contState.visibility = View.VISIBLE
                                    txtState.text = arrState.name
                                }
                            }
                            return ""
                        } else {
                            Toast.makeText(context, "Wrong country", Toast.LENGTH_SHORT).show()
                            return ""
                        }
                    }
                } else {
                    return ""
                }

            } else {
                addressResult = ""

            }
        } catch (ignored: IOException) {
            //do something
        }
        return addressResult
    }


}