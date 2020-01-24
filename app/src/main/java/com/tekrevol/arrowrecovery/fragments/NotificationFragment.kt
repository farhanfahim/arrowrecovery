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
import com.todkars.shimmer.ShimmerAdapter.ItemViewType
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingDelegate
import com.tekrevol.arrowrecovery.adapters.recyleradapters.NotificationShimmerAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.receiving_model.NotificationModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_notification.*
import retrofit2.Call
import java.util.HashMap

class NotificationFragment : BaseFragment(), OnItemClickListener, PagingDelegate.OnPageListener {


    private var arrData: ArrayList<NotificationModel> = ArrayList()
    private lateinit var shimmerNotificationAdapter: NotificationShimmerAdapter
    var webCall: Call<WebResponse<Any>>? = null
    var webCallDelete: Call<WebResponse<Any>>? = null
    private var offset: Int = 0
    private val limit = 2
    private var x = 0

    companion object {

        fun newInstance(): NotificationFragment {

            val args = Bundle()

            val fragment = NotificationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_notification
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.resetViews()
        titleBar.visibility = View.VISIBLE
        titleBar.hide()
        titleBar.showBackButton(activity)
        titleBar.setTitle("Notification")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shimmerNotificationAdapter = NotificationShimmerAdapter(context!!, arrData, this)

    }

    override fun setListeners() {

        /*  backButton.setOnClickListener {
              baseActivity.popBackStack()
          }*/

        cbSelectAll.setOnCheckedChangeListener { buttonView, isChecked ->
            arrData.forEach { it.isSelected = isChecked }
            shimmerNotificationAdapter.notifyDataSetChanged()
        }

        btnDelete.setOnClickListener {

            if (arrData.isEmpty()) {
                return@setOnClickListener
            }

            UIHelper.showAlertDialog("Are you sure you want to delete selected Notifications?", "Delete Notifications", { dialog, which ->
                deleteNotification(dialog)
            }, context)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBind()
        getNotification(limit, 0)

    }

    private fun onBind() {
        arrData.clear()

        val mLayoutManager1: RecyclerView.LayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerViewNotification.layoutManager = mLayoutManager1
        (recyclerViewNotification.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        val pagingDelegate: PagingDelegate = PagingDelegate.Builder(shimmerNotificationAdapter)
                .attachTo(recyclerViewNotification)
                .listenWith(this@NotificationFragment)
                .build()
        recyclerViewNotification.adapter = shimmerNotificationAdapter
        recyclerViewNotification.setItemViewType(ItemViewType { type: Int, position: Int -> R.layout.shimmer_item_notification })

    }


    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, anyObject: Any?, view: View?, type: String?) {
        when (view?.id) {
            R.id.imgSelect -> {
                arrData[position].isSelected = !arrData[position].isSelected
                shimmerNotificationAdapter.notifyDataSetChanged()
            }

        }
    }


    private fun getNotification(limit: Int, offset: Int) {

        if (x == 0) {
            recyclerViewNotification.showShimmer()
        }

        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_PARAM_LIMIT] = limit
        queryMap[WebServiceConstants.Q_PARAM_OFFSET] = offset

        webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_NOTIFICATIONS, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : TypeToken<java.util.ArrayList<NotificationModel?>?>() {}.type
                val arrayList: java.util.ArrayList<NotificationModel> = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type)

                if (x == 0) {
                    recyclerViewNotification.hideShimmer()
                }
                arrData.addAll(arrayList)
                shimmerNotificationAdapter.notifyDataSetChanged()
                onDonePaging()


            }

            override fun onError(`object`: Any?) {
                if (recyclerViewNotification == null) {
                    return
                }
                recyclerViewNotification.showShimmer()

            }
        })

    }

    private fun deleteNotification(dialog: DialogInterface) {

        val id = arrData.filter { it.isSelected }.map { it.id }

        webCallDelete = getBaseWebServices(false).deleteAPIAnyObject(WebServiceConstants.PATH_NOTIFICATIONS_SLASH + id, "", object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                UIHelper.showToast(context, webResponse.message)
                arrData.removeAll { it.isSelected }
                shimmerNotificationAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onError(`object`: Any?) {

            }
        })


    }

    override fun onDonePaging() {
        if (progressBarNotfication != null) {
            progressBarNotfication.visibility = View.GONE
        }
    }

    override fun onPage(i: Int) {
        if (offset < i) {
            offset = i
            x++
            progressBarNotfication.visibility = View.VISIBLE
            getNotification(limit, i)
        }

    }

    override fun onDestroyView() {
        webCall?.cancel()
        webCallDelete?.cancel()
        super.onDestroyView()


    }


}

