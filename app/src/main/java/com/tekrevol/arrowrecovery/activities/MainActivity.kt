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
import com.tekrevol.arrowrecovery.fragments.EditProfileFragment
import com.tekrevol.arrowrecovery.fragments.OptVerification
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
        socialLoginSendingModel.setClientId(userid)
        socialLoginSendingModel.setDeviceType(AppConstants.DEVICE_OS_ANDROID)
        socialLoginSendingModel.setEmail(email)
        socialLoginSendingModel.setImage(imUrl)
        socialLoginSendingModel.setPlatform(AppConstants.SOCIAL_MEDIA_PLATFORM_GOOGLE)
        socialLoginSendingModel.setUsername(name)
        socialLoginSendingModel.setToken("abc123")
        socialLoginSendingModel.setDeviceToken("abc123")
        WebServices(this, "", BaseURLTypes.BASE_URL, true).postAPIAnyObject(PATH_SOCIAL_LOGIN, socialLoginSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                val userModelWrapper: UserModelWrapper = getGson()!!.fromJson(getGson()!!.toJson(webResponse.result), UserModelWrapper::class.java)
                sharedPreferenceManager?.putObject(AppConstants.KEY_CURRENT_USER_MODEL, userModelWrapper.getUser());
                sharedPreferenceManager?.putValue(AppConstants.KEY_CURRENT_USER_ID, userModelWrapper.getUser().getId());
                sharedPreferenceManager?.putValue(AppConstants.KEY_TOKEN, userModelWrapper.getUser().getAccessToken());

                if ((sharedPreferenceManager?.currentUser?.userDetails?.isCompleted)!!.equals(0)) {
                    popBackStack()
                    addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.RegistrationRequired, 1), true)
                } else if ((sharedPreferenceManager?.currentUser?.userDetails?.isVerified)!!.equals(0)) {
                    popBackStack()
                    addDockableFragment(OptVerification.newInstance(), true)
                } else {
                    finish()
                    openActivity(HomeActivity::class.java)
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
        if (SharedPreferenceManager.getInstance(applicationContext).currentUser == null) {
            addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.SimpleLogin, 0), false)
        } else {
            if ((sharedPreferenceManager?.currentUser?.userDetails?.isCompleted)!!.equals(0)) {
                popBackStack()
                addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.RegistrationRequired, 1), true)
            } else if ((sharedPreferenceManager?.currentUser?.userDetails?.isVerified)!!.equals(0)) {
                popBackStack()
                addDockableFragment(OptVerification.newInstance(), true)
            } else {
                openActivity(HomeActivity::class.java)
                finish()
            }
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
                // mFbHelper?.performSignOut()

                if ((sharedPreferenceManager?.currentUser?.userDetails?.isCompleted)!!.equals(0)) {
                    popBackStack()
                    addDockableFragment(RegisterPagerFragment.newInstance(FragmentName.RegistrationRequired, 1), true)
                } else if ((sharedPreferenceManager?.currentUser?.userDetails?.isVerified)!!.equals(0)) {
                    popBackStack()
                    addDockableFragment(OptVerification.newInstance(), true)
                } else {
                    finish()
                    openActivity(HomeActivity::class.java)
                }
            }

            override fun onError(`object`: Any?) {}
        })
    }

    override fun onFbSignInFail() {
    }


}