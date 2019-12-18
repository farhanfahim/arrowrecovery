package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.MyOrderAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.OrderDetailAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_myorder.*
import kotlinx.android.synthetic.main.fragment_orderdetail.*

class OrderDetailFragment : BaseFragment(), OnItemClickListener {

    private var arr: ArrayList<String> = ArrayList()

    companion object {

        fun newInstance(): OrderDetailFragment {

            val args = Bundle()

            val fragment = OrderDetailFragment()
            fragment.setArguments(args)
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBind()
    }

    private fun onBind() {

        arr.add("1")
        arr.add("2")
        arr.add("3")
        arr.add("4")
        arr.add("5")
        arr.add("6")
        recyclerViewOrderDetail.layoutManager = LinearLayoutManager(context)
        recyclerViewOrderDetail.adapter = OrderDetailAdapter(context, arr, this)

    }


    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_orderdetail
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, type: String?) {

    }


}

