package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.activities.HomeActivity
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.KeyboardHelper
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_verify_account.*
import retrofit2.Call
import java.util.*

class OptVerification : BaseFragment() {

    var webCall: Call<WebResponse<Any>>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtPhone.text = "We have sent you a SMS with a code to the number " + sharedPreferenceManager.currentUser.userDetails.phone
        sendOtp()
    }

    private fun sendOtp() {

        // key , fileTypeValue
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

            val fragment = OptVerification()
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

        txtSendCode.setOnClickListener {
            sendOtp()
        }

        pinEditText.setOnPinEnteredListener {
            baseActivity.finish()
            baseActivity.openActivity(HomeActivity::class.java)
        }
    }


    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }
}