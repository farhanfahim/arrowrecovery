package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.SpinnerDialogAdapter
import com.tekrevol.arrowrecovery.callbacks.OnSpinnerItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.fragments.abstracts.GenericContentFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.Country
import com.tekrevol.arrowrecovery.models.IntWrapper
import com.tekrevol.arrowrecovery.models.SpinnerModel
import com.tekrevol.arrowrecovery.models.States
import com.tekrevol.arrowrecovery.models.receiving_model.Slug
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.searchdialog.SimpleSearchDialogCompat
import com.tekrevol.arrowrecovery.searchdialog.core.BaseSearchDialogCompat
import com.tekrevol.arrowrecovery.searchdialog.core.SearchResultListener
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_address.*
import kotlinx.android.synthetic.main.fragment_address.contCountry
import kotlinx.android.synthetic.main.fragment_address.contState
import kotlinx.android.synthetic.main.fragment_address.txtCountry
import kotlinx.android.synthetic.main.fragment_address.txtState
import kotlinx.android.synthetic.main.fragment_editprofile.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList

class AddressFragment : BaseFragment() {
    var countryListAdapter: ListAdapter? = null
    private var selectedCountryIndex: Int = -1
    private var spinnerModelArrayList = ArrayList<SpinnerModel>()
    private var spinnerCountryArrayList = ArrayList<SpinnerModel>()
    var webCall: Call<WebResponse<Any>>? = null
    var aboutCall: Call<WebResponse<Any>>? = null
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
            showProvidersInDialog(arrData)
        }
        contCountry.setOnClickListener {
            showCountrySelectDialog()
        }


        contTermsAndConditions.setOnClickListener {
            privacyAPI(AppConstants.KEY_TERMS)
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
        contState.visibility = View.VISIBLE

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
                baseActivity.addDockableFragment(GenericContentFragment.newInstance(pagesModel.title, pagesModel.content, true), false)
            }

            override fun onError(`object`: Any?) {}
        })
    }

    override fun onDestroyView() {
        webCall?.cancel()
        aboutCall?.cancel()
        super.onDestroyView()
    }



}