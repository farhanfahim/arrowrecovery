package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.NotificationAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_notification.*

class NotificationFragment : BaseFragment(), OnItemClickListener {


    private var arrData: ArrayList<DummyModel> = ArrayList()
    private lateinit var notificationAdapter: NotificationAdapter

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

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationAdapter = NotificationAdapter(context!!, arrData, this)

    }

    override fun setListeners() {

        backButton.setOnClickListener {
            baseActivity.popBackStack()
        }

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

    }


}

