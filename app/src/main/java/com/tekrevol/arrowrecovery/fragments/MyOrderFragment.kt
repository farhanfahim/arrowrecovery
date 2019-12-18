package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.MyOrderAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_myorder.*

class MyOrderFragment : BaseFragment(), OnItemClickListener {

    private var arr: ArrayList<String> = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBind()
    }

    private fun onBind() {

        arr.add("034111")
        arr.add("034111")
        arr.add("034111")
        arr.add("034111")
        arr.add("034111")
        arr.add("034111")
        recyclerViewMyOrder.layoutManager = LinearLayoutManager(context)
        recyclerViewMyOrder.adapter = MyOrderAdapter(context, arr, this)

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