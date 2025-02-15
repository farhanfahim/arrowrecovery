package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.hbb20.CountryCodePicker
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.activities.HomeActivity
import com.tekrevol.arrowrecovery.adapters.RegisterPagerAdapter
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.enums.FragmentName
import com.tekrevol.arrowrecovery.fragments.AddressFragment.Companion.arrData
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.helperclasses.validator.PasswordValidation
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.receiving_model.DataUpdate
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
import java.util.regex.Matcher
import java.util.regex.Pattern


class RegisterPagerFragment : BaseFragment() {

    private lateinit var ccpLoadNumber: CountryCodePicker
    private var adapter: RegisterPagerAdapter? = null
    var email: String = ""
    var webCall: Call<WebResponse<Any>>? = null
    var positionToSelect: Int = 0

    lateinit var fragmentName: FragmentName


    companion object {
        fun newInstance(fragmentName: FragmentName, email: String, positionToSelect: Int): Fragment {
            val args = Bundle()
            val fragment = RegisterPagerFragment()
            fragment.positionToSelect = positionToSelect
            fragment.fragmentName = fragmentName
            fragment.email = email
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
//    private fun registerCarrierEditText() {
//        ccpLoadNumber.registerCarrierNumberEditText(edtPhoneNo)
//        ccpLoadNumber.setPhoneNumberValidityChangeListener { isValidNumber ->
//            isValid = isValidNumber
//        }
//
//        ccpLoadNumber.registerCarrierNumberEditText(edtPhoneNo)
//    }

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
            baseActivity.addDockableFragment(LoginFragment.newInstance(), true)
        }

        btnfb.setOnClickListener {
            loginFacebookAPI()
        }

        btngoogle.setOnClickListener {
            loginGoogleAPI()
        }

        btnnext.setOnClickListener {

            if (positionToSelect < 3) {
                when (positionToSelect) {
                    0 -> accountDetails(positionToSelect)
                    1 -> personalDetails(positionToSelect)
                    2 -> {
                        contactDetails(positionToSelect)

                        /*ccpLoadNumber = ccp
                        registerCarrierEditText()
                        ccp.registerCarrierNumberEditText(edtPhoneNo)
                        ccp.setNumberAutoFormattingEnabled(true)*/
                    }

                }
            } else {
                if (fragmentName == FragmentName.RegistrationRequired) {
                    updateProfileApi()
                } else {
                    signUpApi()
                }
            }
        }

        btnBack.setOnClickListener {
            if (fragmentName == FragmentName.RegistrationRequired) {
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

        if (radioBtnCompany.isChecked) {
            editProfileSendingModel.userType = AppConstants.USER_TYPE_COMPANY
        }
        if (radioBtnIndividual.isChecked) {
            editProfileSendingModel.userType = AppConstants.USER_TYPE_INDIVIDUAL
        }

        if (!edtCountry.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter your country")
            return
        }
        if (!edtCity.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter your city")
            return
        }
        if (!edtState.testValidity()) {
            UIHelper.showAlertDialog(context, "Please select your state")
            return
        }
        if (tvAddress.text.toString().isEmpty()) {
            UIHelper.showAlertDialog(context, "Please enter your address")
            return
        }
        if (!edtZipCode.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter your zip code")
            return

        }
        if (!checked.isChecked) {
            UIHelper.showAlertDialog(context, "Please accept Term of Use")
            return
        }


        editProfileSendingModel.phone = (ccp.fullNumberWithPlus.toString())
        editProfileSendingModel.firstName = (edtFirstname.stringTrimmed)
        editProfileSendingModel.lastName = (edtLastName.stringTrimmed)
        editProfileSendingModel.address = (tvAddress.stringTrimmed)
        editProfileSendingModel.isCompleted = (1)
        editProfileSendingModel.zipCode = (edtZipCode.stringTrimmed)
        editProfileSendingModel.company = (edtCompanyName.stringTrimmed)
        editProfileSendingModel.name = (edtFirstname.stringTrimmed)
        editProfileSendingModel.state = (edtState.stringTrimmed)
        //editProfileSendingModel.kindOfCompany = edtKindCompany.stringTrimmed
        editProfileSendingModel.city = (edtCity.stringTrimmed)
        editProfileSendingModel.country = getCountryFromSpinner()
        //editProfileSendingModel.about = (edtComment.stringTrimmed)

        System.out.println(sharedPreferenceManager!!.getString(AppConstants.KEY_FIREBASE_TOKEN))


     /*   if (txtTitle.text == Constants.title[0]) {
            editProfileSendingModel.title = AppConstants.TITLE_MR
        }
        if (txtTitle.text == Constants.title[1]) {
            editProfileSendingModel.title = AppConstants.TITLE_MISS
        }
        if (txtTitle.text == Constants.title[2]) {
            editProfileSendingModel.title = AppConstants.TITLE_MRS
        }
        if (txtTitle.text == Constants.title[3]) {
            editProfileSendingModel.title = AppConstants.TITLE_MS
        }*/
        //   var email: String = edtEmail.stringTrimmed
        var phone: String = edtPhoneNo.stringTrimmed

        getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_PROFILE, editProfileSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                /*val userDetails: UserDetails = gson.fromJson(gson.toJson(webResponse.result), UserDetails::class.java)
                val userModelWrapper: UserModelWrapper = gson.fromJson(gson.toJson(webResponse.result), UserModelWrapper::class.java)
                val currentUser: UserModel = sharedPreferenceManager.currentUser
                currentUser.userDetails = userDetails*/
                /*    UIHelper.showToast(context, webResponse.message)

                    if (webResponse.message.equals("The email has already been taken.")) {
                        setCurrentItemByPosition(0)
                    }
    */
                val model: DataUpdate = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , DataUpdate::class.java)

                when {

                    (model.details.isCompleted == 0) -> {
                        baseActivity.popBackStack()
                        baseActivity.addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.RegistrationRequired, email, 1), true)
                    }
                    (model.details.isVerified) == 0 -> {
                        baseActivity.popBackStack()
                        baseActivity.addDockableFragment(OtpVerificationFragment.newInstance(email, phone), true)
                    }
                    (model.details.isApproved) == 0 -> {
                        baseActivity.popBackStack()
                        baseActivity.addDockableFragment(ThankyouFragment.newInstance(), true)
                    }
                    else -> {
                        /* sharedPreferenceManager?.putObject(AppConstants.KEY_CURRENT_USER_MODEL, userModelWrapper.user)
                         sharedPreferenceManager?.putValue(AppConstants.KEY_CURRENT_USER_ID, userModelWrapper.user.id)
                         sharedPreferenceManager?.putValue(AppConstants.KEY_TOKEN, userModelWrapper.user.accessToken)*/

                        baseActivity.finish()
                        baseActivity.openActivity(HomeActivity::class.java)
                    }
                }
            }

            override fun onError(`object`: Any?) {

                var web: WebResponse<Any?> = `object` as WebResponse<Any?>

                if (web.message.contains("email")) {
                    setCurrentItemByPosition(0)
                }
            }
        })

    }

    private fun loginGoogleAPI() {
        mainActivity.googleSignIn()
    }

    private fun loginFacebookAPI() {
        mainActivity.fbSignIn()
    }

    private fun signUpApi() {

        sharedPreferenceManager.putValue(AppConstants.KEY_TOKEN,"");

        if (!edtCountry.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter your country")
            return
        }

        if (!edtState.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter your state")
            return
        }

        if (!edtCity.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter your city")
            return
        }

        if (tvAddress.text.toString().isEmpty()) {
            UIHelper.showAlertDialog(context, "Please enter your address")
            return
        }

        if (!edtZipCode.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter your zip code")
            return

        }

        if (!checked.isChecked) {
            UIHelper.showAlertDialog(context, "Please accept term of use")
            return
        }

        var signUpSendingModel = SignupSendingModel()
        signUpSendingModel.deviceToken = sharedPreferenceManager!!.getString(AppConstants.KEY_FIREBASE_TOKEN)
        signUpSendingModel.name = (edtFirstname.stringTrimmed) + " " + (edtLastName.stringTrimmed)
        signUpSendingModel.deviceType = (AppConstants.DEVICE_OS_ANDROID)
        signUpSendingModel.email = (edtEmail.stringTrimmed)
        var email: String = edtEmail.stringTrimmed
        var phone: String = edtPhoneNo.stringTrimmed
        signUpSendingModel.phone = (ccp.fullNumberWithPlus.toString())
        signUpSendingModel.firstName = (edtFirstname.stringTrimmed)
        signUpSendingModel.lastName = (edtLastName.stringTrimmed)
        signUpSendingModel.address = (tvAddress.stringTrimmed)
        signUpSendingModel.zipCode = (edtZipCode.stringTrimmed)
        signUpSendingModel.company = (edtCompanyName.stringTrimmed)
        signUpSendingModel.state = (edtState.stringTrimmed)
        signUpSendingModel.city = (edtCity.stringTrimmed)
        //signUpSendingModel.about = (edtComment.stringTrimmed)
        signUpSendingModel.country = (edtCountry.stringTrimmed)
        signUpSendingModel.password = (edtPasswordReg.stringTrimmed)
        signUpSendingModel.passwordConfirmation = (edtConfirmPassReg.stringTrimmed)
        signUpSendingModel.latitude = AppConstants.LAT
        signUpSendingModel.longitude = AppConstants.LNG
        //signUpSendingModel.kindOfCompany = edtKindCompany.stringTrimmed
        signUpSendingModel.isCompleted = (1)

        if (radioBtnCompany.isChecked) {
            signUpSendingModel.userType = AppConstants.USER_TYPE_COMPANY
        }
        if (radioBtnIndividual.isChecked) {
            signUpSendingModel.userType = AppConstants.USER_TYPE_INDIVIDUAL
        }

      /*  if (txtTitle.text == Constants.title[0]) {
            signUpSendingModel.title = AppConstants.TITLE_MR
        }
        if (txtTitle.text == Constants.title[1]) {
            signUpSendingModel.title = AppConstants.TITLE_MISS
        }
        if (txtTitle.text == Constants.title[2]) {
            signUpSendingModel.title = AppConstants.TITLE_MRS
        }
        if (txtTitle.text == Constants.title[3]) {
            signUpSendingModel.title = AppConstants.TITLE_MS
        }*/

        webCall = getBaseWebServices(true).postAPIAnyObject(WebServiceConstants.PATH_REGISTER, signUpSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                UIHelper.showToast(context, webResponse.message)
//                if (webResponse.message.equals("The email has already been taken.")) {
//                    setCurrentItemByPosition(0)
//                }


                val userModelWrapper: UserModelWrapper = gson.fromJson(gson.toJson(webResponse.result), UserModelWrapper::class.java)
                when {

                    (userModelWrapper.user.userDetails.isCompleted == 0) -> {
                        sharedPreferenceManager?.putValue(AppConstants.KEY_TOKEN, userModelWrapper.user.accessToken)

                        baseActivity.popBackStack()
                        baseActivity.addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.RegistrationRequired, email, 1), true)
                    }
                    (userModelWrapper.user.userDetails.isVerified) == 0 -> {
                        sharedPreferenceManager?.putValue(AppConstants.KEY_TOKEN, userModelWrapper.user.accessToken)

                        baseActivity.popBackStack()
                        baseActivity.addDockableFragment(OtpVerificationFragment.newInstance(email, phone), true)
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

                        baseActivity.finish()
                        baseActivity.openActivity(HomeActivity::class.java)
                    }
                }
            }

            override fun onError(`object`: Any?) {
                // Log.d("test",`object`.toString())

                var web: WebResponse<Any?> = `object` as WebResponse<Any?>

                if (web.message.contains("email")) {
                    setCurrentItemByPosition(0)
                }
            }
        })
    }


    private fun contactDetails(positionToSelect: Int) {


        var phoneNo = ccp.fullNumberWithPlus.toString()

        if (edtPhoneNo.text.toString() == ""){
            UIHelper.showAlertDialog(context, "Phone number is required")
        }else {
            /*val regex = "^\\+(?:[0-9] ?){6,14}[0-9]$"

            val pattern: Pattern = Pattern.compile(regex)
            val matcher: Matcher = pattern.matcher(phoneNo)*/
            if (/*matcher.matches() && phoneNo.length <= 15 &&*/ ContactFragment.isValid) {
                val builder = AlertDialog.Builder(context!!)
                builder.setMessage("Is " + edtPhoneNo.stringTrimmed + " your valid Phone Number? ")
                        .setTitle("Alert")
                        .setCancelable(true)
                        .setNegativeButton("No"
                        ) { dialog, id -> dialog.cancel() }
                        .setPositiveButton("Yes") { dialog, id -> setCurrentItemByPosition(positionToSelect + 1) }
                val alert = builder.create()
                alert.show()
            }else{
                UIHelper.showAlertDialog(context, getString(R.string.phone_number_validation))

            }
        }
    }

    private fun personalDetails(positionToSelect: Int) {
        if (fragmentName == FragmentName.RegistrationRequired) {
            emailLayout.visibility = View.GONE

        }

      /*  if (txtTitle.stringTrimmed.isEmpty()) {
            UIHelper.showAlertDialog(context, "Please select Title")
            return
        }*/

        if (!edtFirstname.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter First Name")
            return
        }
        if (!edtLastName.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter Last Name")
            return
        }

        if (radioBtnCompany.isChecked || radioBtnIndividual.isChecked) {
        } else {
            UIHelper.showAlertDialog(context, "Please Select Type")
            return
        }

        if (radioBtnCompany.isChecked) {
            if (edtCompanyName.stringTrimmed.isEmpty()) {
                UIHelper.showAlertDialog(context, "Please enter your Business Name")
                return
            }
        }

        setCurrentItemByPosition(positionToSelect + 1)

    }

    private fun accountDetails(positionToSelect: Int) {


        if (!edtEmail.testValidity()) {
            UIHelper.showAlertDialog(context, getString(R.string.email_validation))
            return
        }

        edtPasswordReg.addValidator(PasswordValidation())
        edtConfirmPassReg.addValidator(PasswordValidation(edtPasswordReg))

        if (!edtPasswordReg.testValidity()) {
            UIHelper.showAlertDialog(context, getString(R.string.password_validation))
            return
        }

        if (!edtConfirmPassReg.testValidity()) {
            UIHelper.showAlertDialog(context, getString(R.string.confirm_password_validation))
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
                edtZipCode.clearFocus()


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


    private fun getIdFromSpinner(): Int {

        for (states in arrData) {
            if (states.name == edtState.stringTrimmed) {
                return states.id
            }
        }
        return -1
    }

    private fun getCountryFromSpinner(): String {

        for (country in AddressFragment.arrCountryData) {
            if (country.name == edtCountry.stringTrimmed) {
                edtCountry.setText(country.name)
                return country.name
            }
        }

        return ""
    }

}


