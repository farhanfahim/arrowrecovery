package com.tekrevol.arrowrecovery.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingDelegate
import com.tekrevol.arrowrecovery.adapters.recyleradapters.MyCartAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.fragments.dialogs.CheckoutDialogFragment
import com.tekrevol.arrowrecovery.helperclasses.GooglePlaceHelper
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.receiving_model.CollectionModel
import com.tekrevol.arrowrecovery.models.receiving_model.Order
import com.tekrevol.arrowrecovery.models.receiving_model.OrderProduct
import com.tekrevol.arrowrecovery.models.sending_model.UpdateCartModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import com.todkars.shimmer.ShimmerAdapter.ItemViewType
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_converter_dashboard.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

class CartFragment : BaseFragment(), OnItemClickListener, PagingDelegate.OnPageListener, GooglePlaceHelper.GooglePlaceDataInterface {

    private var arrData: ArrayList<OrderProduct> = ArrayList()
    private var arrDataCart: ArrayList<CollectionModel> = ArrayList()
    private lateinit var cartAdapter: MyCartAdapter
    var webCall: Call<WebResponse<Any>>? = null
    var webCallDelete: Call<WebResponse<Any>>? = null
    var webCallUpdate: Call<WebResponse<Any>>? = null
    var orderid: Int? = null
    var quantity: Int? = null
    var latitudee: Double? = null
    var longitudee: Double? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartAdapter = MyCartAdapter(context!!, arrData, this)

    }

    companion object {

        fun newInstance(): CartFragment {

            val args = Bundle()

            val fragment = CartFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBind()
    }

    private fun onBind() {
        arrData.clear()
        arrDataCart.clear()
        myCartApi()
        val mLayoutManager1: RecyclerView.LayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerViewCart.layoutManager = mLayoutManager1
        (recyclerViewCart.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        val pagingDelegate: PagingDelegate = PagingDelegate.Builder(cartAdapter)
                .attachTo(recyclerViewCart)
                .listenWith(this@CartFragment)
                .build()
        recyclerViewCart.adapter = cartAdapter
        recyclerViewCart.setItemViewType(ItemViewType { type: Int, position: Int -> R.layout.shimmer_item_categories })

    }

    private fun myCartApi() {

        recyclerViewCart.showShimmer()
        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_WITH_ORDER_PRODUCTS] = 1
        queryMap[WebServiceConstants.Q_PARAM_STATUS] = 10
        webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_ORDERS, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                if (webResponse.result != null) {
                    val order: Order = GsonFactory.getSimpleGson()
                            .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                    , Order::class.java)

                    recyclerViewCart.hideShimmer()
                    orderid = order.id
                    txtTotalPrice.text = "Total: " + order.amount
                    arrData.clear()
                    arrData.addAll(order.orderProducts)
                    cartAdapter.notifyDataSetChanged()
                }

            }

            override fun onError(`object`: Any?) {
                if (rvConverters == null) {
                    recyclerViewCart.hideShimmer()
                    return
                }
            }
        })
    }


    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_cart
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {
        btnCheckout.setOnClickListener {

            getCollectionSelector()

        }

        cbSelectAll.setOnCheckedChangeListener { buttonView, isChecked ->
            arrData.forEach { it.isSelected = isChecked }
            cartAdapter.notifyDataSetChanged()
        }

        btnDelete.setOnClickListener {
            UIHelper.showAlertDialog("Are you sure you want to delete selected Items?", "Delete Items", { dialog, which ->
                deleteProduct(dialog)
            }, context)
        }
    }

    private fun getCollectionSelector() {


        if (!arrData.isEmpty()) {


            val queryMap = HashMap<String, Any>()
            queryMap[WebServiceConstants.Q_LAT] = latitudee.toString()
            queryMap[WebServiceConstants.Q_LONG] = longitudee.toString()

            webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_COLLECTIONCENTER, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
                override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                    val type = object : TypeToken<java.util.ArrayList<CollectionModel?>?>() {}.type
                    val arrayList: java.util.ArrayList<CollectionModel> = GsonFactory.getSimpleGson()
                            .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                    , type)

                    if (webResponse.isSuccess) {
                        arrDataCart.addAll(arrayList)
                        val checkoutDialogFragment = CheckoutDialogFragment.newInstance(arrData, arrDataCart)
                        checkoutDialogFragment.show(baseActivity.supportFragmentManager, "CheckoutDialogFragment")
                    }
                }

                override fun onError(`object`: Any?) {

                }
            })
        }

    }

    private fun deleteProduct(dialog: DialogInterface) {

        val map = arrData.filter { it.isSelected }.map { it.id }

        webCallDelete = getBaseWebServices(false).deleteAPIAnyObject(WebServiceConstants.PATH_ORDERPRODUCTS + "/" + map, "", object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                UIHelper.showToast(context, webResponse.message)
                arrData.removeAll { it.isSelected }
                cartAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onError(`object`: Any?) {

            }
        })


    }


    private fun deleteAllApi(dialog: DialogInterface) {

        webCallDelete = getBaseWebServices(false).deleteAPIAnyObject(WebServiceConstants.PATH_ORDERS + "/" + orderid, "", object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {
                UIHelper.showToast(context, webResponse.message)
                arrData.removeAll { it.isSelected }
                cartAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onError(`object`: Any?) {

            }
        })

    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, type: String?) {
        when (view?.id) {
            R.id.imgSelect -> {
                arrData[position].isSelected = !arrData[position].isSelected
                cartAdapter.notifyDataSetChanged()
            }
            R.id.contSelectQuality -> {
                UIHelper.showCheckedDialogBox(context, "Select Quality", Constants.qualities, 0) { dialog, which ->
                    //                    val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
//                    txtQuality.text = Constants.qualities[selectedPosition]
                    dialog.dismiss()
                }
            }
            R.id.btnSubtract -> {
                updateQuantity(position, "sub")
            }

            R.id.btnAdd -> {
                updateQuantity(position, "add")
            }
        }
    }

    private fun updateQuantity(position: Int, addorSub: String) {

        when (addorSub) {
            "add" -> {
                quantity = arrData[position].quantity
                if (quantity!! < 999) {


                    val updateCartModel = UpdateCartModel()
                    updateCartModel.quantity = quantity.toString()
                    getBaseWebServices(true).putMultipartAPI(WebServiceConstants.PATH_ORDERPRODUCTS.toString() + "/" + arrData[position].product_id, null,
                            updateCartModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
                        override fun requestDataResponse(webResponse: WebResponse<Any?>?) {

                            quantity = quantity!! + 1
                            arrData.get(position).setQuantity(quantity!!)
                            cartAdapter.notifyItemChanged(position)
                        }

                        override fun onError(`object`: Any?) {}
                    })

                }

            }
            "sub" -> {
                quantity = arrData[position].quantity
                if (quantity!! > 1) {


                    val updateCartModel = UpdateCartModel()
                    updateCartModel.quantity = quantity.toString()
                    getBaseWebServices(true).putMultipartAPI(WebServiceConstants.PATH_ORDERPRODUCTS.toString() + "/" + arrData[position].product_id, null,
                            updateCartModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
                        override fun requestDataResponse(webResponse: WebResponse<Any?>?) {

                            quantity = quantity!! - 1
                            arrData.get(position).setQuantity(quantity!!)
                            cartAdapter.notifyItemChanged(position)
                        }

                        override fun onError(`object`: Any?) {}
                    })


                }
            }
        }


    }

    override fun onDonePaging() {
    }

    override fun onPage(offset: Int) {

    }

    override fun onError(error: String?) {
    }

    override fun onPlaceActivityResult(longitude: Double, latitude: Double, locationName: String?) {

        latitudee = latitude
        longitudee = longitude
    }

}