package com.tekrevol.arrowrecovery.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.hbb20.CountryCodePicker
import com.nostra13.universalimageloader.core.ImageLoader
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.enums.BaseURLTypes
import com.tekrevol.arrowrecovery.enums.FileType
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.GooglePlaceHelper
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.managers.retrofit.entities.MultiFileModel
import com.tekrevol.arrowrecovery.models.Country
import com.tekrevol.arrowrecovery.models.SpinnerModel
import com.tekrevol.arrowrecovery.models.States
import com.tekrevol.arrowrecovery.models.receiving_model.UserModel
import com.tekrevol.arrowrecovery.models.sending_model.EditProfileSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.searchdialog.SimpleSearchDialogCompat
import com.tekrevol.arrowrecovery.searchdialog.core.SearchResultListener
import com.tekrevol.arrowrecovery.widget.TitleBar
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_editprofile.*
import kotlinx.android.synthetic.main.fragment_editprofile.contCountry
import kotlinx.android.synthetic.main.fragment_editprofile.contState
import kotlinx.android.synthetic.main.fragment_editprofile.edtCity
import kotlinx.android.synthetic.main.fragment_editprofile.edtPhoneNo
import kotlinx.android.synthetic.main.fragment_editprofile.edtZipCode
import kotlinx.android.synthetic.main.fragment_editprofile.imgMap
import kotlinx.android.synthetic.main.fragment_editprofile.map
import kotlinx.android.synthetic.main.fragment_editprofile.txtCountry
import kotlinx.android.synthetic.main.fragment_editprofile.txtState
import retrofit2.Call
import java.io.File
import java.io.IOException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class EditProfileFragment : BaseFragment() {

    var isValid = false
    private lateinit var ccpLoadNumber: CountryCodePicker
    private var selectedPosition: Int = 0
    var webCall: Call<WebResponse<Any>>? = null
    var userCall: Call<WebResponse<Any>>? = null
    private var fileTemporaryProfilePicture: File? = null
    var countryListAdapter: ListAdapter? = null
    private var selectedCountryIndex: Int = -1
    private var spinnerModelArrayList = ArrayList<SpinnerModel>()
    private var spinnerCountryArrayList = ArrayList<SpinnerModel>()

    private var locationClick: Long = 0

    var lat = 0.0
    var lng = 0.0
    var googlePlaceHelper: GooglePlaceHelper? = null
    companion object {

        var arrData: ArrayList<States> = ArrayList()
        var arrCountryData: ArrayList<Country> = ArrayList()

        fun newInstance(): EditProfileFragment {

            val args = Bundle()

            val fragment = EditProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ccpLoadNumber = ccp
        registerCarrierEditText()
        ccp.registerCarrierNumberEditText(edtPhoneNo)
        ccp.setNumberAutoFormattingEnabled(true)

        getCountry()
        //setFields()
        getUserDetail(sharedPreferenceManager.currentUser.id)

        if (radioBtnCompany.isChecked) {
            edtCompany.setText(sharedPreferenceManager.currentUser.userDetails.company)
           // edtKindCompany.setText(sharedPreferenceManager.currentUser.userDetails.kindOfCompany)
            edtCompany.visibility = View.VISIBLE
            //edtKindCompany.visibility = View.VISIBLE
        } else {
            edtCompany.visibility = View.GONE
            edtCompany.setText("")
           // edtKindCompany.visibility = View.GONE
           // edtKindCompany.setText("")
        }

    }

    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_editprofile
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.visibility = View.VISIBLE
        titleBar.setTitle("Edit Profile")
        titleBar.showSaveButton(homeActivity, View.OnClickListener {

            editProfileCall()

        })
    }

    override fun setListeners() {

        contState.setOnClickListener {
           // showProvidersInDialog(arrData)
        }
        contCountry.setOnClickListener {
           // showCountrySelectDialog()
        }

        imgCamera.setOnClickListener {

            UIHelper.cropImagePicker(homeActivity, this)
        }

        contTitle.setOnClickListener {
            UIHelper.showCheckedDialogBox(context, "Select Title", Constants.title, selectedPosition) { dialog, which ->
                selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                txtTitle.text = Constants.title[selectedPosition]
                dialog.dismiss()
            }
        }

        radioBtnIndividual.setOnClickListener {
            edtCompany.visibility = View.GONE
            edtCompany.setText("")
           // edtKindCompany.visibility = View.GONE
           // edtKindCompany.setText("")
        }

        radioBtnCompany.setOnClickListener {
            edtCompany.setText(sharedPreferenceManager.currentUser.userDetails.company)
           // edtKindCompany.setText(sharedPreferenceManager.currentUser.userDetails.kindOfCompany)
            edtCompany.visibility = View.VISIBLE
           // edtKindCompany.visibility = View.VISIBLE
        }

        txtAddress.setOnClickListener {
            if (SystemClock.elapsedRealtime() - locationClick < 2000) {
                return@setOnClickListener
            }
            locationClick = SystemClock.elapsedRealtime()
            googlePlaceHelper = GooglePlaceHelper(baseActivity, GooglePlaceHelper.PLACE_PICKER, object : GooglePlaceHelper.GooglePlaceDataInterface {
                override fun onPlaceActivityResult(longitude: Double, latitude: Double, locationName: String?) {
                    txtAddress.text = locationName
                    lat = latitude
                    lng = longitude

                    getCountryName(context,latitude,longitude)

                    var str: String = GooglePlaceHelper.getMapSnapshotURL(latitude,longitude)
                    ImageLoaderHelper.loadImageWithAnimations(imgMap, str, false)
                    map.visibility = View.VISIBLE

                }

                override fun onError(error: String?) {}
            }, this@EditProfileFragment, onCreated)

            googlePlaceHelper!!.openMapsActivity()
        }

        edtPhoneNo.setFocusable(false);
        edtPhoneNo.setClickable(true);
        edtPhoneNo.setOnClickListener {

            UIHelper.showAlertDialog(context, getString(R.string.kindly_sign_up))
        }
    }



    fun showProvidersInDialog(insuranceProvidersList: ArrayList<States>) {
        val dialog = SimpleSearchDialogCompat(context, "Select State",
                "Search here...", null, insuranceProvidersList, SearchResultListener<States> { dialog, item, position ->
            dialog.dismiss()
            txtState.text = item.name
        })

        dialog.show()
    }


    fun initCountryAdapter() {
        countryListAdapter = object : ArrayAdapter<Country?>(
                context!!,
                R.layout.dialog_item,
                android.R.id.text1,
                arrCountryData as List<Country>
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v: View = super.getView(position, convertView, parent)
                val tv = v.findViewById<TextView>(android.R.id.text1)
                val dp10 =
                        (10 * context.resources.displayMetrics.density + 0.5f).toInt()
                tv.compoundDrawablePadding = dp10
                return v
            }
        }
    }

    private fun showCountrySelectDialog() {
        val builder = android.app.AlertDialog.Builder(context, R.style.MyAlertDialogTheme)
        builder.setTitle("Select Country")
        builder.setSingleChoiceItems(
                countryListAdapter, selectedCountryIndex
        ) { dialog, index ->
            selectedCountryIndex = index

            txtCountry.text = arrCountryData[selectedCountryIndex].name
            txtState.text = ""
            //getStates(arrCountryData[selectedCountryIndex].id)
            dialog.dismiss()
        }
        builder.show()
    }


    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                fileTemporaryProfilePicture = File(result.uri.path!!)
                //                uploadImageFile(fileTemporaryProfilePicture.getPath(), result.getUri().toString());
                setImageAfterResult(result.uri.toString())
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                error.printStackTrace()
            }
        }
        if (googlePlaceHelper != null) {
            googlePlaceHelper!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setImageAfterResult(uploadFilePath: String) {
        activity!!.runOnUiThread {
            try {
                ImageLoader.getInstance().displayImage(uploadFilePath, imgProfile)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getUserDetail(id: Int) {


        val queryMap = java.util.HashMap<String, Any>()

        userCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_USER_SLASH + id, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val user: UserModel = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , UserModel::class.java)

                ImageLoaderHelper.loadImageWithAnimations(imgProfile, user.userDetails.imageUrl, true)

                if (user.userDetails.title == AppConstants.TITLE_MR) {
                    txtTitle.text = Constants.title[0]
                }
                if (user.userDetails.title == AppConstants.TITLE_MISS) {
                    txtTitle.text = Constants.title[1]
                }
                if (user.userDetails.title == AppConstants.TITLE_MRS) {
                    txtTitle.text = Constants.title[2]
                }
                if (user.userDetails.title == AppConstants.TITLE_MS) {
                    txtTitle.text = Constants.title[3]
                }

                if (user.userDetails.userType == AppConstants.USER_TYPE_INDIVIDUAL) {
                    radioBtnIndividual.isChecked = true
                }
                if (user.userDetails.userType == AppConstants.USER_TYPE_COMPANY) {
                    radioBtnCompany.isChecked = true
                }

                edtFirstName.setText(user.userDetails.firstName)
                edtLastName.setText(user.userDetails.lastName)
                edtCompany.setText(user.userDetails.company)
                edtPhoneNo.setText(user.userDetails.phone)
                ccpLoadNumber.fullNumber = edtPhoneNo.text.toString()
                txtAddress.text = sharedPreferenceManager.currentUser.userDetails.address
                if (user.userDetails.lat != null &&
                        user.userDetails.lng != null){
                    lat = user.userDetails.lat
                    lng = user.userDetails.lng
                    var str: String = GooglePlaceHelper.getMapSnapshotURL(user.userDetails.lat,user.userDetails.lng)
                    ImageLoaderHelper.loadImageWithAnimations(imgMap, str, false)
                    map.visibility = View.VISIBLE
                }
                edtZipCode.setText(user.userDetails.zipCode)
                edtCity.setText(user.userDetails.city)
                txtCountry.text = (user.userDetails.country)
                //edtKindCompany.setText(sharedPreferenceManager.currentUser.userDetails.kindOfCompany)
                //edtComment.setText(sharedPreferenceManager.currentUser.userDetails.about)
                txtState.text = (user.userDetails.state.name)


            }

            override fun onError(`object`: Any?) {
            }
        })

    }


    private fun setFields() {

        ImageLoaderHelper.loadImageWithAnimations(imgProfile, currentUser.userDetails.imageUrl, true)

        if (sharedPreferenceManager.currentUser.userDetails.title == AppConstants.TITLE_MR) {
            txtTitle.text = Constants.title[0]
        }
        if (sharedPreferenceManager.currentUser.userDetails.title == AppConstants.TITLE_MISS) {
            txtTitle.text = Constants.title[1]
        }
        if (sharedPreferenceManager.currentUser.userDetails.title == AppConstants.TITLE_MRS) {
            txtTitle.text = Constants.title[2]
        }
        if (sharedPreferenceManager.currentUser.userDetails.title == AppConstants.TITLE_MS) {
            txtTitle.text = Constants.title[3]
        }


        if (sharedPreferenceManager.currentUser.userDetails.userType == AppConstants.USER_TYPE_INDIVIDUAL) {
            radioBtnIndividual.isChecked = true
        }
        if (sharedPreferenceManager.currentUser.userDetails.userType == AppConstants.USER_TYPE_COMPANY) {
            radioBtnCompany.isChecked = true
        }

        edtFirstName.setText(sharedPreferenceManager.currentUser.userDetails.firstName)
        edtLastName.setText(sharedPreferenceManager.currentUser.userDetails.lastName)
        edtCompany.setText(sharedPreferenceManager.currentUser.userDetails.company)
        edtPhoneNo.setText(sharedPreferenceManager.currentUser.userDetails.phone)
        ccpLoadNumber.fullNumber = edtPhoneNo.text.toString()
        txtAddress.text = sharedPreferenceManager.currentUser.userDetails.address
        if (sharedPreferenceManager.currentUser.userDetails.lat != null &&
                sharedPreferenceManager.currentUser.userDetails.lng != null){
            lat = sharedPreferenceManager.currentUser.userDetails.lat
            lng = sharedPreferenceManager.currentUser.userDetails.lng
            var str: String = GooglePlaceHelper.getMapSnapshotURL(sharedPreferenceManager.currentUser.userDetails.lat,sharedPreferenceManager.currentUser.userDetails.lng)
            ImageLoaderHelper.loadImageWithAnimations(imgMap, str, false)
            map.visibility = View.VISIBLE
        }
        edtZipCode.setText(sharedPreferenceManager.currentUser.userDetails.zipCode)
        edtCity.setText(sharedPreferenceManager.currentUser.userDetails.city)
        txtCountry.text = (sharedPreferenceManager.currentUser.userDetails.country)
        //edtKindCompany.setText(sharedPreferenceManager.currentUser.userDetails.kindOfCompany)
        //edtComment.setText(sharedPreferenceManager.currentUser.userDetails.about)
        txtState.text = (sharedPreferenceManager.currentUser.userDetails.state.name)

    }


    private fun editProfileCall() {
        // Validations

        if (txtTitle.stringTrimmed.isEmpty()) {
            UIHelper.showAlertDialog(context, "Please select title")
            return
        }

        if (!edtFirstName.testValidity()) {

            UIHelper.showAlertDialog(context, "Please enter first name")
            return
        }
        if (!edtLastName.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter last name")
            return
        }


        if (radioBtnCompany.isChecked || radioBtnIndividual.isChecked) {
        } else {
            UIHelper.showAlertDialog(context, "Please select type")
            return
        }

        if (radioBtnCompany.isChecked) {
            if (edtCompany.stringTrimmed.isEmpty()) {
                UIHelper.showAlertDialog(context, "Please enter your business name")
                return
            }
        }


        var phoneNo = ccp.fullNumberWithPlus.toString()

        if (!edtPhoneNo.testValidity()){
            UIHelper.showAlertDialog(context, "Phone number is required")
            return
        }else {
            val regex = "^\\+(?:[0-9] ?){6,14}[0-9]$"

            val pattern: Pattern = Pattern.compile(regex)
            val matcher: Matcher = pattern.matcher(phoneNo)
            if (matcher.matches() && phoneNo.length <= 15 && isValid) {
                println("Invalid phone no")
            }else{
                UIHelper.showAlertDialog(context, getString(R.string.phone_number_validation))

            }
        }

        if (txtAddress.stringTrimmed.isEmpty()) {
            UIHelper.showAlertDialog(context, "Please enter your address")
            return

        }
        if (!edtZipCode.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter zipCode")
            return

        }
        if (!edtCity.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter your city")
            return
        }

        if (txtCountry.stringTrimmed.isEmpty()) {
            UIHelper.showAlertDialog(context, "Please select country")
            return
        }

        // Initialize Models
        val editProfileSendingModel = EditProfileSendingModel()
        val arrMultiFileModel = ArrayList<MultiFileModel>()

        // Adding Images
        if (fileTemporaryProfilePicture != null) {
            arrMultiFileModel.add(MultiFileModel(fileTemporaryProfilePicture, FileType.IMAGE, "image"))
        }

        // Setting data

        if (radioBtnCompany.isChecked) {
            editProfileSendingModel.userType = AppConstants.USER_TYPE_COMPANY
        }
        if (radioBtnIndividual.isChecked) {
            editProfileSendingModel.userType = AppConstants.USER_TYPE_INDIVIDUAL
        }
        editProfileSendingModel.phone = (ccp.fullNumberWithPlus.toString())
        editProfileSendingModel.firstName = (edtFirstName.stringTrimmed)
        editProfileSendingModel.lastName = (edtLastName.stringTrimmed)
        editProfileSendingModel.address = (txtAddress.stringTrimmed)
        editProfileSendingModel.zipCode = (edtZipCode.stringTrimmed)
        editProfileSendingModel.company = (edtCompany.stringTrimmed)
        editProfileSendingModel.name = (edtFirstName.stringTrimmed)
        editProfileSendingModel.city = (edtCity.stringTrimmed)
        editProfileSendingModel.country = getCountryFromSpinner()
        if (getIdFromSpinner() == -1){
            Toast.makeText(context,"State required",Toast.LENGTH_SHORT).show()
            return

        }else {
            editProfileSendingModel.stateId = getIdFromSpinner()
        }
        editProfileSendingModel.isCompleted = (1)
        editProfileSendingModel.latitude = lat
        editProfileSendingModel.longitude = lng
       // editProfileSendingModel.kindOfCompany = edtKindCompany.stringTrimmed

        if (txtTitle.text == Constants.title[0]) {
            editProfileSendingModel.title = AppConstants.TITLE_MR
        }
        if (txtTitle.text == Constants.title[1]) {
            editProfileSendingModel.title = AppConstants.TITLE_MISS
        }
        if (txtTitle.text == Constants.title[2]) {
            editProfileSendingModel.title = AppConstants.TITLE_MRS
        }

        WebServices(baseActivity, token, BaseURLTypes.BASE_URL, true)
                .postMultipartAPI(WebServiceConstants.PATH_PROFILE, arrMultiFileModel, editProfileSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
                    override fun requestDataResponse(webResponse: WebResponse<Any>) {

                        UIHelper.showAlertDialog1(webResponse.message, "Update Profile", { dialog, which ->
                            baseActivity.popBackStack()
                        }, context)

                    }

                    override fun onError(`object`: Any) {

                    }
                })
    }

    private fun getStates(statId: Int, stateName: String) {

        txtState.text = ""
        val query: MutableMap<String, Any> = HashMap()

        query[WebServiceConstants.Q_PARAM_COUNTRY_ID] = statId
        webCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.Q_PARAM_STATES, query, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : com.google.gson.reflect.TypeToken<ArrayList<States>>() {

                }.type

                AddressFragment.arrData = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result), type)

                spinnerModelArrayList.clear()

                for (states in AddressFragment.arrData) {
                    spinnerModelArrayList.add(SpinnerModel(states.name))
                }

                for (arrState in AddressFragment.arrData) {
                    if (stateName == arrState.name) {
                        contState.visibility = View.VISIBLE
                        txtState.text = arrState.name
                        return
                    } else {
                        txtState.text = "state required"
                    }
                }

                if (spinnerModelArrayList.isEmpty()) {
                    contState.visibility = View.GONE
                    Toast.makeText(context, "No State Available", Toast.LENGTH_SHORT).show()
                } else {
                    //contState.visibility = View.VISIBLE
                }
            }

            override fun onError(`object`: Any?) {}
        })

    }

    private fun getIdFromSpinner(): Int {

        for (states in arrData) {
            if (states.name == txtState.stringTrimmed) {
                txtState.text = states.name
                return states.id
            }
        }
        return -1
    }
    private fun getCountry() {

        val query: MutableMap<String, Any> = HashMap()
        webCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.Q_PARAM_COUNTRY, query, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : com.google.gson.reflect.TypeToken<ArrayList<Country>>() {

                }.type

                arrCountryData = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result), type)

                spinnerCountryArrayList.clear()

                for (country in arrCountryData) {
                    if (country.name == txtCountry.stringTrimmed) {
                        getStates(country.id,"")
                    }
                }
                initCountryAdapter()

            }

            override fun onError(`object`: Any?) {
                Toast.makeText(context,`object`.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getCountryFromSpinner(): String {

        for (country in arrCountryData) {
            if (country.name == txtCountry.stringTrimmed) {
                txtCountry.text = country.name

                getStates(country.id,"")
                return country.name
            }
        }

        return ""
    }

    override fun onDestroyView() {
        webCall?.cancel()
        userCall?.cancel()
        super.onDestroyView()
    }


    fun getCountryName(context: Context?, latitude: Double, longitude: Double) {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            var result: Address
            if (addresses != null && !addresses.isEmpty()) {
                if (addresses[0].countryName != null) {
                    var country = addresses[0].countryName


                        for (arr in arrCountryData) {
                            if (addresses[0].countryName == "United States") {
                                txtCountry.text = arrCountryData[0].name
                                getStates(arrCountryData[0].id, addresses[0].adminArea)

                            } else if (addresses[0].countryName == "Canada") {
                                txtCountry.text = arrCountryData[1].name
                                txtState.text = ""
                                getStates(arrCountryData[1].id, addresses[0].adminArea)


                            } else if (addresses[0].countryName == "Mexico") {
                                txtCountry.text = arrCountryData[2].name
                                txtState.text = ""
                                getStates(arrCountryData[2].id, addresses[0].adminArea)

                            } else {
                                txtAddress.text = sharedPreferenceManager.currentUser.userDetails.address
                                UIHelper.showAlertDialog(context, getString(R.string.we_are_not_available))
                                return
                            }
                        }

                }
            }
        } catch (ignored: IOException) {
            //do something
        }
    }

    private fun registerCarrierEditText() {
        ccpLoadNumber.registerCarrierNumberEditText(edtPhoneNo)
        ccpLoadNumber.setPhoneNumberValidityChangeListener { isValidNumber ->
            isValid = isValidNumber
        }

        ccpLoadNumber.registerCarrierNumberEditText(edtPhoneNo)
    }


}

