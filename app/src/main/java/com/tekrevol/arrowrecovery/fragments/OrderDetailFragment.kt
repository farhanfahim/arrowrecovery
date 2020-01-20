package com.tekrevol.arrowrecovery.fragments

import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.OrderDetailShimmerAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.receiving_model.Order
import com.tekrevol.arrowrecovery.models.receiving_model.OrderProduct
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_converter_dashboard.*
import kotlinx.android.synthetic.main.fragment_orderdetail.*
import retrofit2.Call
import android.graphics.Bitmap
import android.widget.LinearLayout
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.libraries.places.internal.jf.e
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.System.out
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderDetailFragment : BaseFragment(), OnItemClickListener{


    private var arrData: ArrayList<OrderProduct> = ArrayList()
    var webCall: Call<WebResponse<Any>>? = null
    private lateinit var myOrderAdapter: OrderDetailShimmerAdapter
    private lateinit var linearLayoutOrderDetail: LinearLayout
    private lateinit var myBitmap: Bitmap

    var pos :Int = 0
    var model: String? = null
    var order: Order? = null

    companion object {

        fun newInstance(order: Order ,position: Int): OrderDetailFragment {

            val args = Bundle()

            val fragment = OrderDetailFragment()
            fragment.setArguments(args)
            fragment.order = order
            fragment.pos = position
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myOrderAdapter = OrderDetailShimmerAdapter(context!!, arrData, this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutOrderDetail = view.findViewById(R.id.linearLayoutOrderDetail)
        onBind()

    }



    private fun onBind() {


        recyclerViewOrderDetail.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        (recyclerViewOrderDetail.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
//        val resId = R.anim.layout_animation_fall_bottom
//        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
//        recyclerViewOrderDetail.layoutAnimation = animation
        recyclerViewOrderDetail.adapter = myOrderAdapter
        recyclerViewOrderDetail.setItemViewType({ type: Int, position: Int -> R.layout.shimmer_item_order })

        getOrderProducts(order!!.id)

        txtName.text = (order?.userModel!!.userDetails.fullName)
        txtAddress.text = (order?.userModel!!.userDetails.address)
        txtPhone.text = (order?.userModel!!.userDetails.phone)

        if (order!!.status == AppConstants.STATUS_RECEIVED){
            txtStatus!!.text = "Received"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.green_bg))
        }
        if (order!!.status == AppConstants.STATUS_DELIVERED){
            txtStatus!!.text = "Delivered"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.green_bg))
        }
        if (order!!.status == AppConstants.STATUS_VERIFIED){
            txtStatus!!.text = "verified"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.green_bg))
        }

        if (order!!.status == AppConstants.STATUS_PAID){
            txtStatus!!.text = "Paid"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.green_bg))
        }

        if (order!!.status == AppConstants.STATUS_COMPLETED){
            txtStatus!!.text = "Completed"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.green_bg))
        }

        if (order!!.status == AppConstants.STATUS_RETURNED){
            txtStatus!!.text = "Rejected"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.red_bg))
        }

        if (order!!.status == AppConstants.STATUS_CART){
            txtStatus!!.text = "Pending"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.fbutton_color_sun_flower))
        }
        if (order!!.status == AppConstants.STATUS_CART){
            txtStatus!!.text = "In Cart"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.fbutton_color_sun_flower))
        }


        if (order!!.amount == (0)){

            txtAmountStatus.text = "Amount"
            txtTotalPrice.text = "$"+0
        }else{
            txtAmountStatus.text = "Amount Paid"
            txtTotalPrice.text = "$"+order!!.amount.toString()
        }
        txtEstimatedAmount.text = "$"+order!!.estimatedAmount.toString()


    }


    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_orderdetail
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.resetViews()
        titleBar.visibility = View.VISIBLE
        titleBar.hide()
        titleBar.showBackButton(activity)

        titleBar.setTitle("Order Detail")

    }

    override fun setListeners() {
 /*       backButtonorder.setOnClickListener(View.OnClickListener {
            baseActivity.popBackStack()

        })*/



        btnScreenShot.setOnClickListener {
            myBitmap = getScreenShot(linearLayoutOrderDetail)
            saveImageInStorage(myBitmap)
        }

    }


    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, type: String?) {

    }



    private fun getOrderProducts(orderId: Int) {

        recyclerViewOrderDetail.showShimmer()
        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_ORDER_ID] = orderId
        webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_ORDERPRODUCTS, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : TypeToken<java.util.ArrayList<OrderProduct?>?>() {}.type
                val arrayList: java.util.ArrayList<OrderProduct> = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type)

                recyclerViewOrderDetail.hideShimmer()
                arrData.clear()
                arrData.addAll(arrayList)
                myOrderAdapter.notifyDataSetChanged()

            }

            override fun onError(`object`: Any?) {
                if (recyclerViewOrderDetail == null) {
                    recyclerViewOrderDetail.hideShimmer()
                    return
                }
            }
        })
    }




    private fun getScreenShot(view: View): Bitmap {

        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null)
            bgDrawable.draw(canvas)
        else canvas.drawColor(Color.BLUE)
        view.draw(canvas)
        Toast.makeText(context, "screenshot captured", Toast.LENGTH_LONG).show()
        return returnedBitmap
        }

    private fun saveImageInStorage(bitmap: Bitmap) {
        val root: String = Environment.getExternalStorageDirectory().absolutePath
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var currentDateTime:String = dateFormat.format( Date())
        val fileName = "$currentDateTime.jpg"
        val file = File(myDir, fileName)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

//    private fun saveImageInStorage(bitmap:Bitmap) {
//        val externalStorageState = Environment.getExternalStorageState()
//        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)){
//            var externalStorage = Environment.getExternalStorageDirectory().toString()
//            val file = File("$externalStorage/saved_images")
//            try {
//                val stream:OutputStream = FileOutputStream(file)
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
//                out.flush()
//                out.close()
//                Toast.makeText(context, "save", Toast.LENGTH_LONG).show()
//            }catch (e:Exception){
//                e.printStackTrace()
//            }
//
//        }else{
//            Toast.makeText(context, "error", Toast.LENGTH_LONG).show()
//        }
//
//    }




}



