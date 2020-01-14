package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.activities.ProductDetailActivity
import com.tekrevol.arrowrecovery.adapters.recyleradapters.SearchAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.SearchBarShimmerAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.receiving_model.ProductDetailModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import java.util.HashMap

class SearchFragment : BaseFragment(), OnItemClickListener {


    private var arrData: ArrayList<DummyModel> = ArrayList()
    private var text: String? = null
    private var arrDataSearchBar: ArrayList<ProductDetailModel> = ArrayList()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchBarShimmerAdapter: SearchBarShimmerAdapter
    var webCall: Call<WebResponse<Any>>? = null

    companion object {

        fun newInstance(): SearchFragment {

            val args = Bundle()

            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchAdapter = SearchAdapter(context!!, arrData, this)
        searchBarShimmerAdapter = SearchBarShimmerAdapter(context!!, arrDataSearchBar, this)

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

        rvSearch.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        (rvSearch.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        val resId2 = R.anim.layout_animation_fall_bottom
        val animation2 = AnimationUtils.loadLayoutAnimation(context, resId2)
        rvSearch.layoutAnimation = animation2
        rvSearch.adapter = searchBarShimmerAdapter

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
                    //getProducts(text!!)
                    getProducts(s.toString())
                    recyclerViewSearchList.visibility = View.GONE
                    rvSearch.visibility = View.VISIBLE
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

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.visibility = View.GONE

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

        baseActivity.openActivity(ProductDetailActivity::class.java)

    }


    private fun getProducts(query: String) {

        rvSearch.showShimmer()

        val queryMap = HashMap<String, Any>()

        queryMap[WebServiceConstants.Q_QUERY] = query

        webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_GET_PRODUCT, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : TypeToken<java.util.ArrayList<ProductDetailModel?>?>() {}.type
                val arrayList: java.util.ArrayList<ProductDetailModel> = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type)

                rvSearch.hideShimmer()

                arrDataSearchBar.clear()
                arrDataSearchBar.addAll(arrayList)
                searchBarShimmerAdapter.notifyDataSetChanged()

            }

            override fun onError(`object`: Any?) {
                rvSearch.hideShimmer()
            }
        })
    }


}

