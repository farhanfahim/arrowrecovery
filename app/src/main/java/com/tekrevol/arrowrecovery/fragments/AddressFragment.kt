package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.common.reflect.TypeToken
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.fragments.abstracts.GenericContentFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.IntWrapper
import com.tekrevol.arrowrecovery.models.SpinnerModel
import com.tekrevol.arrowrecovery.models.States
import com.tekrevol.arrowrecovery.models.receiving_model.Slug
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_address.*
import retrofit2.Call
import java.util.ArrayList
import java.util.HashMap

public class AddressFragment : BaseFragment() {

    private var spinnerModelArrayList = ArrayList<SpinnerModel>()

    var webCall: Call<WebResponse<Any>>? = null
    var aboutCall: Call<WebResponse<Any>>? = null

    override fun getDrawerLockMode(): Int {
        return 0

    }


    companion object {

        var arrData: ArrayList<States> = ArrayList()

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
            UIHelper.showSpinnerDialog(this@AddressFragment, spinnerModelArrayList, "Select Category", txtState, null, null, IntWrapper(0))
        }

        contTermsAndConditions.setOnClickListener {
            privacyAPI(AppConstants.KEY_TERMS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getStates()

    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    private fun getStates() {

        val query: MutableMap<String, Any> = HashMap()
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

    private fun privacyAPI(slugId: String) {
        val queryMap: Map<String, Any> = HashMap()
        aboutCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_PAGES.toString() + "/" + slugId, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                val pagesModel: Slug = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , Slug::class.java)
                baseActivity.addDockableFragment(GenericContentFragment.newInstance(pagesModel.getTitle(), pagesModel.getContent(), true), false)
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