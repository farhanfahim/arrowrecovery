package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.SearchFragment.Companion.makeId
import com.tekrevol.arrowrecovery.fragments.SearchFragment.Companion.modelId
import com.tekrevol.arrowrecovery.fragments.SearchFragment.Companion.serialNumber
import com.tekrevol.arrowrecovery.fragments.SearchFragment.Companion.year
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.IntWrapper
import com.tekrevol.arrowrecovery.models.SpinnerModel
import com.tekrevol.arrowrecovery.models.receiving_model.VehicleMakeModel
import com.tekrevol.arrowrecovery.models.receiving_model.VehicleModels
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_advanced_search.*
import retrofit2.Call
import java.util.*


class AdvanceSearchFragment : BaseFragment() {

    private val spinnerModelArrayList: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()
    private val spinnerModelArrayList1: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()
    private val spinnerModelArrayList2: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()

    var webCallMake: Call<WebResponse<Any>>? = null
    var webCallModel: Call<WebResponse<Any>>? = null

    companion object {
        var arrDataMake: ArrayList<VehicleMakeModel> = ArrayList()
        var arrDataModel: ArrayList<VehicleModels> = ArrayList()
        fun newInstance(): AdvanceSearchFragment {

            val args = Bundle()
            val fragment = AdvanceSearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()
    }

    private fun bindData() {

        getMakeName()
        getModelName()

        for (carMake in Constants.carMakeSelector()) {
            spinnerModelArrayList.add(SpinnerModel(carMake.text))
        }

        var thisYear: Int = Calendar.getInstance().get(Calendar.YEAR)
        for (i in thisYear downTo 1900) {

            spinnerModelArrayList2.add(SpinnerModel(i.toString()))

        }

        for (carModel in Constants.carModelSelector()) {
            spinnerModelArrayList1.add(SpinnerModel(carModel.text))
        }

        serialNumber = edtSerialNumber.toString()
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_advanced_search
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.resetViews()
        titleBar.visibility = View.VISIBLE
        titleBar.hide()
        titleBar.showBackButton(activity)
        titleBar.setTitle("Advanced Search")

    }

    override fun setListeners() {

        btnAdvSearch.setOnClickListener(View.OnClickListener {
            if (txtMake.equals("") && txtModel.equals("")) {
                Toast.makeText(context, "select at least one field", Toast.LENGTH_LONG).show()
            } else {
                baseActivity.popBackStack()
                if (getModelId() == -1) {
                    modelId = ""
                } else {
                    modelId = getModelId().toString()
                }

                if (getMakeId() == -1) {
                    makeId = ""
                } else {
                    makeId = getMakeId().toString()
                }
                year = txtYear.stringTrimmed
                serialNumber = edtSerialNumber.stringTrimmed
            }
        })

        btnClear.setOnClickListener {
            txtYear.text = ""
            txtMake.text = ""
            txtModel.text = ""
            edtSerialNumber.text = null

        }

        contMake.setOnClickListener(View.OnClickListener {
            UIHelper.showSpinnerDialog(this@AdvanceSearchFragment, spinnerModelArrayList, "Selected Make"
                    , txtMake, null, null, IntWrapper(0))

        })
        contYear.setOnClickListener {
            UIHelper.showSpinnerDialog(this@AdvanceSearchFragment, spinnerModelArrayList2, "Selected Make"
                    , txtYear, null, null, IntWrapper(0))

        }

        contModel.setOnClickListener(View.OnClickListener {
            UIHelper.showSpinnerDialog(this@AdvanceSearchFragment, spinnerModelArrayList1, "Selected Make"
                    , txtModel, null, null, IntWrapper(0))

        })


    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }


    private fun getMakeName() {

        val query: MutableMap<String, Any> = HashMap()
        webCallMake = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.Q_VEHICLE_MAKES, query, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : com.google.gson.reflect.TypeToken<ArrayList<VehicleMakeModel>>() {

                }.type

                arrDataMake = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result), type)

                spinnerModelArrayList.clear()

                for (makeModel in arrDataMake) {
                    spinnerModelArrayList.add(SpinnerModel(makeModel.name))
                }
            }

            override fun onError(`object`: Any?) {}
        })

    }

    private fun getModelName() {

        val query: MutableMap<String, Any> = HashMap()
        webCallModel = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.Q_VEHICLE_MODEL, query, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : com.google.gson.reflect.TypeToken<ArrayList<VehicleModels>>() {

                }.type

                arrDataModel = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result), type)

                spinnerModelArrayList1.clear()

                for (modelName in arrDataModel) {
                    spinnerModelArrayList1.add(SpinnerModel(modelName.name))
                }
            }

            override fun onError(`object`: Any?) {}
        })

    }

    private fun getMakeId(): Int {

        for (makeId in arrDataMake) {
            if (makeId.name == txtMake.stringTrimmed) {
                return makeId.id
            }
        }
        return -1
    }

    private fun getModelId(): Int {

        for (modelId in arrDataModel) {
            if (modelId.name == txtModel.stringTrimmed) {
                return modelId.id
            }
        }
        return -1
    }

    override fun onDestroyView() {
        if (webCallMake != null) {
            webCallMake!!.cancel()
        }
        if (webCallModel != null) {
            webCallModel!!.cancel()
        }
        super.onDestroyView()

    }

}

