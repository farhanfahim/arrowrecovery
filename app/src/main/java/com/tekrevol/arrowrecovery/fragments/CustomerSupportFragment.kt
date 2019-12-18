package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_support_customer.*

class CustomerSupportFragment : BaseFragment() {

    companion object {

        fun newInstance(): CustomerSupportFragment {

            val args = Bundle()

            val fragment = CustomerSupportFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun getFragmentLayout(): Int {

        return R.layout.fragment_support_customer
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {

        txtLeaveMessage.setOnClickListener(View.OnClickListener {

            showNextBuildToast()

        })

    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

}