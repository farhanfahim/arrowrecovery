package com.tekrevol.arrowrecovery.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import com.nostra13.universalimageloader.core.ImageLoader
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.widget.TitleBar
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_editprofile.*
import kotlinx.android.synthetic.main.fragment_product_detail.*

class EditProfileFragment : BaseFragment() {

    private var selectedPosition: Int = 0

    companion object {

        fun newInstance(): EditProfileFragment {

            val args = Bundle()

            val fragment = EditProfileFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imgCamera.setOnClickListener(View.OnClickListener {

            UIHelper.cropImagePicker(homeActivity, this)
        })
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
            baseActivity.popBackStack()
        })
    }

    override fun setListeners() {

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

}

