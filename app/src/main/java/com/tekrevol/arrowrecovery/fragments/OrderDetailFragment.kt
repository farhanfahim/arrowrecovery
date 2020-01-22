package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
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
import com.tekrevol.arrowrecovery.models.receiving_model.OrderModel
import com.tekrevol.arrowrecovery.models.receiving_model.OrderProductModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_orderdetail.*
import retrofit2.Call
import android.widget.LinearLayout
import android.os.Environment
import android.print.PrintAttributes
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.libraries.htmltopdf.CreatePdf
import com.tekrevol.arrowrecovery.managers.FileManager
import com.tekrevol.arrowrecovery.managers.FileManager.openFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderDetailFragment : BaseFragment(), OnItemClickListener {


    private var arrData: ArrayList<OrderProductModel> = ArrayList()
    var webCall: Call<WebResponse<Any>>? = null
    private lateinit var myOrderAdapter: OrderDetailShimmerAdapter
    private lateinit var linearLayoutOrderDetail: LinearLayout
    var path: String = ""
    lateinit var folder: File

    var pos: Int = 0
    var model: String? = null
    var orderModel: OrderModel? = null

    companion object {

        fun newInstance(orderModel: OrderModel, position: Int): OrderDetailFragment {

            val args = Bundle()

            val fragment = OrderDetailFragment()
            fragment.arguments = args
            fragment.orderModel = orderModel
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

        getOrderProducts(orderModel!!.id)

        txtName.text = (orderModel?.userModel!!.userDetails.fullName)
        txtAddress.text = (orderModel?.userModel!!.userDetails.address)
        txtPhone.text = (orderModel?.userModel!!.userDetails.phone)

        if (orderModel!!.status == AppConstants.STATUS_RECEIVED) {
            txtStatus!!.text = "Received"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.green_bg))
        }
        if (orderModel!!.status == AppConstants.STATUS_ORDERED) {
            txtStatus!!.text = "Ordered"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.green_bg))
        }
        if (orderModel!!.status == AppConstants.STATUS_DELIVERED) {
            txtStatus!!.text = "Delivered"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.green_bg))
        }
        if (orderModel!!.status == AppConstants.STATUS_VERIFIED) {
            txtStatus!!.text = "verified"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.green_bg))
        }

        if (orderModel!!.status == AppConstants.STATUS_PAID) {
            txtStatus!!.text = "Paid"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.green_bg))
        }

        if (orderModel!!.status == AppConstants.STATUS_COMPLETED) {
            txtStatus!!.text = "Completed"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.green_bg))
        }

        if (orderModel!!.status == AppConstants.STATUS_RETURNED) {
            txtStatus!!.text = "Rejected"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.red_bg))
        }

        if (orderModel!!.status == AppConstants.STATUS_CART) {
            txtStatus!!.text = "Pending"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.fbutton_color_sun_flower))
        }
        if (orderModel!!.status == AppConstants.STATUS_CART) {
            txtStatus!!.text = "In Cart"
            txtStatus!!.setTextColor(ContextCompat.getColor(context!!, R.color.fbutton_color_sun_flower))
        }


        if (orderModel!!.amount == (0)) {

            txtAmountStatus.text = "Amount"
            txtTotalPrice.text = "$" + 0
        } else {
            txtAmountStatus.text = "Amount Paid"
            txtTotalPrice.text = "$" + orderModel!!.amount.toString()
        }
        txtEstimatedAmount.text = "$" + orderModel!!.estimatedAmount.toString()


        path = Environment.getExternalStorageDirectory().path + "/ArrowRecovery/"
        folder = File(path)

        if (!folder.exists()) {
            folder.mkdirs()
        }


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



        btnSavePdf.setOnClickListener {
            savePdfFile(path)
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

                val type = object : TypeToken<java.util.ArrayList<OrderProductModel?>?>() {}.type
                val arrayList: java.util.ArrayList<OrderProductModel> = GsonFactory.getSimpleGson()
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


    private fun savePdfFile(path: String) {


        var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var currentDateTime: String = dateFormat.format(Date())
        val fileName = "$currentDateTime"
        Toast.makeText(context, "PDF is generating", Toast.LENGTH_SHORT).show()
        CreatePdf(context!!, orderModel!!.invoiceUrl)
                .setPdfName(fileName)
                .openPrintDialog(false)
                .setContentBaseUrl(null)
                .setPageSize(PrintAttributes.MediaSize.ISO_A4)
                .setContent("Your Content")
                .setFilePath(path)
                .setCallbackListener(object : CreatePdf.PdfCallbackListener {
                    override fun onFailure(errorMsg: String) {
                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                    }

                    override fun onSuccess(filePath: String) {
                        val snack = Snackbar.make(view, "PDF Saved", 10000)
                        snack.setAction("View PDF") {
                            if (FileManager.isFileExits(filePath)) {
                                openFile(context, File(filePath))
                            }
                        }
                        snack.show()


                    }
                })
                .create()


    }


}



