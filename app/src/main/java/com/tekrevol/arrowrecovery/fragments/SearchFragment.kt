package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.SearchAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment(), OnItemClickListener {


    private var arrData: ArrayList<DummyModel> = ArrayList()
    private lateinit var searchAdapter: SearchAdapter

    companion object {

        fun newInstance(): SearchFragment {

            val args = Bundle()

            val fragment = SearchFragment()
            fragment.setArguments(args)
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchAdapter = SearchAdapter(context!!, arrData, this)

    }

    private fun onBind() {

        arrData.clear()
        arrData.addAll(Constants.daysSelector())

        recyclerViewSearchList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewSearchList.adapter = searchAdapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBind()
    }


    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_search
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {

        image.setOnClickListener(View.OnClickListener {
            baseActivity.popBackStack()
        })
        advSearch.setOnClickListener(View.OnClickListener {

            baseActivity.addDockableFragment(AdvanceSearchFragment.newInstance(), true)
        })
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, anyObject: Any?) {

    }


}

