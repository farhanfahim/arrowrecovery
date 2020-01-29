package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.AppConstants.AboutUs
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.fragments.abstracts.GenericContentFragment
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.receiving_model.Slug
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import java.util.*

class ProfileFragment : BaseFragment() {


    var aboutCall: Call<WebResponse<Any>>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserData()

    }

    companion object {

        fun newInstance(): ProfileFragment {

            val args = Bundle()

            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun getFragmentLayout(): Int {

        return R.layout.fragment_profile
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {


        linearlayoutMyorder.setOnClickListener(View.OnClickListener {

            baseActivity.addDockableFragment(MyOrderFragment.newInstance(), true)
        })

        linearlayoutChangePassword.setOnClickListener(View.OnClickListener {

            baseActivity.addDockableFragment(ChangePasswordFragment.newInstance(), true)

        })

        logout.setOnClickListener(View.OnClickListener {
            logoutClick()
        })


        contNotificationSetting.setOnClickListener {
            showNextBuildToast()
        }

        contPrivacyPolicy.setOnClickListener {
            privacyAPI(AppConstants.KEY_PRIVACY)
        }

        contTermsAndConditions.setOnClickListener {
            privacyAPI(AppConstants.KEY_TERMS)
           // baseActivity.addDockableFragment(GenericContentFragment.newInstance("Terms and Conditions", AboutUs), true)
        }


    }

    private fun setUserData() {
        ImageLoaderHelper.loadImageWithAnimations(imgProfile, currentUser.userDetails.imageUrl, true)
        var userName = sharedPreferenceManager.currentUser.name
        val fullName = sharedPreferenceManager.currentUser.userDetails.fullName
        val userEmail = sharedPreferenceManager.currentUser.email
        val address = sharedPreferenceManager.currentUser.userDetails.address
        val city = sharedPreferenceManager.currentUser.userDetails.city
        val country = sharedPreferenceManager.currentUser.country
        ImageLoaderHelper.loadImageWithouAnimationByPath(imgProfile, currentUser.userDetails.imageUrl, true)

        txtName.text = fullName
        txtUsername.text = userName
        txtEmail.text = userEmail
        if (country != null) {
            txtAddress.text = "$address, $city, $country"
        } else {
            txtAddress.text = "$address, $city"
        }


    }


    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    private fun privacyAPI(slugId: String) {
        val queryMap: Map<String, Any> = HashMap()
        aboutCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_PAGES.toString() + "/" + slugId, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                val pagesModel: Slug = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , Slug::class.java)
                baseActivity.addDockableFragment(GenericContentFragment.newInstance(pagesModel.getTitle(), pagesModel.getContent(), true), false)
            }

            override fun onError(`object`: Any?) {}
        })
    }

}