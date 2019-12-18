package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.CartAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.DaysSelectorAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(), OnItemClickListener {

    private var arrData: ArrayList<DummyModel> = ArrayList()
    private lateinit var daysSelectorAdapter: DaysSelectorAdapter

    companion object {

        fun newInstance(): HomeFragment {

            val args = Bundle()

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        daysSelectorAdapter = DaysSelectorAdapter(context!!, arrData, this)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBind()
    }

    private fun onBind() {
        arrData.clear()
        arrData.addAll(Constants.daysSelector())

        rvDays.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvDays.adapter = daysSelectorAdapter
    }

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_home
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {
        contRefresh.setOnClickListener {
            imgArrow.visibility = View.GONE
            progressRefresh.visibility = View.VISIBLE

            Handler().postDelayed({
                imgArrow.visibility = View.VISIBLE
                progressRefresh.visibility = View.GONE
            }, 3000)


        }
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, anyObject: Any?) {
        arrData.forEach { it.isSelected = false }
        arrData[position].isSelected = true
        daysSelectorAdapter.notifyDataSetChanged()
    }

}