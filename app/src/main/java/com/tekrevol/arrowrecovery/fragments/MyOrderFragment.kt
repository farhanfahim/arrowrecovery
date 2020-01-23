package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ProgressBar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingDelegate
import com.tekrevol.arrowrecovery.adapters.recyleradapters.MyOrderShimmerAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.todkars.shimmer.ShimmerAdapter.ItemViewType
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.receiving_model.OrderModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_myorder.*
import retrofit2.Call
import java.util.HashMap

class MyOrderFragment : BaseFragment(), OnItemClickListener, PagingDelegate.OnPageListener {

    private var arrData: ArrayList<OrderModel> = ArrayList()
    private lateinit var myOrderAdapter: MyOrderShimmerAdapter
    var webCall: Call<WebResponse<Any>>? = null

    private var offset: Int = 0
    private val limit = 10
    private var x = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myOrderAdapter = MyOrderShimmerAdapter(context!!, arrData, this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBind()
        getOrders(limit, 0)
    }

    private fun onBind() {

        arrData.clear()
        val mLayoutManager1: RecyclerView.LayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerViewMyOrder.layoutManager = mLayoutManager1
        (recyclerViewMyOrder.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        val pagingDelegate: PagingDelegate = PagingDelegate.Builder(myOrderAdapter)
                .attachTo(recyclerViewMyOrder)
                .listenWith(this@MyOrderFragment)
                .build()
        recyclerViewMyOrder.adapter = myOrderAdapter
        recyclerViewMyOrder.setItemViewType(ItemViewType { type: Int, position: Int -> R.layout.shimmer_item_categories })


    }

    companion object {

        fun newInstance(): MyOrderFragment {

            val args = Bundle()

            val fragment = MyOrderFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.resetViews()
        titleBar.visibility = View.VISIBLE
        titleBar.hide()
        titleBar.showBackButton(activity)

        titleBar.setTitle("My Order")
    }

    override fun setListeners() {

    }

    override fun getDrawerLockMode(): Int {

        return 0
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_myorder
    }

    override fun onItemClick(position: Int, anyObject: Any?, view: View?, type: String?) {

        var orderModel: OrderModel = anyObject as OrderModel
        baseActivity.addDockableFragment(OrderDetailFragment.newInstance(orderModel, position), true)


    }


    private fun getOrders(limit: Int, offset: Int) {

        if (x == 0) {
            recyclerViewMyOrder.showShimmer()

        }

        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_WITH_ORDER_PRODUCTS] = AppConstants.ALL_ORDERS
        queryMap[WebServiceConstants.Q_PARAM_LIMIT] = limit
        queryMap[WebServiceConstants.Q_PARAM_OFFSET] = offset

        webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_ORDERS, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                if (webResponse.result != null) {
                    val type = object : TypeToken<java.util.ArrayList<OrderModel?>?>() {}.type
                    val arrayList: java.util.ArrayList<OrderModel> = GsonFactory.getSimpleGson()
                            .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                    , type)

                    if (x == 0) {
                        recyclerViewMyOrder.hideShimmer()
                    }
                    arrData.addAll(arrayList)
                    myOrderAdapter.notifyDataSetChanged()
                    onDonePaging()

                } else {
                    if (recyclerViewMyOrder == null) {
                        return
                    }
                    recyclerViewMyOrder.hideShimmer()

                }

            }

            override fun onError(`object`: Any?) {
                if (recyclerViewMyOrder == null) {
                    return
                }
                recyclerViewMyOrder.hideShimmer()

            }
        })

    }


    override fun onPage(i: Int) {
        if (offset < i) {

            offset = i

            x++
            progressBarMyOrder.visibility = View.VISIBLE

            getOrders(limit, i)
        }
    }

    override fun onDonePaging() {
        if (progressBarMyOrder != null) {
            progressBarMyOrder.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        webCall?.cancel()
        super.onDestroyView()
    }
}