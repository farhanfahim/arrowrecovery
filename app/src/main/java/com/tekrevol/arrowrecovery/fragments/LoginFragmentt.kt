package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.activities.HomeActivity
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.enums.FragmentName
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.sending_model.LoginSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.UserModelWrapper
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.Call


class LoginFragmentt : BaseFragment() {

    var webCall: Call<WebResponse<Any>>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

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

        txtLogin.setOnClickListener(View.OnClickListener {
            loginUpAPI()
        })

        txtForgot.setOnClickListener(View.OnClickListener {
            baseActivity.addDockableFragment(ForgotFragment.newInstance(), true)
        })

        btnfbLogin.setOnClickListener(View.OnClickListener {
            loginFacebookAPI()
        })

        btngoogleLogin.setOnClickListener(View.OnClickListener {
            loginGoogleAPI()
        })

        txt_signup.setOnClickListener(View.OnClickListener {
            baseActivity.popBackStack()
            baseActivity.addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.SimpleLogin,0), true)
        })

    }

    private fun loginFacebookAPI() {
        mainActivity.fbSignIn()
    }

    private fun loginGoogleAPI() {
        mainActivity.googleSignIn()
    }


    private fun loginUpAPI() {

        if (!inputEmail.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter valid email address")
            return
        }
        if (!inputPassword.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter valid password")
            return
        }

        if (inputEmail.testValidity() && inputPassword.testValidity()) {
            var loginSendingModel = LoginSendingModel()
            loginSendingModel.setEmail(inputEmail.getStringTrimmed())
            loginSendingModel.setDeviceToken("abc")
            loginSendingModel.setDeviceType(AppConstants.DEVICE_OS_ANDROID)
            loginSendingModel.setPassword(inputPassword.getStringTrimmed())
            webCall = getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_LOGIN, loginSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
                override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                    UIHelper.showToast(context, webResponse.message)
                    /* if(isChecked == true)
                    {
                        saveUserInfo();
                    } else {
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }
*/
                    var userModelWrapper: UserModelWrapper = getGson().fromJson(getGson().toJson(webResponse.result), UserModelWrapper::class.java)

                    sharedPreferenceManager.putObject(AppConstants.KEY_CURRENT_USER_MODEL, userModelWrapper.getUser());
                    sharedPreferenceManager.putValue(AppConstants.KEY_CURRENT_USER_ID, userModelWrapper.getUser().getId());
                    sharedPreferenceManager.putValue(AppConstants.KEY_TOKEN, userModelWrapper.getUser().getAccessToken());

                    if ((sharedPreferenceManager?.currentUser?.userDetails?.isCompleted)!!.equals(0)) {
                        baseActivity.popBackStack()
                        baseActivity.addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.RegistrationRequired,1), true)
                    } else if ((sharedPreferenceManager?.currentUser?.userDetails?.isVerified)!!.equals(0)) {
                        baseActivity.popBackStack()
                        baseActivity.addDockableFragment(OptVerification.newInstance(), true)
                    } else {
                        baseActivity.finish()
                        baseActivity.openActivity(HomeActivity::class.java)
                    }
                }

                override fun onError(`object`: Any?) {}
            })
        }
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {

            val args = Bundle()

            val fragment = LoginFragmentt()
            fragment.arguments = args
            return fragment
        }
    }
}