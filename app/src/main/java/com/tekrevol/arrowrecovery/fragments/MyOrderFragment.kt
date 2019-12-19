package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.DaysSelectorAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.MyOrderAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_myorder.*

class MyOrderFragment : BaseFragment(), OnItemClickListener {

    private var arrData: ArrayList<DummyModel> = ArrayList()
    private lateinit var myOrderAdapter: MyOrderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myOrderAdapter = MyOrderAdapter(context!!, arrData, this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBind()
    }

    private fun onBind() {

        arrData.clear()
        arrData.addAll(Constants.daysSelector())

        recyclerViewMyOrder.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {
        backButton.setOnClickListener(View.OnClickListener {
            baseActivity.popBackStack()
        })



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

    override fun onItemClick(position: Int, `object`: Any?, view: View?, type: String?) {

        baseActivity.addDockableFragment(OrderDetailFragment.newInstance(),true)

    }
}