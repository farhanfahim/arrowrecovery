package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_thankyou.*
import kotlinx.android.synthetic.main.fragment_verify_account.*
import kotlinx.android.synthetic.main.item_cart.*

class ThankyouFragment : BaseFragment() {

    companion object {

        fun newInstance(): ThankyouFragment {

            val args = Bundle()

            val fragment = ThankyouFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_thankyou
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {

        txtBackToLogin.setOnClickListener(View.OnClickListener {
            baseActivity.popBackStack()
            baseActivity.addDockableFragment(LoginFragmentt.newInstance(), true)
        })
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }
}