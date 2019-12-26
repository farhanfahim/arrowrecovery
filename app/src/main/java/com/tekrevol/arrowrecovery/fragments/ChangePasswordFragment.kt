package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_change_password.*

class ChangePasswordFragment : BaseFragment() {

    companion object {

        fun newInstance(): ChangePasswordFragment {

            val args = Bundle()

            val fragment = ChangePasswordFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_change_password
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.resetViews()
        titleBar.visibility = View.VISIBLE
        titleBar.hide()
        titleBar.showBackButton(activity)

        titleBar.setTitle("Change Password")

    }

    override fun setListeners() {

        txtChangePass.setOnClickListener(View.OnClickListener {
            baseActivity.popBackStack()
        })
    }

    private fun changePassApi() {

    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }



}

