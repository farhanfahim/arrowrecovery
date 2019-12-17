package com.tekrevol.arrowrecovery.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.GravityCompat
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.fragments.RegisterPagerFragment
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.RunTimePermissions
import com.tekrevol.arrowrecovery.managers.SharedPreferenceManager
import kotlinx.android.synthetic.main.fragment_register.*

class MainActivity : BaseActivity() {
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        setContentView(R.layout.activity_main);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
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
        if (SharedPreferenceManager.getInstance(applicationContext).currentUser == null) addDockableFragment(RegisterPagerFragment.newInstance(), false) else {
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
}