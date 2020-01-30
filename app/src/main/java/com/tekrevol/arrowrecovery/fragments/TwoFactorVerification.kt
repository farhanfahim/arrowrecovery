package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.activities.HomeActivity
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.KeyboardHelper
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.sending_model.OtpModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_verify_account.*
import retrofit2.Call
import java.util.*

class TwoFactorVerification : BaseFragment() {

    var webCall: Call<WebResponse<Any>>? = null
    var webCallVerify: Call<WebResponse<Any>>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtPhone.text = "We have sent you a SMS with a code to the number " + sharedPreferenceManager.currentUser.userDetails.phone + " and your email " + sharedPreferenceManager.currentUser.email
        txtBackToLoginScreen.visibility = View.GONE
        sendOtp()
    }

    private fun sendOtp() {

        var query: HashMap<String, Any> = HashMap<String, Any>()
        webCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_RESENDOTP, query, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                UIHelper.showToast(context, webResponse.message)
            }

            override fun onError(`object`: Any?) {}
        })

    }

    companion object {
        fun newInstance(): Fragment {

            val args = Bundle()

            val fragment = TwoFactorVerification()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_verify_account
    }

    override fun setTitlebar(titleBar: TitleBar?) {
        titleBar?.visibility = View.GONE

    }

    override fun onResume() {
        super.onResume()

        KeyboardHelper.showSoftKeyboardForcefully(context, pinEditText)
    }

    override fun setListeners() {


        txtBackToLoginScreen.setOnClickListener(View.OnClickListener {
            baseActivity.popBackStack()
            baseActivity.addDockableFragment(LoginFragment.newInstance(), true)
        })

        txtSendCode.setOnClickListener {
            sendOtp()
        }

        pinEditText.setOnPinEnteredListener {

            var otpModel = OtpModel()
            otpModel.otp = pinEditText.text.toString()

            webCallVerify = getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_VERIFYOTP, otpModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
                override fun requestDataResponse(webResponse: WebResponse<Any>) {
                    UIHelper.showToast(context, webResponse.message)
                    if (webResponse.isSuccess) {
                        sharedPreferenceManager?.putValue(AppConstants.KEY_IS_VERIFIED, "1")
                        Log.d("verify", sharedPreferenceManager.getString(AppConstants.KEY_IS_VERIFIED))

                        baseActivity.finish()
                        baseActivity.openActivity(HomeActivity::class.java)
                    } else {
                        pinEditText.text?.clear()
                    }

                }

                override fun onError(`object`: Any?) {
                    pinEditText.text?.clear()
                }
            })
        }
    }


    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }


    override fun onDestroyView() {
        webCall?.cancel()
        webCallVerify?.cancel()
        super.onDestroyView()
    }
}