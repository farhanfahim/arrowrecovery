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
import com.tekrevol.arrowrecovery.enums.FragmentName
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.helperclasses.validator.PasswordValidation
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.UserDetails
import com.tekrevol.arrowrecovery.models.receiving_model.UserModel
import com.tekrevol.arrowrecovery.models.sending_model.EditProfileSendingModel
import com.tekrevol.arrowrecovery.models.sending_model.SignupSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.UserModelWrapper
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_address.*
import kotlinx.android.synthetic.main.fragment_contact.*
import kotlinx.android.synthetic.main.fragment_personal.*
import kotlinx.android.synthetic.main.fragment_register.*
import retrofit2.Call

private var adapter: RegisterPagerAdapter? = null

class RegisterPagerFragment : BaseFragment() {

    var webCall: Call<WebResponse<Any>>? = null
    var positionToSelect: Int = 0
    lateinit var fragmentName: FragmentName

    companion object {
        fun newInstance(fragmentName: FragmentName, positionToSelect: Int): Fragment {
            val args = Bundle()
            val fragment = RegisterPagerFragment()
            fragment.positionToSelect = positionToSelect
            fragment.fragmentName = fragmentName
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

        if (fragmentName.equals(FragmentName.RegistrationRequired)) {
            setCurrentItemByPosition(positionToSelect)
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

        btnfb.setOnClickListener(View.OnClickListener {
            loginFacebookAPI()
        })

        btngoogle.setOnClickListener(View.OnClickListener {
            loginGoogleAPI()
        })

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
                if (fragmentName.equals(FragmentName.RegistrationRequired)) {
                    updateProfileApi()

                } else {
                    signUpApi()
                }
            }
        }

        btnBack.setOnClickListener {
            if (fragmentName.equals(FragmentName.RegistrationRequired)) {
                if (positionToSelect > 1) {
                    setCurrentItemByPosition(positionToSelect - 1)

                }
            } else {
                setCurrentItemByPosition(positionToSelect - 1)

            }
        }
    }

    private fun updateProfileApi() {

        val editProfileSendingModel = EditProfileSendingModel()
        editProfileSendingModel.email = (sharedPreferenceManager.currentUser.email)
        editProfileSendingModel.phone = (inputPhoneNo.getStringTrimmed())
        editProfileSendingModel.firstName = (inputFirstname.getStringTrimmed())
        editProfileSendingModel.lastName = (inputLastname.getStringTrimmed())
        editProfileSendingModel.address = (inputAddress.getStringTrimmed())
        editProfileSendingModel.zipCode = (inputZipCode.getStringTrimmed())
        editProfileSendingModel.company = (txtCompanyName.getStringTrimmed())
        editProfileSendingModel.name = (inputFirstname.stringTrimmed)
        editProfileSendingModel.stateId = (1)
        editProfileSendingModel.city = (inputCity.getStringTrimmed())


        getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_PROFILE, editProfileSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                val userDetails: UserDetails = gson.fromJson(gson.toJson(webResponse.result), UserDetails::class.java)
//                val userModelWrapper: UserModelWrapper = gson.fromJson(gson.toJson(webResponse.result), UserModelWrapper::class.java)
                val currentUser: UserModel = sharedPreferenceManager.currentUser
                currentUser.setUserDetails(userDetails)
                sharedPreferenceManager.putObject(AppConstants.KEY_CURRENT_USER_MODEL, currentUser)

                if((sharedPreferenceManager?.currentUser?.userDetails?.isCompleted)!!.equals(1))
                {
                    baseActivity.addDockableFragment(OtpVerification.newInstance(), true)

                }
            }

            override fun onError(`object`: Any?) {}
        })

    }

    private fun loginGoogleAPI() {
        mainActivity.googleSignIn()
    }

    private fun loginFacebookAPI() {
        mainActivity.fbSignIn()
    }

    private fun signUpApi() {


        if (!inputAddress.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter your address")
            return

        }

        if (!inputZipCode.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter zipCode")
            return

        }
        if (!inputCity.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter your city")
            return
        }

        var signupSendingModel = SignupSendingModel()
        signupSendingModel.deviceToken = ("abc")
        signupSendingModel.name = (inputUsername.stringTrimmed)
        signupSendingModel.deviceType = (AppConstants.DEVICE_OS_ANDROID)
        signupSendingModel.email = (inputEmail.getStringTrimmed())
        signupSendingModel.phone = (inputPhoneNo.getStringTrimmed())
        signupSendingModel.firstName = (inputFirstname.getStringTrimmed())
        signupSendingModel.lastName = (inputLastname.getStringTrimmed())
        signupSendingModel.address = (inputAddress.getStringTrimmed())
        signupSendingModel.zipCode = (inputZipCode.getStringTrimmed())
        signupSendingModel.company = (txtCompanyName.getStringTrimmed())
        signupSendingModel.stateId = (1)
        signupSendingModel.city = (inputCity.getStringTrimmed())
        signupSendingModel.password = (inputPasswordReg.getStringTrimmed())
        signupSendingModel.passwordConfirmation = (inputConfirmPassReg.getStringTrimmed())
        signupSendingModel.isCompleted = (1)

        webCall = getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_REGISTER, signupSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                UIHelper.showToast(context, webResponse.message)
                val userModelWrapper: UserModelWrapper = gson.fromJson(gson.toJson(webResponse.result), UserModelWrapper::class.java)
                sharedPreferenceManager.putObject(AppConstants.KEY_CURRENT_USER_MODEL, userModelWrapper.getUser())
                sharedPreferenceManager.putValue(AppConstants.KEY_CURRENT_USER_ID, userModelWrapper.getUser().getId())
                sharedPreferenceManager.putValue(AppConstants.KEY_TOKEN, userModelWrapper.getUser().getAccessToken())
                if((sharedPreferenceManager?.currentUser?.userDetails?.isCompleted)!!.equals(1))
                {
                    baseActivity.addDockableFragment(OtpVerification.newInstance(), true)

                }
            }

            override fun onError(`object`: Any?) {}
        })
    }

    private fun contactDetails(positionToSelect: Int) {

        if (fragmentName.equals(FragmentName.SimpleLogin)) {
            if (!inputEmail.testValidity()) {
                UIHelper.showAlertDialog(context, "Please enter your email")
                return
            }
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

        if (fragmentName.equals(FragmentName.RegistrationRequired)) {
            emailLayout.visibility = View.GONE
        }

        if (txtTitle.getStringTrimmed().isEmpty()) {
            UIHelper.showAlertDialog(context, "Please select title")
            return
        }
        if (txtCompanyName.getStringTrimmed().isEmpty()) {
            UIHelper.showAlertDialog(context, "Please  enter your country name")
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
        //tabs.setupWithViewPager(viewpager);
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


