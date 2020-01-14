package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.activities.ProductDetailActivity
import com.tekrevol.arrowrecovery.adapters.recyleradapters.DaysSelectorAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.MyOrderAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.MyOrderShimmerAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.receiving_model.Order
import com.tekrevol.arrowrecovery.models.receiving_model.ProductDetailModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_converter_dashboard.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_myorder.*
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import java.util.HashMap

class MyOrderFragment : BaseFragment(), OnItemClickListener {

    private var arrData: ArrayList<Order> = ArrayList()
    private lateinit var myOrderAdapter: MyOrderShimmerAdapter
    var webCall: Call<WebResponse<Any>>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myOrderAdapter = MyOrderShimmerAdapter(context!!, arrData, this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBind()
        getOrders()
    }

    private fun onBind() {

        arrData.clear()
        //arrData.addAll(Constants.daysSelector())

        recyclerViewMyOrder.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        (recyclerViewMyOrder.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        val resId = R.anim.layout_animation_fall_bottom
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        recyclerViewMyOrder.layoutAnimation = animation
        recyclerViewMyOrder.adapter = myOrderAdapter

    }

    companion object {

        fun newInstance(): MyOrderFragment {

            val args = Bundle()

            val fragment = MyOrderFragment()
            fragment.setArguments(args)
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
        baseActivity.addDockableFragment(OrderDetailFragment.newInstance(order), true)


    }


    private fun getOrders() {

        recyclerViewMyOrder.showShimmer()
        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_WITH_ORDER_PRODUCTS] = 1
        webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_ORDERS, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : TypeToken<java.util.ArrayList<Order?>?>() {}.type
                val arrayList: java.util.ArrayList<Order> = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type)

                recyclerViewMyOrder.hideShimmer()
                arrData.clear()
                arrData.addAll(arrayList)
                myOrderAdapter.notifyDataSetChanged()

            }

            override fun onError(`object`: Any?) {
                if (rvConverters == null) {
                    recyclerViewMyOrder.hideShimmer()
                    return
                }
            }
        })
    }
}