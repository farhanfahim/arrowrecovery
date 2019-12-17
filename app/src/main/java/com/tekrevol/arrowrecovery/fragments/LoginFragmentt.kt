package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.AnyTextView
import com.tekrevol.arrowrecovery.widget.TitleBar


class LoginFragmentt : BaseFragment() {
    private var txt_signup: AnyTextView? = null
    private var txtLogin: TextView? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txt_signup = view.findViewById(R.id.txt_signup)
        txtLogin = view.findViewById(R.id.txtLogin)
    }

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_login
    }

    override fun setTitlebar(titleBar: TitleBar?) {

    }

    override fun setListeners() {

        txtLogin?.setOnClickListener(View.OnClickListener {
            baseActivity.addDockableFragment(OptVerification.newInstance(), true)
        })

        txt_signup?.setOnClickListener(View.OnClickListener {
            baseActivity.popBackStack()
            baseActivity.addDockableFragment(RegisterPagerFragment.newInstance(), true)
        })
    }

    override fun onClick(v: View?) {
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