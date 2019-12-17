package com.tekrevol.arrowrecovery.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.fragments.DashboardPagerFragment
import com.tekrevol.arrowrecovery.fragments.DashboardPagerFragment.Companion.newInstance
import com.tekrevol.arrowrecovery.fragments.RightSideMenuFragment
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.libraries.residemenu.ResideMenu
import com.tekrevol.arrowrecovery.utils.utility.Blur
import com.tekrevol.arrowrecovery.utils.utility.Utils

class HomeActivity : BaseActivity() {
    var navigationView: NavigationView? = null
    var contMain: FrameLayout? = null
    var contParentActivityLayout: RelativeLayout? = null
    var rightSideMenuFragment: RightSideMenuFragment? = null
        private set
    var resideMenu: ResideMenu? = null
        private set
    //For Blurred Background
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
        //        setContentView(R.layout.activity_main);
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val intentData = intent.getStringExtra(AppConstants.JSON_STRING_KEY)
        navigationView = findViewById(R.id.nav_view)
        navigationView?.background?.setColorFilter(-0x80000000, PorterDuff.Mode.MULTIPLY)
        contMain = findViewById(R.id.contMain)
        contParentActivityLayout = findViewById(R.id.contParentActivityLayout)
        blurImage = findViewById(R.id.imageBlur)
        setSideMenu(ResideMenu.DIRECTION_RIGHT)
        initFragments()
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