package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.enums.FragmentName
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.fragments.abstracts.GenericContentFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.receiving_model.Slug
import com.tekrevol.arrowrecovery.models.sending_model.LoginSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.UserModelWrapper
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.edtEmail
import retrofit2.Call
import java.util.HashMap


class LoginFragment : BaseFragment() {

    var webCall: Call<WebResponse<Any>>? = null
    var aboutCall: Call<WebResponse<Any>>? = null


    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_login
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.visibility = View.GONE

    }

    override fun setListeners() {

        txtLogin.setOnClickListener {
            loginUpAPI()
        }

        txtForgot.setOnClickListener {
            baseActivity.addDockableFragment(ForgotFragment.newInstance(), true)
        }

        btnfbLogin.setOnClickListener { loginFacebookAPI() }

        btngoogleLogin.setOnClickListener {
            loginGoogleAPI()
        }

        txt_signup.setOnClickListener {
            baseActivity.popBackStack()
            baseActivity.addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.SimpleLogin, "", 0), true)
        }
        contTermsAndConditions.setOnClickListener {
            privacyAPI(AppConstants.KEY_TERMS)
        }

    }

    private fun privacyAPI(slugId: String) {
        val queryMap: Map<String, Any> = HashMap()
        aboutCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_PAGES.toString() + "/" +  slugId, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                val pagesModel: Slug = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , Slug::class.java)
                baseActivity.addDockableFragment(GenericContentFragment.newInstance(pagesModel.getTitle(), pagesModel.getContent(), true), false)
            }

            override fun onError(`object`: Any?) {}
        })

    }

    private fun loginFacebookAPI() {
        mainActivity.fbSignIn()
    }

    private fun loginGoogleAPI() {
        mainActivity.googleSignIn()
    }


    private fun loginUpAPI() {

        if (!edtEmail.testValidity()) {
            UIHelper.showAlertDialog(context, getString(R.string.email_validation))
            return
        }
        if (!edtPassword.testValidity()) {
            UIHelper.showAlertDialog(context, getString(R.string.password_validation))
            return
        }
        if (!checked.isChecked) {
            UIHelper.showAlertDialog(context, "Please accept Term of Use")
            return
        }

        val loginSendingModel = LoginSendingModel()
        loginSendingModel.deviceToken = sharedPreferenceManager!!.getString(AppConstants.KEY_FIREBASE_TOKEN)
        loginSendingModel.email = edtEmail.stringTrimmed
        loginSendingModel.deviceType = AppConstants.DEVICE_OS_ANDROID
        loginSendingModel.password = edtPassword.stringTrimmed
        val email: String = edtEmail.stringTrimmed
        //var phone:String = inputPhoneNo.stringTrimmed
        webCall = getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_LOGIN, loginSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                UIHelper.showToast(context, webResponse.message)

                val userModelWrapper: UserModelWrapper = gson.fromJson(gson.toJson(webResponse.result), UserModelWrapper::class.java)
                /*sharedPreferenceManager?.putObject(AppConstants.KEY_CURRENT_USER_MODEL, userModelWrapper.user)
            sharedPreferenceManager?.putValue(AppConstants.KEY_CURRENT_USER_ID, userModelWrapper.user.id)
            sharedPreferenceManager?.putValue(AppConstants.KEY_TOKEN, userModelWrapper.user.accessToken)*/

                when {

                    (userModelWrapper.user.userDetails.isCompleted == 0) -> {
                        sharedPreferenceManager?.putValue(AppConstants.KEY_TOKEN, userModelWrapper.user.accessToken)

                        baseActivity.popBackStack()
                        baseActivity.addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.RegistrationRequired, email, 1), true)
                    }
                    (userModelWrapper.user.userDetails.isVerified) == 0 -> {
                        sharedPreferenceManager?.putValue(AppConstants.KEY_TOKEN, userModelWrapper.user.accessToken)

                        baseActivity.popBackStack()
                        baseActivity.addDockableFragment(OtpVerificationFragment.newInstance(email, ""), true)
                    }
                    (userModelWrapper.user.userDetails.isApproved) == 0 -> {
                        sharedPreferenceManager?.putValue(AppConstants.KEY_TOKEN, userModelWrapper.user.accessToken)

                        baseActivity.popBackStack()
                        baseActivity.addDockableFragment(ThankyouFragment.newInstance(), true)
                    }
                    else -> {
                        sharedPreferenceManager?.putObject(AppConstants.KEY_CURRENT_USER_MODEL, userModelWrapper.user)
                        sharedPreferenceManager?.putValue(AppConstants.KEY_CURRENT_USER_ID, userModelWrapper.user.id)
                        sharedPreferenceManager?.putValue(AppConstants.KEY_TOKEN, userModelWrapper.user.accessToken)
                        baseActivity.popBackStack()
                        baseActivity.addDockableFragment(TwoFactorVerification.newInstance(), true)
                    }
                }


            }

            override fun onError(`object`: Any?) {}
        })

    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {

            val args = Bundle()

            val fragment = LoginFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        webCall?.cancel()
        aboutCall?.cancel()
        super.onDestroyView()
    }
}