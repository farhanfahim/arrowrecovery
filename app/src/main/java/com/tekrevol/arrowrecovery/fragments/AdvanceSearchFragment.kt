package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.models.IntWrapper
import com.tekrevol.arrowrecovery.models.SpinnerModel
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_advanced_search.*
import java.util.*

class AdvanceSearchFragment : BaseFragment() {
    private val spinnerModelArrayList: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()
    private val spinnerModelArrayList1: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()
    private val spinnerModelArrayList2: ArrayList<SpinnerModel> = ArrayList<SpinnerModel>()


    companion object {

        fun newInstance(): AdvanceSearchFragment {

            val args = Bundle()

            val fragment = AdvanceSearchFragment()
            fragment.setArguments(args)
            return fragment
        }
    }


    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindData()
    }

    private fun bindData() {


        for (carMake in Constants.carMakeSelector()) {
            spinnerModelArrayList.add(SpinnerModel(carMake.text))
        }

        for (carModel in Constants.carModelSelector()) {
            spinnerModelArrayList1.add(SpinnerModel(carModel.text))
        }
        for (carYear in Constants.carYearSelector()) {
            spinnerModelArrayList2.add(SpinnerModel(carYear.text))
        }
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_advanced_search
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {

        advSearch.setOnClickListener(View.OnClickListener {
            baseActivity.popBackStack()
        })
        backButton.setOnClickListener(View.OnClickListener {
            baseActivity.popBackStack()
        })

        contMake.setOnClickListener(View.OnClickListener {
            UIHelper.showSpinnerDialog(this, spinnerModelArrayList, "Selected Make"
                    , txtMake, null, null, IntWrapper(0))

        })
        contYear.setOnClickListener(View.OnClickListener {
            UIHelper.showSpinnerDialog(this, spinnerModelArrayList2, "Selected Make"
                    , txtYear, null, null, IntWrapper(0))

        })


        contModel.setOnClickListener(View.OnClickListener {
            UIHelper.showSpinnerDialog(this, spinnerModelArrayList1, "Selected Make"
                    , txtModel, null, null, IntWrapper(0))

        })


    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }


}

