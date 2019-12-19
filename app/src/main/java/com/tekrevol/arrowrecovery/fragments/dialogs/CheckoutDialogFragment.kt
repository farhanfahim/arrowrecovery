package com.tekrevol.arrowrecovery.fragments.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.DaysSelectorAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.TimeSelectorAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.helperclasses.GooglePlaceHelper
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.DateManager
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.IntWrapper
import kotlinx.android.synthetic.main.fragment_checkout_dialog.*
import kotlinx.android.synthetic.main.fragment_home.*

class CheckoutDialogFragment : BottomSheetDialogFragment(), GooglePlaceHelper.GooglePlaceDataInterface, OnItemClickListener {

    var pickupSelectedPos: IntWrapper = IntWrapper(0)
    var googlePlaceHelper: GooglePlaceHelper? = null
    private var arrData: ArrayList<DummyModel> = ArrayList()
    private lateinit var timeSelectorAdapter: TimeSelectorAdapter

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timeSelectorAdapter = TimeSelectorAdapter(context!!, arrData, this)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_checkout_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBind()
        setListener()
    }

    private fun onBind() {
        arrData.clear()
        arrData.addAll(Constants.timeSelector())

        rvTimeSelection.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvTimeSelection.adapter = timeSelectorAdapter

        timeSelectorAdapter.notifyDataSetChanged()
    }

    private fun setListener() {

        rgShipping.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbPickup -> {
                    contPickupSelected.visibility = View.VISIBLE
                    contCollectionCenter.visibility = View.GONE
                }
                R.id.rbCollectionCenter -> {
                    contPickupSelected.visibility = View.GONE
                    contCollectionCenter.visibility = View.VISIBLE
                }
            }
        }

        contDate.setOnClickListener {
            DateManager.showDatePicker(context, txtDate, null, false, true)
        }

        contPickupSelected.setOnClickListener {
            UIHelper.showSpinnerDialog(fragmentManager, Constants.locationSelector(), "Select Location", txtPickupLocation, null, {}, pickupSelectedPos)
        }

        contCollectionCenter.setOnClickListener {
            googlePlaceHelper = GooglePlaceHelper(activity, GooglePlaceHelper.PLACE_PICKER, this, this, true)
            googlePlaceHelper?.openMapsActivity()
        }

        btnCancel.setOnClickListener{
            dismiss()
        }

        btnPlaceOrder.setOnClickListener {
            UIHelper.showAlertDialog( "Are you sure you want to place this order?","Place Order", { dialog, which ->
                dialog.dismiss()
                UIHelper.showToast(context, "Order Placed Successfully")
                this@CheckoutDialogFragment.dismiss()
            }, context)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googlePlaceHelper?.onActivityResult(requestCode, resultCode, data)

    }

    override fun onError(error: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPlaceActivityResult(longitude: Double, latitude: Double, locationName: String?) {
        txtCollectionCenterLocation.text = locationName
    }

    override fun onItemClick(position: Int, anyObject: Any?, view: View?, type: String?) {
        arrData.forEach { it.isSelected = false }
        arrData[position].isSelected = true
        timeSelectorAdapter.notifyDataSetChanged()
    }
}