package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar

class MyOrderFragment : BaseFragment() {

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
}