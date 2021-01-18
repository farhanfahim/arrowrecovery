package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.hbb20.CountryCodePicker
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_contact.*

public class ContactFragment : BaseFragment() {

    companion object {
        var ccpLoadNumber:CountryCodePicker? = null
        var isValid = false
        fun newInstance(): ContactFragment {
            val args = Bundle()


            val fragment = ContactFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ccpLoadNumber = ccp
        registerCarrierEditText()
        ccp.registerCarrierNumberEditText(edtPhoneNo)
        ccp.setNumberAutoFormattingEnabled(true)
    }

    private fun registerCarrierEditText() {
        ccpLoadNumber!!.registerCarrierNumberEditText(edtPhoneNo)
        ccpLoadNumber!!.setPhoneNumberValidityChangeListener { isValidNumber ->
            isValid = isValidNumber
        }

        ccpLoadNumber!!.registerCarrierNumberEditText(edtPhoneNo)
    }

    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_contact


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
