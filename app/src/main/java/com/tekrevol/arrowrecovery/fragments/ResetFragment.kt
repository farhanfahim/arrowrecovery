package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_reset.*
import kotlinx.android.synthetic.main.fragment_verify.*

class ResetFragment : BaseFragment() {
    override fun getDrawerLockMode(): Int {

        return  0;
    }

    companion object {

        fun newInstance(): ResetFragment {

            val args = Bundle()

            val fragment = ResetFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun getFragmentLayout(): Int {

        return R.layout.fragment_reset
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.visibility = View.GONE

    }

    override fun setListeners() {

        backbtn.setOnClickListener(View.OnClickListener {

            baseActivity.popStackTill(1)
        })

        txtResetPass.setOnClickListener(View.OnClickListener {

            baseActivity.popStackTill(1)
        })
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }
}
