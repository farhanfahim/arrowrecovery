package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.SearchAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.SearchBarAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment(), OnItemClickListener {


    private var arrData: ArrayList<DummyModel> = ArrayList()
    private var text: String? = null
    private var arrDataSearchBar: ArrayList<DummyModel> = ArrayList()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchBarAdapter: SearchBarAdapter

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
        searchBarAdapter = SearchBarAdapter(context!!, arrDataSearchBar, this)

    }

    private fun onBind() {

        arrData.clear()
        arrData.addAll(Constants.daysSelector())


        recyclerViewSearchList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        (recyclerViewSearchList.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        val resId = R.anim.layout_animation_fall_bottom
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        recyclerViewSearchList.layoutAnimation = animation
        recyclerViewSearchList.adapter = searchAdapter

        recyclerViewSearchItem.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        (recyclerViewSearchItem.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        val resId2 = R.anim.layout_animation_fall_bottom
        val animation2 = AnimationUtils.loadLayoutAnimation(context, resId2)
        recyclerViewSearchItem.layoutAnimation = animation2
        recyclerViewSearchItem.adapter = searchBarAdapter

        /*if (onCreated) {
            return;
        }*/txtSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (txtSearch.text.length > 2) {
                    text = txtSearch.text.toString()
                    arrDataSearchBar.clear()
                    arrDataSearchBar.addAll(Constants.daysSelector())
                    recyclerViewSearchList.visibility = View.GONE
                    recyclerViewSearchItem.visibility = View.VISIBLE
                }
            }
        })

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

    override fun onItemClick(position: Int, anyObject: Any?, view: View?, type: String?) {

        baseActivity.addDockableFragment(OrderDetailFragment.newInstance(), true)

    }


}

