package com.tekrevol.arrowrecovery.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.nostra13.universalimageloader.core.ImageLoader
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.enums.BaseURLTypes
import com.tekrevol.arrowrecovery.enums.FileType
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.managers.retrofit.entities.MultiFileModel
import com.tekrevol.arrowrecovery.models.IntWrapper
import com.tekrevol.arrowrecovery.models.SpinnerModel
import com.tekrevol.arrowrecovery.models.States
import com.tekrevol.arrowrecovery.models.UserDetails
import com.tekrevol.arrowrecovery.models.sending_model.EditProfileSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_editprofile.*
import kotlinx.android.synthetic.main.fragment_editprofile.contTitle
import kotlinx.android.synthetic.main.fragment_editprofile.inputKindCompany
import kotlinx.android.synthetic.main.fragment_editprofile.radio_btn_company
import kotlinx.android.synthetic.main.fragment_editprofile.radio_btn_individual
import kotlinx.android.synthetic.main.fragment_editprofile.txtTitle
import retrofit2.Call
import java.io.File
import java.util.ArrayList
import java.util.HashMap

class EditProfileFragment : BaseFragment() {

    private var selectedPosition: Int = 0
    var webCall: Call<WebResponse<Any>>? = null
    private var fileTemporaryProfilePicture: File? = null
    private var spinnerModelArrayList = ArrayList<SpinnerModel>()

