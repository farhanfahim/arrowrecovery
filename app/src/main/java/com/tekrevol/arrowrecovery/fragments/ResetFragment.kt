package com.tekrevol.arrowrecovery.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants.PATH_RESET_PASSWORD
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.sending_model.ResetPasswordSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_reset.*
import retrofit2.Call

class ResetFragment : BaseFragment() {

    var webCall: Call<WebResponse<Any>>? = null

    override fun getDrawerLockMode(): Int {

        return 0;
    }

    companion object {

        fun newInstance(): ResetFragment {

            val args = Bundle()

            val fragment = ResetFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun getFragmentLayout(): Int {

        return R.layout.fragment_reset
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.resetViews()
        titleBar.visibility = View.VISIBLE
        titleBar.hide()
        titleBar.showBackButton(activity)
        titleBar.setTitle("Reset Password")

    }

    override fun setListeners() {

        txtResetPass.setOnClickListener(View.OnClickListener {

            resetpasswordAPI()
        })
    }

    private fun resetpasswordAPI() {

        if (!edtNewPass.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter valid new password")
            return
        }
        if (!edtConfirmPass.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter valid confirm password")
            return
        }

        val txtEmail: String
        val code: String
        txtEmail = sharedPreferenceManager.getString(AppConstants.KEY_CURRENT_USER_EMAIL)

        code = sharedPreferenceManager.getString(AppConstants.KEY_CODE)

        val resetPasswordSendingModel = ResetPasswordSendingModel()
        resetPasswordSendingModel.setPassword(edtNewPass.getStringTrimmed())
        resetPasswordSendingModel.setEmail(txtEmail)
        resetPasswordSendingModel.setVerificationCode(code)
        resetPasswordSendingModel.setPassword(edtConfirmPass.getStringTrimmed())

        webCall = getBaseWebServices(true).postAPIAnyObject(PATH_RESET_PASSWORD, resetPasswordSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                UIHelper.showAlertDialogWithCallback(webResponse.message, "Reset Password", DialogInterface.OnClickListener { dialog, which -> baseActivity.popStackTill(LoginFragment::class.java.simpleName) }, context)
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
