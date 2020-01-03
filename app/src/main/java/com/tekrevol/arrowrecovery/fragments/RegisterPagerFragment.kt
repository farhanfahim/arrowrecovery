package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.RegisterPagerAdapter
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.helperclasses.validator.PasswordValidation
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.sending_model.SignupSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.UserModelWrapper
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_contact.*
import kotlinx.android.synthetic.main.fragment_personal.*
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call

private var adapter: RegisterPagerAdapter? = null


class RegisterPagerFragment : BaseFragment() {

    var webCall: Call<WebResponse<Any>>? = null

    var positionToSelect: Int = 0


    companion object {
        fun newInstance(): Fragment {
            val args = Bundle()
            val fragment = RegisterPagerFragment()
            fragment.positionToSelect = 0
            fragment.arguments = args
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (onCreated) {
            setViewPagerAdapter()
        } else {
            adapter = RegisterPagerAdapter(childFragmentManager)
            setViewPagerAdapter()
        }

    }

    override fun getDrawerLockMode(): Int {
        return 0
    }


    override fun getFragmentLayout(): Int {
        return R.layout.fragment_register
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.visibility = View.GONE

    }

    override fun setListeners() {

        txt_login.setOnClickListener {
            baseActivity.popBackStack()
            baseActivity.addDockableFragment(LoginFragmentt.newInstance(), true)
        }

        btnnext.setOnClickListener {
            if (positionToSelect < 3) {
                if (positionToSelect.equals(0)) {
                    accountDetails(positionToSelect)
                } else if (positionToSelect.equals(1)) {
                    personalDetails(positionToSelect)
                } else if (positionToSelect.equals(2)) {
                    contactDetails(positionToSelect)
                }
            } else {
                signUpApi()
            }
        }

        btnBack.setOnClickListener {
            setCurrentItemByPosition(positionToSelect - 1)
        }

    }

    private fun signUpApi() {


        var signupSendingModel = SignupSendingModel()
        signupSendingModel.setDeviceToken("abc")
        signupSendingModel.setDeviceType(AppConstants.DEVICE_OS_ANDROID)
        signupSendingModel.setEmail(inputEmail.getStringTrimmed())
        signupSendingModel.setName(inputFirstname.getStringTrimmed())
        signupSendingModel.setPassword(inputPasswordReg.getStringTrimmed())
        signupSendingModel.setPasswordConfirmation(inputConfirmPassReg.getStringTrimmed())

        webCall = getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_REGISTER, signupSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                UIHelper.showToast(context, webResponse.message)
                val userModelWrapper: UserModelWrapper = gson.fromJson(gson.toJson(webResponse.result), UserModelWrapper::class.java)
                sharedPreferenceManager.putObject(AppConstants.KEY_CURRENT_USER_MODEL, userModelWrapper.getUser());
                sharedPreferenceManager.putValue(AppConstants.KEY_CURRENT_USER_ID, userModelWrapper.getUser().getId());
                sharedPreferenceManager.putValue(AppConstants.KEY_TOKEN, userModelWrapper.getUser().getAccessToken());

                baseActivity.addDockableFragment(OptVerification.newInstance(), true)

            }

            override fun onError(`object`: Any?) {}
        })

    }

    private fun contactDetails(positionToSelect: Int) {

        if (!inputEmail.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter your email")
            return
        }

        if (!inputPhoneNo.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter your phoneno")
            return
        }
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage("Is " + inputPhoneNo.stringTrimmed + " your valid phone no? ")
                .setTitle("Alert")
                .setCancelable(true)
                .setNegativeButton("No"
                ) { dialog, id -> dialog.cancel() }
                .setPositiveButton("Yes") { dialog, id -> setCurrentItemByPosition(positionToSelect + 1) }
        val alert = builder.create()
        alert.show()
    }

    private fun personalDetails(positionToSelect: Int) {

        if (txtTitle.getStringTrimmed().isEmpty()) {
            UIHelper.showAlertDialog(context, "Please select title")
            return
        }

        if (!inputFirstname.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter first name")
            return
        }
        if (!inputLastname.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter last name")
            return
        }
        setCurrentItemByPosition(positionToSelect + 1)

    }

    private fun accountDetails(positionToSelect: Int) {

        if (!inputUsername.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter username")
            return
        }

        inputPasswordReg.addValidator(PasswordValidation())
        inputConfirmPassReg.addValidator(PasswordValidation(inputPasswordReg))

        if (!inputPasswordReg.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter valid password")
            return
        }

        if (!inputConfirmPassReg.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter valid confirm password")
            return
        }


        setCurrentItemByPosition(positionToSelect + 1)

    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    private fun setViewPagerAdapter() {

        viewpager.adapter = adapter
        viewpager.setPagingEnabled(false)
        viewpager.offscreenPageLimit = 4
        setCurrentItemByPosition(positionToSelect)

        //        tabs.setupWithViewPager(viewpager);
    }

    fun setCurrentItemByPosition(position: Int) {
        positionToSelect = position
        resetStates()
        setStates()
        viewpager.setCurrentItem(position, true)

        when (positionToSelect) {
            0 -> {
                title.text = "Account Information"
                btnnext.text = "Next"
                btnBack.visibility = View.GONE
                socialloginLayout.visibility = View.VISIBLE
            }
            1 -> {
                socialloginLayout.visibility = View.GONE
                title.text = "Personal Information"
                btnnext.text = "Next"
                btnBack.visibility = View.VISIBLE
            }
            2 -> {
                title.text = "Contact Information"
                btnnext.text = "Next"
                btnBack.visibility = View.VISIBLE
                socialloginLayout.visibility = View.GONE
            }

            3 -> {
                title.text = "Address"
                btnnext.text = "Sign Up"
                btnBack.visibility = View.VISIBLE
                socialloginLayout.visibility = View.GONE


            }
        }
    }


    private fun resetStates() {
        viewPersonal.setBackgroundColor(resources.getColor(R.color.star_grey))
        imgPersonal.setImageResource(R.drawable.personallogogrey)
        txtPersonal.setTextColor(resources.getColor(R.color.star_grey))

        viewContact.setBackgroundColor(resources.getColor(R.color.star_grey))
        imgContact.setImageResource(R.drawable.contactlogogrey)
        txtContact.setTextColor(resources.getColor(R.color.star_grey))

        viewAddress.setBackgroundColor(resources.getColor(R.color.star_grey))
        imgAddress.setImageResource(R.drawable.addressgrey)
        txtAddress.setTextColor(resources.getColor(R.color.star_grey))
    }

    private fun setStates() {
        when (positionToSelect) {
            1 -> {
                viewPersonal.setBackgroundColor(resources.getColor(R.color.green_bg))
                imgPersonal.setImageResource(R.drawable.personalcoloredlogo)
                txtPersonal.setTextColor(resources.getColor(R.color.c_black))
            }
            2 -> {
                viewPersonal.setBackgroundColor(resources.getColor(R.color.green_bg))
                imgPersonal.setImageResource(R.drawable.personalcoloredlogo)
                txtPersonal.setTextColor(resources.getColor(R.color.c_black))

                viewContact.setBackgroundColor(resources.getColor(R.color.green_bg))
                imgContact.setImageResource(R.drawable.contact)
                txtContact.setTextColor(resources.getColor(R.color.c_black))
            }
            3 -> {
                viewPersonal.setBackgroundColor(resources.getColor(R.color.green_bg))
                imgPersonal.setImageResource(R.drawable.personalcoloredlogo)
                txtPersonal.setTextColor(resources.getColor(R.color.c_black))

                viewContact.setBackgroundColor(resources.getColor(R.color.green_bg))
                imgContact.setImageResource(R.drawable.contact)
                txtContact.setTextColor(resources.getColor(R.color.c_black))

                viewAddress.setBackgroundColor(resources.getColor(R.color.green_bg))
                imgAddress.setImageResource(R.drawable.addresscolered)
                txtAddress.setTextColor(resources.getColor(R.color.c_black))
            }
        }
    }


}


