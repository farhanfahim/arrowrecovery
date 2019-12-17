package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar

class ProfileFragment : BaseFragment() {


    var editProfile: ImageView? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editProfile = view.findViewById(R.id.editProfile)
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