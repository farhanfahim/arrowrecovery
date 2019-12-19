package com.tekrevol.arrowrecovery.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.managers.SharedPreferenceManager


class SplashActivityKotlin : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 2000
    private val ANIMATIONS_DELAY = 2000
    private val ANIMATIONS_TIME_OUT = 250
    private val FADING_TIME = 500
    private val hasAnimationStarted = false

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

            //   txtVersionNumber.setText("Build Version: " + info.versionName)
        } catch (e: PackageManager.NameNotFoundException) {
            //    txtVersionNumber.setText("")
            e.printStackTrace()
        }


    }

    private fun changeActivity(activityClass: Class<*>) {
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
                }, ANIMATIONS_TIME_OUT.toLong())
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {

            if (SharedPreferenceManager.getInstance(applicationContext).currentUser == null) {

                Handler().postDelayed({
                    //                        animateSplashLayout(true);
                    changeActivity(MainActivity::class.java)
                }, ANIMATIONS_DELAY.toLong())

            } else {
                Handler().postDelayed({
                    changeActivity(HomeActivity::class.java)
                    //                        animateSplashLayout(false);
                }, ANIMATIONS_DELAY.toLong())
            }
        }
    }


    override fun onDestroy() {

        super.onDestroy()

    }

    companion object {
        private val TAG = "SPLASH SCREEN"
    }
}