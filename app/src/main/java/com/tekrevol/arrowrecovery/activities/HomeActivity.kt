package com.tekrevol.arrowrecovery.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.GravityCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.AppConstants.KEY_FIREBASE_TOKEN_UPDATED
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.enums.BaseURLTypes
import com.tekrevol.arrowrecovery.fragments.DashboardPagerFragment
import com.tekrevol.arrowrecovery.fragments.RightSideMenuFragment
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.libraries.residemenu.ResideMenu
import com.tekrevol.arrowrecovery.managers.SharedPreferenceManager
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.receiving_model.UserModel
import com.tekrevol.arrowrecovery.models.sending_model.EditProfileSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse

class HomeActivity : BaseActivity() {
    var navigationView: NavigationView? = null
    private var sharedPreferenceManager: SharedPreferenceManager? = null
    var contMain: FrameLayout? = null
    var contParentActivityLayout: RelativeLayout? = null
    var rightSideMenuFragment: RightSideMenuFragment? = null
        private set
    var resideMenu: ResideMenu? = null
        private set
    private val mDownScaled: Bitmap? = null
    private val mBackgroundFilename: String? = null
    private val background: Bitmap? = null
    var blurImage: ImageView? = null
        private set

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        if (getCurrentUser() != null) {
            checkAproved()
            if (isFirebaseTokenUpdated()) {
                updateProfileWithFirebaseToken()

            } else {
                callRefreshApi()
            }
        }


    }

    private fun checkAproved() {

        val services = WebServices(this, sharedPreferenceManager!!.getString(AppConstants.KEY_TOKEN), BaseURLTypes.BASE_URL, false)
        services.postAPIAnyObject(WebServiceConstants.PATH_ME, "", object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val userModel = getGson()!!.fromJson(getGson()!!.toJson(webResponse.result), UserModel::class.java)
                val currentUser = sharedPreferenceManager!!.currentUser
                currentUser.userDetails = userModel.userDetails
                sharedPreferenceManager!!.putObject(AppConstants.KEY_CURRENT_USER_MODEL, currentUser)
            }

            override fun onError(`object`: Any?) {}
        })
    }

    open fun getCurrentUser(): UserModel? {
        return sharedPreferenceManager?.getCurrentUser()
    }


    private fun callRefreshApi() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                //   Log.w(HomeActivity.TAG, "getInstanceId failed", task.exception)
                return@OnCompleteListener
            }
            // Get new Instance ID token
            val token = task.result!!.token
            //                TODO call refresh api and if response is 402 logout current session
        })
    }


    private fun updateProfileWithFirebaseToken() {
        val model = EditProfileSendingModel()
        model.setDeviceType(AppConstants.DEVICE_OS_ANDROID)
        model.setDeviceToken(sharedPreferenceManager!!.getString(AppConstants.KEY_FIREBASE_TOKEN))
        val services = WebServices(this, sharedPreferenceManager!!.getString(AppConstants.KEY_TOKEN), BaseURLTypes.BASE_URL, false)
        services.postAPIAnyObject(WebServiceConstants.PATH_PROFILE, model.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                if (webResponse.isSuccess()) {
                    SharedPreferenceManager.getInstance(this@HomeActivity).putValue(KEY_FIREBASE_TOKEN_UPDATED, false)
                }
            }

            override fun onError(`object`: Any?) {}
        })
    }

    private fun isFirebaseTokenUpdated(): Boolean {
        return SharedPreferenceManager.getInstance(this).getBoolean(KEY_FIREBASE_TOKEN_UPDATED)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val intentData = intent.getStringExtra(AppConstants.JSON_STRING_KEY)
        navigationView = findViewById(R.id.nav_view)
        navigationView?.background?.setColorFilter(-0x80000000, PorterDuff.Mode.MULTIPLY)
        contMain = findViewById(R.id.contMain)
        contParentActivityLayout = findViewById(R.id.contParentActivityLayout)
        blurImage = findViewById(R.id.imageBlur)

        initFragments()

        /*     if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                 val manager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                 if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                     checkState()
                 } else {
                     UIHelper.showAlertDialog1("You have not enabled GPS, please enable to proceed ", "Logout", { dialog, which ->
                         sharedPreferenceManager!!.clearDB()
                         clearAllActivitiesExceptThis(MainActivity::class.java)
                     }, this)
                 }
             } else {
                 UIHelper.showAlertDialog1("You have not enabled Location, please enable to proceed ", "Logout", { dialog, which ->
                     sharedPreferenceManager!!.clearDB()
                     clearAllActivitiesExceptThis(MainActivity::class.java)
                 }, this)
             }*/
    }

    private fun checkState() {

        // FIXME changes needed
        /*val googleAddressModel = GooglePlaceHelper.getCurrentLocation(this, false)
        if (((sharedPreferenceManager?.currentUser?.userDetails?.state?.name)?.equals(googleAddressModel.address)!!) || ((sharedPreferenceManager?.currentUser?.userDetails?.state?.shortName)?.equals(googleAddressModel.address)!!)) {
            initFragments()
        } else {
            UIHelper.showAlertDialog1("Your current state doesn't match the state info you provided at the time of registration.", "Alert", { dialog, which ->
                sharedPreferenceManager!!.clearDB()
                clearAllActivitiesExceptThis(MainActivity::class.java)
            }, this)

        }*/
    }

    private fun setSideMenu(direction: Int) {
        resideMenu = ResideMenu(this)
        resideMenu?.setBackground(R.drawable.measurment_background)
        resideMenu?.attachToActivity(this@HomeActivity)
        resideMenu?.setScaleValue(0.56f)
        resideMenu?.setShadowVisible(false)
        setMenuItemDirection(direction)
    }

    private fun setMenuItemDirection(direction: Int) {
        if (direction == ResideMenu.DIRECTION_RIGHT) {
            rightSideMenuFragment = RightSideMenuFragment.newInstance()
            resideMenu?.addMenuItem(rightSideMenuFragment, "RightSideMenuFragment", direction)
        }
    }

    override val viewId: Int
        get() = R.layout.activity_home

    override val titlebarLayoutId: Int
        get() = R.id.titlebar

    override val drawerLayoutId: Int
        get() = R.id.drawer_layout

    override val dockableFragmentId: Int
        get() = R.id.contMain

    override val drawerFragmentId: Int
        get() = R.id.contDrawer

    private fun initFragments() {
        addDockableFragment(DashboardPagerFragment.newInstance(), false)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                drawerLayout!!.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
                val fragments = supportFragmentManager.fragments
                val fragment = fragments[fragments.size - 1] as BaseFragment
                fragment.setTitlebar(titleBar)
            }
        } else {
            moveTaskToBack(true)
        }
    }

    fun setBlurBackground() {}

    fun removeBlurImage() {
        blurImage!!.visibility = View.GONE
    }
}
