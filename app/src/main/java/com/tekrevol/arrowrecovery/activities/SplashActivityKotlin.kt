package com.tekrevol.arrowrecovery.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.mikhaellopez.rxanimation.RxAnimation
import com.mikhaellopez.rxanimation.fadeIn
import com.mikhaellopez.rxanimation.resize
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.AppConstants.KEY_IS_VERIFIED
import com.tekrevol.arrowrecovery.managers.SharedPreferenceManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivityKotlin : AppCompatActivity() {

    private val ANIMATIONS_DELAY = 0
    private val ANIMATIONS_TIME_OUT: Long = 3000
    private val composite = CompositeDisposable()
    var subscribe: Disposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //        contParentLayout.setVisibility(View.INVISIBLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //   val txtVersionNumber: TextView = findViewById(R.id.txtVersionNumber)

        try {
            val manager = packageManager
            val info = manager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            txtVersionNumber.text = "Build Version: " + info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            txtVersionNumber.text = ""
            e.printStackTrace()
        }



        if (SharedPreferenceManager.getInstance(applicationContext).currentUser == null) {

            Handler().postDelayed({
                //                        animateSplashLayout(true);
                SharedPreferenceManager.getInstance(applicationContext).putValue(AppConstants.KEY_IS_VERIFIED, "0")
                changeActivity(MainActivity::class.java)
            }, ANIMATIONS_DELAY.toLong())

        } else if (SharedPreferenceManager.getInstance(applicationContext).currentUser != null && SharedPreferenceManager.getInstance(applicationContext).getString(AppConstants.KEY_IS_VERIFIED).equals(1)) {
            Handler().postDelayed({

                changeActivity(HomeActivity::class.java)

            }, ANIMATIONS_DELAY.toLong())
        } else if (SharedPreferenceManager.getInstance(applicationContext).currentUser != null && SharedPreferenceManager.getInstance(applicationContext).getString(AppConstants.KEY_IS_VERIFIED).equals(0)) {
            Handler().postDelayed({
                SharedPreferenceManager.getInstance(applicationContext).putValue(AppConstants.KEY_IS_VERIFIED, "0")
                changeActivity(MainActivity::class.java)

            }, ANIMATIONS_DELAY.toLong())
        }


    }

    private fun changeActivity(activityClass: Class<*>) {
        RxAnimation.together(
                imgLogo.fadeIn(ANIMATIONS_TIME_OUT),
                //  imgLogo.rotation(360f, ANIMATIONS_TIME_OUT )
                imgLogo.resize(200, 200, ANIMATIONS_TIME_OUT)
        ).subscribe()



        Handler().postDelayed(/*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
                {
                    val i: Intent = Intent(this@SplashActivityKotlin, activityClass)
                    // This method will be executed once the timer is over
                    // Start your app main activity

                    startActivity(i)
                    overridePendingTransition(android.R.anim.fade_in, R.anim.fade_out)
                    // close this activity
                    finish()
                }, 1000 + ANIMATIONS_TIME_OUT.toLong())
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {


        }
    }


    override fun onDestroy() {

        super.onDestroy()

    }

    companion object {
        private val TAG = "SPLASH SCREEN"
    }
}