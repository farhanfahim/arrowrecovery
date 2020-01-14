package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.sending_model.ResetPasswordSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_verify.*
import retrofit2.Call
import java.util.*

class VerifyFragment : BaseFragment() {
    var countDownTimer: CountDownTimer? = null
    var webCall: Call<WebResponse<Any>>? = null


    companion object {

        fun newInstance(): VerifyFragment {

            val args = Bundle()

            val fragment = VerifyFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_verify
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.resetViews()
        titleBar.visibility = View.VISIBLE
        titleBar.hide()
        titleBar.showBackButton(activity)
        titleBar.setTitle("Verify Code")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        countDownTimer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timer.text = "Resend verification code in 00: " + millisUntilFinished / 1000
            }

            override fun onFinish() {
                timer.text = "Resend verification code in 00:00"
                resendPassword.visibility = View.VISIBLE
            }
        }
        (countDownTimer as CountDownTimer).start()
    }

    override fun setListeners() {

        resendPassword.setOnClickListener(View.OnClickListener {
            forgotPasswordAPI()
        })

        txtVerify.setOnClickListener(View.OnClickListener {
            verifycodeAPI()
        })

    }

    private fun verifycodeAPI() {
        var resetPasswordSendingModel = ResetPasswordSendingModel()
        resetPasswordSendingModel.verificationCode = inputCode.stringTrimmed

        sharedPreferenceManager.putValue(AppConstants.KEY_CODE, inputCode.stringTrimmed)

        getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_VERIFY_RESET_CODE, resetPasswordSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                UIHelper.showToast(context, webResponse.message)
                baseActivity.addDockableFragment(ResetFragment.newInstance(), true)
            }

            override fun onError(`object`: Any?) {}
        })
    }

    private fun forgotPasswordAPI() {

        val txtEmail: String
        txtEmail = sharedPreferenceManager.getString(AppConstants.KEY_CURRENT_USER_EMAIL)

        val query: MutableMap<String, Any> = HashMap()
        query[WebServiceConstants.Q_PARAM_EMAIL] = txtEmail

        webCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_FORGET_PASSWORD, query, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                UIHelper.showToast(context, webResponse.message)
            }

            override fun onError(`object`: Any?) {}
        })

    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onDestroyView() {
        if (webCall != null) {
            webCall!!.cancel()
        }
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
        super.onDestroyView()
    }

}

