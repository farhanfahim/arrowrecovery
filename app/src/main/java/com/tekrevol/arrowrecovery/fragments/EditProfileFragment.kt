package com.tekrevol.arrowrecovery.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
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
import kotlinx.android.synthetic.main.fragment_editprofile.contState
import kotlinx.android.synthetic.main.fragment_editprofile.contTitle
import kotlinx.android.synthetic.main.fragment_editprofile.image
import kotlinx.android.synthetic.main.fragment_editprofile.inputAddress
import kotlinx.android.synthetic.main.fragment_editprofile.inputCity
import kotlinx.android.synthetic.main.fragment_editprofile.inputCountry
import kotlinx.android.synthetic.main.fragment_editprofile.inputPhoneNo
import kotlinx.android.synthetic.main.fragment_editprofile.inputZipCode
import kotlinx.android.synthetic.main.fragment_editprofile.txtKindCompany
import kotlinx.android.synthetic.main.fragment_editprofile.txtState
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

        fun newInstance(): EditProfileFragment {

            val args = Bundle()

            val fragment = EditProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getStates()
        setFields()

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

        imgCamera.setOnClickListener(View.OnClickListener {

            UIHelper.cropImagePicker(homeActivity, this)
        })

        contTitle.setOnClickListener {
            UIHelper.showCheckedDialogBox(context, "Select Title", Constants.title, selectedPosition) { dialog, which ->
                selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                txtTitle.text = Constants.title[selectedPosition]
                dialog.dismiss()
            }
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
        txtTitle.text = sharedPreferenceManager.currentUser.userDetails.title
        inputFirstName.setText(sharedPreferenceManager.currentUser.userDetails.firstName)
        inputLastName.setText(sharedPreferenceManager.currentUser.userDetails.lastName)
        inputCompany.setText(sharedPreferenceManager.currentUser.userDetails.company)
        inputPhoneNo.setText(sharedPreferenceManager.currentUser.userDetails.phone)
        inputAddress.setText(sharedPreferenceManager.currentUser.userDetails.address)
        inputZipCode.setText(sharedPreferenceManager.currentUser.userDetails.zipCode)
        inputCity.setText(sharedPreferenceManager.currentUser.userDetails.city)
        inputCountry.setText(sharedPreferenceManager.currentUser.country)
        txtKindCompany.setText(sharedPreferenceManager.currentUser.kindOfCompany)
        txtState.text = getNameFromSpinner()

    }


    fun editProfileCall() {
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

        if (inputCompany.stringTrimmed.isEmpty()) {
            UIHelper.showAlertDialog(context, "Please enter your company name")
            return
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

        editProfileSendingModel.phone = (inputPhoneNo.stringTrimmed)
        editProfileSendingModel.firstName = (inputFirstName.stringTrimmed)
        editProfileSendingModel.lastName = (inputLastName.stringTrimmed)
        editProfileSendingModel.address = (inputAddress.stringTrimmed)
        editProfileSendingModel.zipCode = (inputZipCode.stringTrimmed)
        editProfileSendingModel.company = (inputCompany.stringTrimmed)
        editProfileSendingModel.name = (inputFirstName.stringTrimmed)
        editProfileSendingModel.city = (inputCity.stringTrimmed)
        editProfileSendingModel.stateId = getIdFromSpinner()



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

                AddressFragment.arrData = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result), type)

                spinnerModelArrayList.clear()

                for (states in AddressFragment.arrData) {
                    spinnerModelArrayList.add(SpinnerModel(states.name))
                    //spinnerModelArrayList.add(SpinnerModel(categories.id))
                }
            }

            override fun onError(`object`: Any?) {}
        })

    }

    private fun getIdFromSpinner(): Int {

        for (states in AddressFragment.arrData) {
            if (states.name == txtState.stringTrimmed) {
                return states.id
            }
        }
        return -1
    }
    private fun getNameFromSpinner(): String? {

        for (states in AddressFragment.arrData) {
            if (states.id == sharedPreferenceManager.currentUser.userDetails.stateId) {
                return states.name
            }
        }
        return null
    }

}

