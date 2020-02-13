package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.helperclasses.validator.PasswordValidation
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.sending_model.ChangePasswordSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_change_password.*
import retrofit2.Call

class ChangePasswordFragment : BaseFragment() {

    var webCall: Call<WebResponse<Any>>? = null

    companion object {

        fun newInstance(): ChangePasswordFragment {

            val args = Bundle()

            val fragment = ChangePasswordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_change_password
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edtNewPass.addValidator(PasswordValidation())
        edtConfirmPass.addValidator(PasswordValidation(edtNewPass))
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.resetViews()
        titleBar.visibility = View.VISIBLE
        titleBar.hide()
        titleBar.showBackButton(activity)
        titleBar.setTitle("Change Password")

    }

    override fun setListeners() {

        txtChangePass.setOnClickListener(View.OnClickListener {
            changePassApi()

        })

    }

    private fun changePassApi() {


        if (!edtCurrentPass.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter valid current password")
            return
        }
        if (!edtNewPass.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter valid new password")
            return
        }
        if (!edtConfirmPass.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter valid confirm password")
            return
        }

        var changePasswordSendingModel = ChangePasswordSendingModel()
        changePasswordSendingModel.authorization = token
        changePasswordSendingModel.currentPassword = edtCurrentPass.stringTrimmed
        changePasswordSendingModel.password = edtNewPass.stringTrimmed
        changePasswordSendingModel.passwordConfirmation = edtConfirmPass.stringTrimmed

        webCall = getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_CHANGE_PASSWORD, changePasswordSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any>) {
                UIHelper.showToast(context, webResponse.message)
                activity?.supportFragmentManager?.popBackStack()
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

