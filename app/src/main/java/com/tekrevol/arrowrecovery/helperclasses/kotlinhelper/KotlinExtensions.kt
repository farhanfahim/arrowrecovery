package com.tekrevol.arrowrecovery.helperclasses.kotlinhelper

import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.fragment_cart.*

/**
 * KOTLIN Scripts class, use this class to write KOTLIN scripts only to perform data or UI operation.
 * Make sure to not override others work without their permission.
 * You may add your new method
 */


fun View?.disableClick(time: Long = 2000) {
    this?.let {
        it.isEnabled = false
        val handler = Handler()
        handler.postDelayed({
            it.isEnabled = true
        }, time)
    }

}




