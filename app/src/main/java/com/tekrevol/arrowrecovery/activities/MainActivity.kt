package com.tekrevol.arrowrecovery.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.GravityCompat
import com.modules.facebooklogin.FacebookHelper
import com.modules.facebooklogin.FacebookResponse
import com.modules.facebooklogin.FacebookUser
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants.PATH_SOCIAL_LOGIN
import com.tekrevol.arrowrecovery.enums.BaseURLTypes
import com.tekrevol.arrowrecovery.fragments.RegisterPagerFragment
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.RunTimePermissions
import com.tekrevol.arrowrecovery.managers.SharedPreferenceManager
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.sending_model.SocialLoginSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.UserModelWrapper
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse

class MainActivity : BaseActivity(), FacebookResponse {
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    var sharedPreferenceManager: SharedPreferenceManager? = null

    private var mFbHelper: FacebookHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        setContentView(R.layout.activity_main);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        mFbHelper = FacebookHelper(this,
                "id,name,email,gender,birthday,picture",
                this)


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mFbHelper!!.onActivityResult(requestCode, resultCode, data)
    }

    override val viewId: Int = R.layout.activity_main
    override val titlebarLayoutId: Int = R.id.titlebar
    override val drawerLayoutId: Int = R.id.drawer_layout
    override val dockableFragmentId: Int = R.id.contMain
    override val drawerFragmentId: Int = R.id.contDrawer


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        RunTimePermissions.verifyStoragePermissions(this)
        initFragments()
    }


    private fun initFragments() {
        if (SharedPreferenceManager.getInstance(applicationContext).currentUser == null) {
            addDockableFragment(RegisterPagerFragment.newInstance(), false)
        } else {
            openActivity(HomeActivity::class.java)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        //        if (DeviceUtils.isRooted(getApplicationContext())) {
//            showAlertDialogAndExitApp("This device is rooted. You can't use this app.");
//        }
    }

    override fun onBackPressed() {
        /**
         * Show Close app popup if no or single fragment is in stack. otherwise check if drawer is open. Close it..
         */
        if (supportFragmentManager.backStackEntryCount > 1) {
            if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                drawerLayout!!.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
                val fragments = supportFragmentManager.fragments
                val fragment = fragments[fragments.size - 1] as BaseFragment
                fragment.setTitlebar(titleBar)
            }
        } else if (supportFragmentManager.fragments[0] is RegisterPagerFragment) {
            val registerPagerFragment = supportFragmentManager.fragments[0] as RegisterPagerFragment
            if (registerPagerFragment.positionToSelect == 0) {
                moveTaskToBack(true)
            } else {
                registerPagerFragment.setCurrentItemByPosition(registerPagerFragment.positionToSelect - 1)
            }

        } else {
            moveTaskToBack(true)
        }
    }

    fun fbSignIn() {
        mFbHelper?.performSignIn(this)
    }

    override fun onFbSignInSuccess() {
    }

    override fun onFBSignOut() {
    }

    override fun onFbProfileReceived(facebookUser: FacebookUser) {
        val socialLoginSendingModel = SocialLoginSendingModel()
        socialLoginSendingModel.setClientId(facebookUser.facebookID)
        socialLoginSendingModel.setDeviceType(AppConstants.DEVICE_OS_ANDROID)
        socialLoginSendingModel.setEmail(facebookUser.email)
        socialLoginSendingModel.setImage(facebookUser.profilePic)
        socialLoginSendingModel.setPlatform(AppConstants.SOCIAL_MEDIA_PLATFORM_FACEBOOK)
        socialLoginSendingModel.setUsername(facebookUser.name)
        socialLoginSendingModel.setToken("abc123")
        socialLoginSendingModel.setDeviceToken("abc123")
        WebServices(this, "", BaseURLTypes.BASE_URL, true).postAPIAnyObject(PATH_SOCIAL_LOGIN, socialLoginSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                val userModelWrapper: UserModelWrapper = getGson()!!.fromJson(getGson()!!.toJson(webResponse.result), UserModelWrapper::class.java)
                sharedPreferenceManager?.putObject(AppConstants.KEY_CURRENT_USER_MODEL, userModelWrapper.getUser());
                sharedPreferenceManager?.putValue(AppConstants.KEY_CURRENT_USER_ID, userModelWrapper.getUser().getId());
                sharedPreferenceManager?.putValue(AppConstants.KEY_TOKEN, userModelWrapper.getUser().getAccessToken());

                mFbHelper!!.performSignOut()
            }

            override fun onError(`object`: Any?) {}
        })
    }

    override fun onFbSignInFail() {
    }

}