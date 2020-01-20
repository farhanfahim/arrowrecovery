package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ProgressBar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingDelegate
import com.tekrevol.arrowrecovery.adapters.recyleradapters.MyOrderShimmerAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.receiving_model.Order
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import com.todkars.shimmer.ShimmerAdapter
import kotlinx.android.synthetic.main.fragment_converter_dashboard.*
import kotlinx.android.synthetic.main.fragment_myorder.*
import retrofit2.Call
import java.util.HashMap

class MyOrderFragment : BaseFragment(), OnItemClickListener , PagingDelegate.OnPageListener {


    private var arrData: ArrayList<Order> = ArrayList()
    private lateinit var myOrderAdapter: MyOrderShimmerAdapter
    var webCall: Call<WebResponse<Any>>? = null

    private var offset: Int = 0
    private val limit = 2
    private var x = 0

    private var progressBarMyOrder: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myOrderAdapter = MyOrderShimmerAdapter(context!!, arrData, this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarMyOrder = view.findViewById(R.id.progressBarMyOrder) as ProgressBar

        onBind()
        getOrders(limit,offset)
    }

    private fun onBind() {

        arrData.clear()
        //arrDataMake.addAll(Constants.daysSelector())
        recyclerViewMyOrder.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewMyOrder.adapter = myOrderAdapter
        recyclerViewMyOrder.setItemViewType(ShimmerAdapter.ItemViewType({ type: Int, position: Int -> R.layout.shimmer_item_myorder }))
        (recyclerViewMyOrder.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
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
  /*      backButton.setOnClickListener(View.OnClickListener {
            baseActivity.popBackStack()
        })*/

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

        var order: Order = anyObject as Order
        //baseActivity.openActivity(ProductDetailActivity::class.java, product.toString())
        baseActivity.addDockableFragment(OrderDetailFragment.newInstance(order ,position), true)


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

                    val type = object : TypeToken<java.util.ArrayList<Order?>?>() {}.type
                    val arrayList: java.util.ArrayList<Order> = GsonFactory.getSimpleGson()
                            .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                    , type)

                    if (x == 0) {
                        recyclerViewMyOrder.hideShimmer()
                    }
                    arrData.clear()
                    arrData.addAll(arrayList)
                    myOrderAdapter.notifyDataSetChanged()
                    onDonePaging()
                }

                override fun onError(`object`: Any?) {
                    if (recyclerViewMyOrder == null) {
                        recyclerViewMyOrder.hideShimmer()
                        return
                    }
                }
            })

    }



    override fun onPage(i:Int) {
        if (offset < i) {

            offset = i

            x++
            progressBarMyOrder!!.visibility = View.VISIBLE

            getOrders(limit, i)
        }
    }

    override fun onDonePaging() {
        if (progressBarMyOrder != null) {
            progressBarMyOrder!!.visibility = View.GONE
        }
    }
}