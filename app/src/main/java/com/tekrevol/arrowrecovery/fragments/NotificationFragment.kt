package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.NotificationAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.receiving_model.VehicleMakeModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_converter_dashboard.*
import kotlinx.android.synthetic.main.fragment_notification.*
import retrofit2.Call
import java.util.HashMap

class NotificationFragment : BaseFragment(), OnItemClickListener {


    private var arrData: ArrayList<DummyModel> = ArrayList()
    private lateinit var notificationAdapter: NotificationAdapter
    var webCall: Call<WebResponse<Any>>? = null

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
        notificationAdapter = NotificationAdapter(context!!, arrData, this)

    }

    override fun setListeners() {

      /*  backButton.setOnClickListener {
            baseActivity.popBackStack()
        }*/

        cbSelectAll.setOnCheckedChangeListener { buttonView, isChecked ->
            arrData.forEach { it.isSelected = isChecked }
            notificationAdapter.notifyDataSetChanged()
        }

        btnDelete.setOnClickListener {
            UIHelper.showAlertDialog("Are you sure you want to delete selected Notifications?", "Delete Notification", { dialog, which ->
                arrData.removeAll { it.isSelected }
                notificationAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }, context)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBind()

    }

    private fun onBind() {
        arrData.clear()
        arrData.addAll(Constants.notifications())

        recyclerViewNotification.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        (recyclerViewNotification.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        val resId = R.anim.layout_animation_fall_bottom
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        recyclerViewNotification.layoutAnimation = animation
        recyclerViewNotification.adapter = notificationAdapter
    }


    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, anyObject: Any?, view: View?, type: String?) {
        when(view?.id){
            R.id.imgSelect -> {
                arrData[position].isSelected = !arrData[position].isSelected
                notificationAdapter.notifyDataSetChanged()
            }

        }
    }


    private fun getNotification() {


        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_PARAM_LIMIT] = 0
        queryMap[WebServiceConstants.Q_PARAM_OFFSET] = 0

        webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_NOTIFICATIONS, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : TypeToken<java.util.ArrayList<DummyModel?>?>() {}.type
                val arrayList: java.util.ArrayList<DummyModel> = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type)


                arrData.addAll(arrayList)
                notificationAdapter.notifyDataSetChanged()


            }

            override fun onError(`object`: Any?) {
                if (recyclerViewNotification == null) {
                    return
                }

            }
        })

    }


}

