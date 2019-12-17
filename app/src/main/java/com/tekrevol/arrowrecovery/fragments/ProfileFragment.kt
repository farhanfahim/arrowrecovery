package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editProfile.setOnClickListener(View.OnClickListener {
            editProfile.visibility = View.GONE
            txtAddress.visibility = View.GONE
            edtAddress.visibility = View.VISIBLE
            edtAbout.visibility = View.VISIBLE
            txtAbout.visibility = View.GONE
            txtSave.visibility = View.VISIBLE
        })

        txtSave.setOnClickListener(View.OnClickListener {
            txtSave.visibility = View.GONE
            editProfile.visibility = View.VISIBLE
            txtAddress.visibility = View.VISIBLE
            edtAddress.visibility = View.GONE
            edtAbout.visibility = View.GONE
            txtAbout.visibility = View.VISIBLE

        })
    }

    companion object {

        fun newInstance(): ProfileFragment {

            val args = Bundle()

            val fragment = ProfileFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun getFragmentLayout(): Int {

        return R.layout.fragment_profile
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