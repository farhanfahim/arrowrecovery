package com.tekrevol.arrowrecovery.fragments.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.TimeSelectorAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.enums.BaseURLTypes
import com.tekrevol.arrowrecovery.fragments.OrderDetailFragment
import com.tekrevol.arrowrecovery.helperclasses.GooglePlaceHelper
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.managers.DateManager
import com.tekrevol.arrowrecovery.managers.SharedPreferenceManager
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.IntWrapper
import com.tekrevol.arrowrecovery.models.SpinnerModel
import com.tekrevol.arrowrecovery.models.receiving_model.*
import com.tekrevol.arrowrecovery.models.sending_model.UpdateDeliveryCollectionCenterModel
import com.tekrevol.arrowrecovery.models.sending_model.UpdatePickupModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.AnyTextView
import kotlinx.android.synthetic.main.fragment_checkout_dialog.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class CheckoutDialogFragment : BottomSheetDialogFragment(), GooglePlaceHelper.GooglePlaceDataInterface, OnItemClickListener {

    private var orderProductModel: ArrayList<OrderProductModel> = ArrayList()
    private var arrCollectionModel: ArrayList<CollectionModel> = ArrayList()
    private var arrWorkingDay: ArrayList<Working_daysModel> = ArrayList()
    var pickupSelectedPos: IntWrapper = IntWrapper(0)
    var googlePlaceHelper: GooglePlaceHelper? = null
    private var arrData: ArrayList<DummyModel> = ArrayList()
    private var arrDate: ArrayList<String> = ArrayList()
    private var sharedPreferenceManager: SharedPreferenceManager? = null
    private lateinit var timeSelectorAdapter: TimeSelectorAdapter
    private var spinnerModelArrayList = java.util.ArrayList<SpinnerModel>()
    var latitudee: Double? = null
    var orderid: Int? = null
    var orderTotal: Double? = null
    var txtStr: String? = null
    var txtPick: String? = null
    var longitudee: Double? = null
    var param: Int = 0
    var idFromSpinner = 0

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    companion object {

        fun newInstance(orderProductModel: ArrayList<OrderProductModel>, arrCollectionModel: ArrayList<CollectionModel>, orderid: Int?, orderTotal: Double?): CheckoutDialogFragment {

            val args = Bundle()
            val fragment = CheckoutDialogFragment()
            fragment.orderProductModel = orderProductModel
            fragment.arrCollectionModel = arrCollectionModel
            fragment.orderid = orderid
            fragment.orderTotal = orderTotal
            fragment.arguments = args
            return fragment
        }
    }

    private fun getIdFromSpinner(checkoutDialogFragment: CheckoutDialogFragment): Int {
        for (category in checkoutDialogFragment.arrCollectionModel) {
            if (category.address.equals(checkoutDialogFragment.txtCollectionCenterLocation.getStringTrimmed())) {
                return category.getId()
            }
        }
        return -1
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
        sharedPreferenceManager = SharedPreferenceManager.getInstance(context)
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
        spinnerModelArrayList.clear()

        txtTotal.text = "$" + orderTotal.toString()
        for (collection in arrCollectionModel) {
            spinnerModelArrayList.add(SpinnerModel(collection.address.trim()))
        }

        rvTimeSelection.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvTimeSelection.adapter = timeSelectorAdapter

    }

    private fun setListener() {

        rgShipping.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbPickup -> {

                    param = 1
                    contPickupSelected.visibility = View.VISIBLE
                    contCollectionCenter.visibility = View.GONE
                    txtCollectionCenterLocation.text = ""
                    txtPickupLocation.text = ""
                    txtDate.text = ""
                    heading.visibility = View.GONE
                    map.visibility = View.GONE
                    getPickUpTime()
                }
                R.id.rbCollectionCenter -> {

                    param = 2
                    txtDate.text = ""
                    contPickupSelected.visibility = View.GONE
                    contCollectionCenter.visibility = View.VISIBLE
                    heading.visibility = View.GONE
                    txtCollectionCenterLocation.text = ""
                    txtPickupLocation.text = ""
                    map.visibility = View.GONE
                    arrData.clear()
                    txtPickup.visibility = View.GONE
                    timeSelectorAdapter.notifyDataSetChanged()

                }
            }
        }

        contDate.setOnClickListener {

            if (rbPickup.isChecked || rbCollectionCenter.isChecked) {

                if (param.equals(1)) {
                    if (txtPickupLocation.stringTrimmed.isEmpty()) {
                        UIHelper.showAlertDialog(context, "Please select location")
                        return@setOnClickListener
                    }
                    DateManager.showDatePicker(context, txtDate, null, false, true)
                } else {
                    if (txtCollectionCenterLocation.stringTrimmed.isEmpty()) {
                        UIHelper.showAlertDialog(context, "Please select location")
                        return@setOnClickListener
                    }
                    DateManager.showDatePicker(context, txtDate, { view, year, month, dayOfMonth ->
                        getSlot(txtDate)
                    }, false, true)
                }

            } else {
                UIHelper.showAlertDialog(context, "Please select type pickup or delivered to collection center")
                return@setOnClickListener
            }

        }

        contCollectionCenter.setOnClickListener {
            contPickupSelected.visibility = View.GONE
            contCollectionCenter.visibility = View.VISIBLE
            heading.visibility = View.GONE
            txtCollectionCenterLocation.text = ""
            txtPickupLocation.text = ""
            map.visibility = View.GONE
            arrData.clear()
            txtDate.text = ""
            txtPickup.visibility = View.GONE
            timeSelectorAdapter.notifyDataSetChanged()

            UIHelper.showSpinnerDialog(fragmentManager, spinnerModelArrayList, "Select Location", txtCollectionCenterLocation, null, {
                spinnerModelArrayList.filter { it.isSelected }.firstOrNull()?.let {
                    if (txtCollectionCenterLocation.text.isNotEmpty()) {
                        txtStr = txtCollectionCenterLocation.text.toString()
                        getMap(it)
                    }
                }

                if (txtCollectionCenterLocation.text.equals("")) {
                    contPickupSelected.visibility = View.GONE
                    contCollectionCenter.visibility = View.VISIBLE
                    heading.visibility = View.GONE
                    txtCollectionCenterLocation.text = ""
                    txtPickupLocation.text = ""
                    map.visibility = View.GONE
                    arrData.clear()
                    txtDate.text = ""
                    txtPickup.visibility = View.GONE
                    timeSelectorAdapter.notifyDataSetChanged()

                }
            }, pickupSelectedPos)
        }

        imgMap.setOnClickListener(View.OnClickListener {
            Log.d("map", "map")
            latitudee?.let { it1 -> longitudee?.let { it2 -> GooglePlaceHelper.intentOpenMap(context, it1, it2, "testing") } }
        })

        contPickupSelected.setOnClickListener {

            sleepThread()
            //  contPickupSelected.setClickable(false)
            txtCollectionCenterLocation.text = ""
            txtPickupLocation.text = ""
            txtDate.text = ""
            heading.visibility = View.GONE
            map.visibility = View.GONE
            googlePlaceHelper = GooglePlaceHelper(activity, GooglePlaceHelper.PLACE_PICKER, this, this, true)
            googlePlaceHelper?.openMapsActivity()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnPlaceOrder.setOnClickListener {

            if (rbPickup.isChecked || rbCollectionCenter.isChecked) {
            } else {
                UIHelper.showAlertDialog(context, "Please select type pickup or delivered to collection center")
                return@setOnClickListener
            }

            if (param == 1) {

                if (txtPickupLocation.stringTrimmed.isEmpty()) {
                    UIHelper.showAlertDialog(context, "Please select location")
                    return@setOnClickListener
                }

                if (txtDate.stringTrimmed.isEmpty()) {
                    UIHelper.showAlertDialog(context, "Please select date")
                    return@setOnClickListener
                }
                if (txtPick.isNullOrBlank()) {
                    UIHelper.showAlertDialog(context, "Please select time slot")
                    return@setOnClickListener
                }

                UIHelper.showAlertDialog("Are you sure you want to place this order?", "Place Order", { dialog, which ->
                    placeOrder(dialog)
                }, context)

            } else if (param == 2) {

                if (txtCollectionCenterLocation.stringTrimmed.isEmpty()) {
                    UIHelper.showAlertDialog(context, "Please select location")
                    return@setOnClickListener
                }

                if (txtDate.stringTrimmed.isEmpty()) {
                    UIHelper.showAlertDialog(context, "Please select date")
                    return@setOnClickListener
                }
                if (txtPick.isNullOrBlank()) {
                    UIHelper.showAlertDialog(context, "Please select time slot")
                    return@setOnClickListener
                }
                idFromSpinner = getIdFromSpinner(this)

                if (idFromSpinner == -1) {
                    UIHelper.showShortToastInCenter(context, "Invalid ID")
                    return@setOnClickListener
                }

                UIHelper.showAlertDialog("Are you sure you want to place this order?", "Place Order", { dialog, which ->
                    placeOrder(dialog)
                }, context)
            }

        }
    }

    private fun getSlot(txtDate: AnyTextView) {

        idFromSpinner = getIdFromSpinner(this)

        if (idFromSpinner == -1) {
            UIHelper.showShortToastInCenter(context, "Invalid ID")
            return
        }

        // Log.d("txtDate", txtDate.stringTrimmed.toString())

        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_COLLECTION_ID] = idFromSpinner
        queryMap[WebServiceConstants.Q_DATE] = txtDate.stringTrimmed.toString()

        WebServices(activity, sharedPreferenceManager?.currentUser?.accessToken, BaseURLTypes.BASE_URL, true).getAPIAnyObject(WebServiceConstants.PATH_GETAVAILABILITY, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                arrDate.clear()
                arrData.clear()
                val dateModel: DateModels = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , DateModels::class.java)

                if (!(txtDate.text.equals(dateModel.date))) {
                    txtDate.text = dateModel.date
                    UIHelper.showAlertDialog(context, "The slots are available on " + dateModel.date)
                }

                arrDate.addAll(dateModel.slots)
                for (arr in arrDate) {
                    arrData.add((DummyModel(arr, false)))
                }
                txtPickup.visibility = View.VISIBLE
                txtToltalSlot.text = "(" + arrData.size.toString() + " Slots availables)"
                timeSelectorAdapter.notifyDataSetChanged()
            }

            override fun onError(`object`: Any?) {
            }
        })

    }

    private fun placeOrder(dialog: DialogInterface) {
        if (param == 1) {

            val updateOrderModel = UpdatePickupModel()
            updateOrderModel.deliveryDate = txtDate.stringTrimmed
            updateOrderModel.deliveryMode = AppConstants.PICKUP
            updateOrderModel.timeSlot = txtPick
            updateOrderModel.address = txtPickupLocation.stringTrimmed

            WebServices(activity, sharedPreferenceManager?.currentUser?.accessToken, BaseURLTypes.BASE_URL, true).putMultipartAPI(WebServiceConstants.PATH_ORDERS + "/" + orderid, null,
                    updateOrderModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
                override fun requestDataResponse(webResponse: WebResponse<Any?>?) {
                    val orderModel: OrderModel = GsonFactory.getSimpleGson()
                            .fromJson(GsonFactory.getSimpleGson().toJson(webResponse?.result)
                                    , OrderModel::class.java)
                    dialog.dismiss()
                    UIHelper.showToast(context, "Order Placed Successfully")
                    dismiss()

                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.contMain, OrderDetailFragment.newInstance(orderModel)).addToBackStack(null).commit()

                    // clearAllActivitiesExceptThis(HomeActivity::class.java)
                }

                override fun onError(`object`: Any?) {}
            })

        } else if (param == 2) {

            val updateOrderModel = UpdateDeliveryCollectionCenterModel()
            updateOrderModel.deliveryDate = txtDate.stringTrimmed
            updateOrderModel.deliveryMode = AppConstants.DELIVERED
            updateOrderModel.timeSlot = txtPick
            updateOrderModel.collectionCenterId = idFromSpinner
            WebServices(activity, sharedPreferenceManager?.currentUser?.accessToken, BaseURLTypes.BASE_URL, true).putMultipartAPI(WebServiceConstants.PATH_ORDERS + "/" + orderid, null,
                    updateOrderModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
                override fun requestDataResponse(webResponse: WebResponse<Any?>?) {
                    val orderModel: OrderModel = GsonFactory.getSimpleGson()
                            .fromJson(GsonFactory.getSimpleGson().toJson(webResponse?.result)
                                    , OrderModel::class.java)
                    dialog.dismiss()
                    UIHelper.showToast(context, "OrderModel Placed Successfully")
                    dismiss()
                    // clearAllActivitiesExceptThis(HomeActivity::class.java)
                    activity!!.supportFragmentManager.beginTransaction().replace(R.id.contMain, OrderDetailFragment.newInstance(orderModel)).addToBackStack(null).commit()
                }

                override fun onError(`object`: Any?) {}
            })
        }

    }

    private fun getMap(it: SpinnerModel) {

        for (arr in arrCollectionModel) {

            if (it.text.equals(arr.address.trim())) {
                latitudee = arrCollectionModel[it.positionInList].latitude
                longitudee = arrCollectionModel[it.positionInList].longitude
                var str: String = GooglePlaceHelper.getMapSnapshotURL(arrCollectionModel[it.positionInList].latitude, arrCollectionModel[it.positionInList].longitude)
                ImageLoaderHelper.loadImageWithAnimations(imgMap, str, false)
                heading.visibility = View.VISIBLE
                map.visibility = View.VISIBLE
            }
        }

    }

    private fun getPickUpTime() {

        val mquery: Map<String, Any> = HashMap()
        WebServices(activity, sharedPreferenceManager?.currentUser?.accessToken, BaseURLTypes.BASE_URL, true).getAPIAnyObject(WebServiceConstants.PATH_SLOTS, mquery, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                arrDate.clear()
                arrData.clear()
                arrDate.addAll(webResponse.result as Collection<String>)
                for (arr in arrDate) {
                    arrData.add((DummyModel(arr, false)))
                }
                txtPickup.visibility = View.VISIBLE
                txtToltalSlot.text = "(" + arrData.size.toString() + " Slots availables)"
                timeSelectorAdapter.notifyDataSetChanged()
            }

            override fun onError(`object`: Any?) {
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googlePlaceHelper?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onError(error: String?) {
    }

    override fun onPlaceActivityResult(longitude: Double, latitude: Double, locationName: String?) {

        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.size > 0) {
                val state = addresses[0].adminArea
                if (state != null) {
                    if (((sharedPreferenceManager?.currentUser?.userDetails?.state)?.equals(state)!!) || ((sharedPreferenceManager?.currentUser?.userDetails?.state)?.equals(state)!!)) {
                        txtPickupLocation.text = locationName
                        latitudee = latitude
                        longitudee = longitude
                        var str: String = GooglePlaceHelper.getMapSnapshotURL(latitude, longitude)
                        ImageLoaderHelper.loadImageWithAnimations(imgMap, str, false)
                        heading.visibility = View.VISIBLE
                        map.visibility = View.VISIBLE
                    } else {
                        UIHelper.showAlertDialog(context, "Your current state doesn't match the state info you provided at the time of registration.")
                    }
                } else {
                    UIHelper.showAlertDialog(context, "Your current state doesn't match the state info you provided at the time of registration.")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onItemClick(position: Int, anyObject: Any?, view: View?, type: String?) {
        arrData.forEach { it.isSelected = false }
        arrData[position].isSelected = true
        txtPick = arrData[position].text
        timeSelectorAdapter.notifyDataSetChanged()
    }

    fun clearAllActivitiesExceptThis(cls: Class<*>?) {
        val intents = Intent(context, cls)
        intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intents)
        activity?.finish()
    }

    /*override fun onResume() {
        super.onResume()
        contPickupSelected.setClickable(true)

    }*/

    private fun sleepThread() {
        contPickupSelected.setEnabled(false)
        val handler = Handler()
        handler.postDelayed({
            if (contPickupSelected != null) {
                contPickupSelected.setEnabled(true)
            }
        }, 3000)
    }

}