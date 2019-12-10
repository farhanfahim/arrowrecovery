package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Switch
import androidx.fragment.app.Fragment
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar


private var btnBack: Button? = null

class LoginFragmentt : BaseFragment() {

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_login
    }

    override fun setTitlebar(titleBar: TitleBar?) {

    }

    override fun setListeners() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnBack -> activity?.supportFragmentManager?.popBackStack();
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {

            val args = Bundle()

            val fragment = LoginFragmentt()
            fragment.arguments = args
            return fragment
        }
    }
}