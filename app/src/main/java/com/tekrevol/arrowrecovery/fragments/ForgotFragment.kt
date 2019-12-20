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

class ForgotFragment : BaseFragment() {

    companion object {

        fun newInstance(): ForgotFragment {

            val args = Bundle()

            val fragment = ForgotFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_forgot_password
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.resetViews()
        titleBar.visibility = View.VISIBLE
        titleBar.hide()
        titleBar.showBackButton(activity)

        titleBar.setTitle("Forgot Password")

    }

    override fun setListeners() {

      /*  backButton.setOnClickListener(View.OnClickListener {

            baseActivity.popBackStack()
        })
*/
        txtForgotPass.setOnClickListener(View.OnClickListener {

            baseActivity.addDockableFragment(VerifyFragment.newInstance(), true)
        })


    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }


}

