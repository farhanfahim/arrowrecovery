package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.callbacks.OnMessageReceive
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
import swarajsaaj.smscodereader.interfaces.OTPListener
import swarajsaaj.smscodereader.receivers.OtpReader
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class OtpVerificationFragment : BaseFragment() {
    var webCall: Call<WebResponse<Any>>? = null
    var email: String = ""
    var phone: String = ""
    var webCallVerify: Call<WebResponse<Any>>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtPhone.text = "Please enter the OTP code sent to your phone number $phone"
      //  txtPhone.text = "We have sent you a SMS with a code to the number $phone and your email $email"
        txtBackToLoginScreen.visibility = View.GONE

    }



    private fun sendOtp() {

        var query: HashMap<String, Any> = HashMap<String, Any>()
        webCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_RESENDOTP, query, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                UIHelper.showToast(context, webResponse.message)
            }

            override fun onError(`object`: Any?) {

            }
        })

    }


    companion object {
        fun newInstance(email: String, phone: String): Fragment {

            val args = Bundle()

            val fragment = OtpVerificationFragment()
            fragment.arguments = args
            fragment.email = email
            fragment.phone = phone
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
                        baseActivity.popBackStack()
                        baseActivity.addDockableFragment(ThankyouFragment.newInstance(), true)
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
        KeyboardHelper.hideSoftKeyboard(context, pinEditText)
        super.onDestroyView()
    }

    override fun onPause() {
        super.onPause()
        KeyboardHelper.hideSoftKeyboard(context, pinEditText)
    }



}