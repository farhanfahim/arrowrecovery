package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.common.reflect.TypeToken
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.IntWrapper
import com.tekrevol.arrowrecovery.models.SpinnerModel
import com.tekrevol.arrowrecovery.models.States
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_address.*
import kotlinx.android.synthetic.main.fragment_editprofile.*
import retrofit2.Call
import java.util.ArrayList
import java.util.HashMap

public class AddressFragment : BaseFragment() {
    private var selectedPosition: Int = 0


    private var spinnerModelArrayList = ArrayList<SpinnerModel>()


    var webCall: Call<WebResponse<Any>>? = null

    private var arrData: ArrayList<States> = ArrayList<States>()
    private var listOfStates = ArrayList<States>()


    override fun getDrawerLockMode(): Int {
        return 0

    }

    companion object {
        fun newInstance(): AddressFragment {
            val args = Bundle()

            val fragment = AddressFragment()
            fragment.setArguments(args)
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
            UIHelper.showSpinnerDialog(this@AddressFragment, spinnerModelArrayList, "Selected Category", txtState, null, null, IntWrapper(0))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //arrData = ArrayList<States>()

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
                UIHelper.showToast(context, webResponse.message)


                val type = object : com.google.gson.reflect.TypeToken<ArrayList<States>>() {

                }.type

                arrData = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result), type)

                
                spinnerModelArrayList.clear()

                for (categories in arrData) {
                    spinnerModelArrayList.add(SpinnerModel(categories.name))
                }
            }

            override fun onError(`object`: Any?) {}
        })

    }

}