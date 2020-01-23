package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import retrofit2.Call
import java.util.*

class ForgotFragment : BaseFragment() {

    var webCall: Call<WebResponse<Any>>? = null

    companion object {

        fun newInstance(): ForgotFragment {

            val args = Bundle()

            val fragment = ForgotFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_forgot_password
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.resetViews()
        titleBar.visibility = View.VISIBLE
        titleBar.hide()
        titleBar.showBackButton(activity)

        titleBar.setTitle("Forgot Password")

    }

    override fun setListeners() {

        txtForgotPass.setOnClickListener(View.OnClickListener {

            baseActivity.addDockableFragment(VerifyFragment.newInstance(), true)
        })

        txtForgotPass.setOnClickListener(View.OnClickListener {
            forgotPasswordAPI()

        })

    }

    private fun forgotPasswordAPI() {

        if (!edtEmail.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter valid email address")
            return
        }

        sharedPreferenceManager.putValue(AppConstants.KEY_CURRENT_USER_EMAIL, edtEmail.getStringTrimmed())
        // key , fileTypeValue
        // key , fileTypeValue
        val query: MutableMap<String, Any> = HashMap()
        query[WebServiceConstants.Q_PARAM_EMAIL] = edtEmail.getStringTrimmed()

        webCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_FORGET_PASSWORD, query, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                UIHelper.showToast(context, webResponse.message)
                baseActivity.addDockableFragment(VerifyFragment.newInstance(), true)
            }

            override fun onError(`object`: Any?) {}
        })

    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }


    override fun onDestroyView() {
        webCall?.cancel()
        super.onDestroyView()
    }
}

