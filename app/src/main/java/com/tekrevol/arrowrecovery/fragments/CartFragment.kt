package com.tekrevol.arrowrecovery.fragments

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
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.receiving_model.Order
import com.tekrevol.arrowrecovery.models.receiving_model.OrderProduct
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import com.todkars.shimmer.ShimmerAdapter.ItemViewType
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_converter_dashboard.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.forEach
import kotlin.collections.removeAll
import kotlin.collections.set

class CartFragment : BaseFragment(), OnItemClickListener, PagingDelegate.OnPageListener {

    private var arrData: ArrayList<OrderProduct> = ArrayList()
    private lateinit var cartAdapter: MyCartAdapter
    var webCall: Call<WebResponse<Any>>? = null


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
        myCartApi()
        //       arrData.addAll(Constants.notifications())

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
        webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_ORDERS, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : TypeToken<java.util.ArrayList<Order?>?>() {}.type
                val arrayList: java.util.ArrayList<Order> = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type)

                recyclerViewCart.hideShimmer()
                arrData.clear()
                for (categories in arrayList) {
                    arrData.addAll(categories.orderProducts)
                }

                cartAdapter.notifyDataSetChanged()
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
            /* val checkoutDialogFragment = CheckoutDialogFragment()
             checkoutDialogFragment.show(baseActivity.supportFragmentManager, "CheckoutDialogFragment")*/
            val checkoutDialogFragment = CheckoutDialogFragment()
            checkoutDialogFragment.show(baseActivity.supportFragmentManager, "CheckoutDialogFragment")
        }

        cbSelectAll.setOnCheckedChangeListener { buttonView, isChecked ->
            arrData.forEach { it.isSelected = isChecked }
            cartAdapter.notifyDataSetChanged()
        }

        btnDelete.setOnClickListener {
            UIHelper.showAlertDialog("Are you sure you want to delete selected Items?", "Delete Items", { dialog, which ->
                arrData.removeAll { it.isSelected }
                cartAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }, context)
        }
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
                showNextBuildToast()
            }
            R.id.btnAdd -> {
                showNextBuildToast()
            }
        }
    }

    override fun onDonePaging() {
    }

    override fun onPage(offset: Int) {

    }

}