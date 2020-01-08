package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_editprofile.*

public class PersonalFragment : BaseFragment() {
    private var selectedPosition: Int = 0


    companion object {
        fun newInstance(): PersonalFragment {
            val args = Bundle()

            val fragment = PersonalFragment()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_personal

    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {


        contTitle.setOnClickListener {
            UIHelper.showCheckedDialogBox(context, "Select Title", Constants.title, selectedPosition) { dialog, which ->
                selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                txtTitle.text = Constants.title[selectedPosition]
                dialog.dismiss()
            }
        }
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

}