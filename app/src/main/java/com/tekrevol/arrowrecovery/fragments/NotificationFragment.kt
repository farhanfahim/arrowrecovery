package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.CartAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.NotificationAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_notification.*

class NotificationFragment : BaseFragment(), OnItemClickListener {


    private var arrData: ArrayList<DummyModel> = ArrayList()
    private lateinit var notificationAdapter: NotificationAdapter

    companion object {

        fun newInstance(): NotificationFragment {

            val args = Bundle()

            val fragment = NotificationFragment()
            fragment.setArguments(args)
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

        backButton.setOnClickListener(View.OnClickListener {
            baseActivity.popBackStack()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBind()


    }

    private fun onBind() {
        arrData.clear()
        arrData.addAll(Constants.daysSelector())

        recyclerViewNotification.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewNotification.adapter = notificationAdapter
    }




    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, anyObject: Any?, view: View?, type: String?) {

    }


}

