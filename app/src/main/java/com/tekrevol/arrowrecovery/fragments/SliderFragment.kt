package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar

class SliderFragment : BaseFragment() {
    override fun getDrawerLockMode(): Int {
        return 0
    }

    companion object {

        fun newInstance(): SliderFragment {

            val args = Bundle()

            val fragment = SliderFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun getFragmentLayout(): Int {
        return R.layout.fragment_slider
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }
}