    companion object {

        var arrData: ArrayList<States> = ArrayList()

        fun newInstance(): EditProfileFragment {

            val args = Bundle()

            val fragment = EditProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getStates()
        setFields()


        if (radio_btn_company.isChecked){
            inputCompany.setText(sharedPreferenceManager.currentUser.userDetails.company)
            inputKindCompany.setText(sharedPreferenceManager.currentUser.userDetails.kindOfCompany)
            inputCompany.visibility = View.VISIBLE
            inputKindCompany.visibility = View.VISIBLE
        }else{
            inputCompany.visibility = View.GONE
            inputCompany.setText("")
            inputKindCompany.visibility = View.GONE
            inputKindCompany.setText("")
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
            UIHelper.showSpinnerDialog(this@EditProfileFragment, spinnerModelArrayList, "Selected States", txtState, null, null, IntWrapper(0))
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

        radio_btn_individual.setOnClickListener{
            inputCompany.visibility = View.GONE
            inputCompany.setText("")
            inputKindCompany.visibility = View.GONE
            inputKindCompany.setText("")
        }

        radio_btn_company.setOnClickListener{
            inputCompany.setText(sharedPreferenceManager.currentUser.userDetails.company)
            inputKindCompany.setText(sharedPreferenceManager.currentUser.userDetails.kindOfCompany)
            inputCompany.visibility = View.VISIBLE
            inputKindCompany.visibility = View.VISIBLE
        }
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
    }

    private fun setImageAfterResult(uploadFilePath: String) {
        activity!!.runOnUiThread {
            try {
                ImageLoader.getInstance().displayImage(uploadFilePath, image)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setFields(){

        ImageLoaderHelper.loadImageWithAnimations(image, currentUser.userDetails.imageUrl, true)

        if (sharedPreferenceManager.currentUser.userDetails.title == AppConstants.TITLE_MR){
            txtTitle.text = Constants.title[0]
        }
        if (sharedPreferenceManager.currentUser.userDetails.title == AppConstants.TITLE_MISS){
            txtTitle.text = Constants.title[1]
        }
        if (sharedPreferenceManager.currentUser.userDetails.title == AppConstants.TITLE_MRS){
            txtTitle.text = Constants.title[2]
        }
        if (sharedPreferenceManager.currentUser.userDetails.title == AppConstants.TITLE_MS){
            txtTitle.text = Constants.title[3]
        }


        if (sharedPreferenceManager.currentUser.userDetails.userType == AppConstants.USER_TYPE_INDIVIDUAL){
            radio_btn_individual.isChecked = true
        }
        if (sharedPreferenceManager.currentUser.userDetails.userType == AppConstants.USER_TYPE_COMPANY){
            radio_btn_company.isChecked = true
        }

        inputFirstName.setText(sharedPreferenceManager.currentUser.userDetails.firstName)
        inputLastName.setText(sharedPreferenceManager.currentUser.userDetails.lastName)
        inputCompany.setText(sharedPreferenceManager.currentUser.userDetails.company)
        inputPhoneNo.setText(sharedPreferenceManager.currentUser.userDetails.phone)
        inputAddress.setText(sharedPreferenceManager.currentUser.userDetails.address)
        inputZipCode.setText(sharedPreferenceManager.currentUser.userDetails.zipCode)
        inputCity.setText(sharedPreferenceManager.currentUser.userDetails.city)
        inputCountry.setText(sharedPreferenceManager.currentUser.country)
        inputKindCompany.setText(sharedPreferenceManager.currentUser.userDetails.kindOfCompany)
        txtState.text = (sharedPreferenceManager.currentUser.userDetails.state.name)

    }


    private fun editProfileCall() {
        // Validations

        if (txtTitle.stringTrimmed.isEmpty()) {
            UIHelper.showAlertDialog(context, "Please select title")
            return
        }

        if (!inputFirstName.testValidity()) {

            UIHelper.showAlertDialog(context, "Please enter first name")
            return
        }
        if (!inputLastName.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter last name")
            return
        }


        if (radio_btn_company.isChecked || radio_btn_individual.isChecked) {
        } else {
            UIHelper.showAlertDialog(context, "Please select type")
            return
        }

        if (radio_btn_company.isChecked){
            if (inputCompany.stringTrimmed.isEmpty()) {
                UIHelper.showAlertDialog(context, "Please enter your company name")
                return
            }
        }

        if (!inputPhoneNo.testValidity()) {
            UIHelper.showAlertDialog(context, "Please enter your phone no")
            return
        }

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

        if (txtState.stringTrimmed.isEmpty()) {
            UIHelper.showAlertDialog(context, "Please select state")
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

        if (radio_btn_company.isChecked){
            editProfileSendingModel.userType = AppConstants.USER_TYPE_COMPANY
        }
        if (radio_btn_individual.isChecked){
            editProfileSendingModel.userType = AppConstants.USER_TYPE_INDIVIDUAL
        }
        editProfileSendingModel.phone = (inputPhoneNo.stringTrimmed)
        editProfileSendingModel.firstName = (inputFirstName.stringTrimmed)
        editProfileSendingModel.lastName = (inputLastName.stringTrimmed)
        editProfileSendingModel.address = (inputAddress.stringTrimmed)
        editProfileSendingModel.zipCode = (inputZipCode.stringTrimmed)
        editProfileSendingModel.company = (inputCompany.stringTrimmed)
        editProfileSendingModel.name = (inputFirstName.stringTrimmed)
        editProfileSendingModel.city = (inputCity.stringTrimmed)
        editProfileSendingModel.stateId = getIdFromSpinner()
        editProfileSendingModel.isCompleted = (1)
        editProfileSendingModel.kindOfCompany = inputKindCompany.stringTrimmed

        if (txtTitle.text == Constants.title[0]){
            editProfileSendingModel.title = AppConstants.TITLE_MR
        }
        if (txtTitle.text == Constants.title[1]){
            editProfileSendingModel.title = AppConstants.TITLE_MISS
        }
        if (txtTitle.text == Constants.title[2]){
            editProfileSendingModel.title = AppConstants.TITLE_MRS
        }



        WebServices(baseActivity, token, BaseURLTypes.BASE_URL, true)
                .postMultipartAPI(WebServiceConstants.PATH_PROFILE, arrMultiFileModel, editProfileSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
                    override fun requestDataResponse(webResponse: WebResponse<Any>) {
                        val userDetails = gson.fromJson(gson.toJson(webResponse.result), UserDetails::class.java)
                        val currentUser = sharedPreferenceManager.currentUser
                        currentUser.userDetails = userDetails
                        sharedPreferenceManager.putObject(AppConstants.KEY_CURRENT_USER_MODEL, currentUser)
//                        baseActivity.finish()
//                        baseActivity.openActivity(HomeActivity::class.java)
                        baseActivity.popBackStack()
                    }

                    override fun onError(`object`: Any) {

                    }
                })
    }

    private fun getStates() {

        val query: MutableMap<String, Any> = HashMap()
        webCall = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.Q_PARAM_STATES, query, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : com.google.gson.reflect.TypeToken<ArrayList<States>>() {

                }.type

                arrData = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result), type)

                spinnerModelArrayList.clear()

                for (states in arrData) {
                    spinnerModelArrayList.add(SpinnerModel(states.name))
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


}

