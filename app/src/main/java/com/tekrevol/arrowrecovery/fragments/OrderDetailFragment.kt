package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.MyOrderAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.MyOrderShimmerAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.OrderDetailAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.OrderDetailShimmerAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.receiving_model.Order
import com.tekrevol.arrowrecovery.models.receiving_model.OrderProduct
import com.tekrevol.arrowrecovery.models.receiving_model.ProductDetailModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_myorder.*
import kotlinx.android.synthetic.main.fragment_orderdetail.*
import kotlinx.android.synthetic.main.fragment_product_detail.*
import retrofit2.Call

class OrderDetailFragment : BaseFragment(), OnItemClickListener {

    private var arrData: ArrayList<Order> = ArrayList()
    private var arr: ArrayList<DummyModel> = ArrayList()

    private lateinit var myOrderAdapter: OrderDetailShimmerAdapter

    var model: String? = null
    var order: Order? = null

    companion object {

        fun newInstance(order: Order): OrderDetailFragment {

            val args = Bundle()

            val fragment = OrderDetailFragment()
            fragment.setArguments(args)
            fragment.order = order
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myOrderAdapter = OrderDetailShimmerAdapter(context!!, arrData, this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //onBind()
    }

//    private fun onBind() {
//
//        arr.add("1")
//        arr.add("2")
//        arr.add("3")
//        arr.add("4")
//        arr.add("5")
//        arr.add("6")
//        recyclerViewOrderDetail.layoutManager = LinearLayoutManager(context)
//        (recyclerViewOrderDetail.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
//        val resId = R.anim.layout_animation_fall_bottom
//        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
//        recyclerViewOrderDetail.layoutAnimation = animation
//        recyclerViewOrderDetail.adapter = OrderDetailAdapter(context, arr, this)
//
//        txtName.text = (order?.userModel!!.userDetails.fullName)
//        txtAddress.text = (order?.userModel!!.userDetails.address)
//        txtPhone.text = (order?.userModel!!.userDetails.phone)
//
//    }

//
//    private fun onBind() {
//
//        arrData.clear()
//        //arrData.addAll(Constants.daysSelector())
//
//        recyclerViewOrderDetail.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        (recyclerViewOrderDetail.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
//        val resId = R.anim.layout_animation_fall_bottom
//        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
//        recyclerViewOrderDetail.layoutAnimation = animation
//        recyclerViewOrderDetail.adapter = myOrderAdapter
//
//        myOrderAdapter.notifyDataSetChanged()
//
//        txtName.text = (order?.userModel!!.userDetails.fullName)
//        txtAddress.text = (order?.userModel!!.userDetails.address)
//        txtPhone.text = (order?.userModel!!.userDetails.phone)
//
//    }


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

    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, type: String?) {

    }


}

