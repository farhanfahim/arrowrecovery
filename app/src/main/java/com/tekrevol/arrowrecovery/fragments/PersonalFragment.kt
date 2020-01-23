package com.tekrevol.arrowrecovery.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_editprofile.contTitle
import kotlinx.android.synthetic.main.fragment_editprofile.txtTitle
import kotlinx.android.synthetic.main.fragment_personal.*

class PersonalFragment : BaseFragment() {
    private var selectedPosition: Int = 0


    companion object {
        fun newInstance(): PersonalFragment {
            val args = Bundle()

            val fragment = PersonalFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_personal

    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {


        contTitle.setOnClickListener {
            UIHelper.showCheckedDialogBox(context, "Select Title", Constants.title, selectedPosition) { dialog, which ->
                selectedPosition = (dialog as androidx.appcompat.app.AlertDialog).listView.checkedItemPosition
                txtTitle.text = Constants.title[selectedPosition]
                dialog.dismiss()
            }
        }

        radioBtnIndividual.setOnClickListener {
            edtCompanyName.visibility = View.GONE
            edtKindCompany.visibility = View.GONE
        }

        radioBtnCompany.setOnClickListener {
            edtCompanyName.visibility = View.VISIBLE
            edtKindCompany.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

}