package com.tekrevol.arrowrecovery.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.modules.facebooklogin.FacebookHelper
import com.modules.facebooklogin.FacebookResponse
import com.modules.facebooklogin.FacebookUser
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants.PATH_SOCIAL_LOGIN
import com.tekrevol.arrowrecovery.enums.BaseURLTypes
import com.tekrevol.arrowrecovery.enums.FragmentName
import com.tekrevol.arrowrecovery.fragments.*
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.RunTimePermissions
import com.tekrevol.arrowrecovery.managers.SharedPreferenceManager
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.sending_model.SocialLoginSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.UserModelWrapper
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse

class MainActivity : BaseActivity(), FacebookResponse {

    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private var sharedPreferenceManager: SharedPreferenceManager? = null
    private lateinit var firebaseAuth: FirebaseAuth

    private var mFbHelper: FacebookHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        mFbHelper = FacebookHelper(this,
                "id,name,email,gender,birthday,picture",
                this)

        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.d("error", e.toString())
            }
        } else {
            mFbHelper!!.onActivityResult(requestCode, resultCode, data)

        }

    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val email = account?.email
        val name = account?.displayName
        val userid = account?.id
        var imUrl = ""
        imUrl = if (account?.photoUrl == null || account.photoUrl.toString().isEmpty()) { //set default image
            WebServiceConstants.IMAGE_BASE_URL
        } else {
            account.photoUrl.toString() //photo_url is String
        }
        var socialLoginSendingModel = SocialLoginSendingModel()
        socialLoginSendingModel.clientId = userid
        socialLoginSendingModel.deviceType = AppConstants.DEVICE_OS_ANDROID
        socialLoginSendingModel.email = email
        socialLoginSendingModel.image = imUrl
        socialLoginSendingModel.platform = AppConstants.SOCIAL_MEDIA_PLATFORM_GOOGLE
        socialLoginSendingModel.username = name
        socialLoginSendingModel.token = "abc123"
        socialLoginSendingModel.deviceToken = "abc123"
        WebServices(this, "", BaseURLTypes.BASE_URL, true).postAPIAnyObject(PATH_SOCIAL_LOGIN, socialLoginSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                val userModelWrapper: UserModelWrapper = getGson()!!.fromJson(getGson()!!.toJson(webResponse.result), UserModelWrapper::class.java)
                when {
                    ((SharedPreferenceManager.getInstance(applicationContext).currentUser != null) && SharedPreferenceManager.getInstance(applicationContext).currentUser.isLoginVerified) -> {
                        popBackStack()
                        addDockableFragment(TwoFactorVerification.newInstance(), true)
                    }
                    (userModelWrapper.user.userDetails.isCompleted == 0) -> {
                        popBackStack()
                        addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.RegistrationRequired, 1), true)
                    }
                    (userModelWrapper.user.userDetails.isVerified)!! == 0 -> {
                        popBackStack()
                        addDockableFragment(OtpVerification.newInstance(), true)
                    }
                    (userModelWrapper.user.userDetails.isApproved)!! == 0 -> {
                        popBackStack()
                        addDockableFragment(ThankyouFragment.newInstance(), true)
                    }
                    else -> {
                        sharedPreferenceManager?.putObject(AppConstants.KEY_CURRENT_USER_MODEL, userModelWrapper.user)
                        sharedPreferenceManager?.putValue(AppConstants.KEY_CURRENT_USER_ID, userModelWrapper.user.id)
                        sharedPreferenceManager?.putValue(AppConstants.KEY_TOKEN, userModelWrapper.user.accessToken)

                        finish()
                        openActivity(HomeActivity::class.java)
                    }
                }

            }

            override fun onError(`object`: Any?) {}
        })

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

        when {
            (SharedPreferenceManager.getInstance(applicationContext).currentUser == null) ->
                addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.SimpleLogin, 0), false)

            else -> {
                when {
                    ((sharedPreferenceManager?.currentUser?.userDetails?.isCompleted) == 0) -> {
                        popBackStack()
                        addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.RegistrationRequired, 1), true)
                    }
                    (sharedPreferenceManager?.currentUser?.userDetails?.isVerified) == 0 -> {

                        popBackStack()
                        addDockableFragment(OtpVerification.newInstance(), true)

                    }
                    else -> {
                        finish()
                        openActivity(HomeActivity::class.java)
                    }
                }
            }
        }
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

    fun googleSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onFbSignInSuccess() {
    }

    override fun onFBSignOut() {
    }

    override fun onFbProfileReceived(facebookUser: FacebookUser) {
        var socialLoginSendingModel = SocialLoginSendingModel()
        socialLoginSendingModel.clientId = facebookUser.facebookID
        socialLoginSendingModel.deviceType = AppConstants.DEVICE_OS_ANDROID
        socialLoginSendingModel.email = facebookUser.email
        socialLoginSendingModel.image = facebookUser.profilePic
        socialLoginSendingModel.platform = AppConstants.SOCIAL_MEDIA_PLATFORM_FACEBOOK
        socialLoginSendingModel.username = facebookUser.name
        socialLoginSendingModel.token = "abc123"
        socialLoginSendingModel.deviceToken = "abc123"
        WebServices(this, "", BaseURLTypes.BASE_URL, true).postAPIAnyObject(PATH_SOCIAL_LOGIN, socialLoginSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                val userModelWrapper: UserModelWrapper = getGson()!!.fromJson(getGson()!!.toJson(webResponse.result), UserModelWrapper::class.java)
                when {
                    (userModelWrapper.user.userDetails.isCompleted == 0) -> {
                        popBackStack()
                        addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.RegistrationRequired, 1), true)
                    }
                    (userModelWrapper.user.userDetails.isVerified) == 0 -> {

                        popBackStack()
                        addDockableFragment(OtpVerification.newInstance(), true)

                    }
                    (userModelWrapper.user.userDetails.isApproved) == 0 -> {
                        popBackStack()
                        addDockableFragment(ThankyouFragment.newInstance(), true)
                    }
                    else -> {
                        finish()
                        openActivity(HomeActivity::class.java)
                    }
                }
            }

            override fun onError(`object`: Any?) {}
        })
    }

    override fun onFbSignInFail() {
    }


}