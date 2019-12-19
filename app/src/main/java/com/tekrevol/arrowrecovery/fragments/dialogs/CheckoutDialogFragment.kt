package com.tekrevol.arrowrecovery.fragments.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tekrevol.arrowrecovery.R

class CheckoutDialogFragment: BottomSheetDialogFragment() {

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        //Set the custom view
        //Set the custom view
        val view: View = LayoutInflater.from(context).inflate(R.layout.fragment_checkout_dialog, null)
        dialog.setContentView(view)

        val params = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams

        // this is for full screen height
        // this is for full screen height
        params.height = getScreenHeight()

        val behavior = params.behavior

        if (behavior is BottomSheetBehavior<*>) {
            behavior.setBottomSheetCallback(object : BottomSheetCallback() {
                @SuppressLint("SwitchIntDef")
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    var state = ""
                    when (newState) {
                        BottomSheetBehavior.STATE_DRAGGING -> {
                            state = "DRAGGING"
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                            state = "SETTLING"
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            state = "EXPANDED"
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            state = "COLLAPSED"
                        }
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            dismiss()
                            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            state = "HIDDEN"
                        }
                    }
                    //                    Toast.makeText(getContext(), "Bottom Sheet State Changed to: " + state, Toast.LENGTH_SHORT).show();
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })
        }
    }
}