package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.activities.ProductDetailActivity
import com.tekrevol.arrowrecovery.adapters.recyleradapters.SearchAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.SearchBarShimmerAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.SearchHistoryModel
import com.tekrevol.arrowrecovery.models.receiving_model.ProductDetailModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import com.todkars.shimmer.ShimmerAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.firstOrNull
import kotlin.collections.set

class SearchFragment : BaseFragment(), OnItemClickListener {

    private var arrData: ArrayList<SearchHistoryModel> = ArrayList()
    private var arrData2: ArrayList<SearchHistoryModel> = ArrayList()
    private var text: String? = null
    private var arrDataSearchBar: ArrayList<ProductDetailModel> = ArrayList()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchBarShimmerAdapter: SearchBarShimmerAdapter
    var webCall: Call<WebResponse<Any>>? = null
    var searchHistoryModel: SearchHistoryModel = SearchHistoryModel()

    companion object {
        var makeId: String = ""
        var modelId: String = ""
        var year: String = ""
        var serialNumber: String = ""
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
        arrData2.clear()
        loadData()
        recyclerViewSearchList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        (recyclerViewSearchList.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        val resId = R.anim.layout_animation_fall_bottom
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        recyclerViewSearchList.layoutAnimation = animation
        recyclerViewSearchList.adapter = searchAdapter

        rvSearch.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        (rvSearch.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        rvSearch.adapter = searchBarShimmerAdapter
        rvSearch.setItemViewType(ShimmerAdapter.ItemViewType { type: Int, position: Int -> R.layout.shimmer_item_searchbar })

        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                text = s.toString()
                arrDataSearchBar.clear()
                if (s.length < 1) {
                    recyclerViewSearchList.visibility = View.VISIBLE
                    rvSearch.visibility = View.GONE
                    if (makeId.isNullOrEmpty() && modelId.isNullOrEmpty() && year.isNullOrEmpty() && serialNumber.isNullOrEmpty()) {
                    } else {
                        getProducts(text!!, makeId, modelId, year, serialNumber)
                        searchHistoryModel.query = text
                        recyclerViewSearchList.visibility = View.GONE
                        rvSearch.visibility = View.VISIBLE
                    }
                    //Toast.makeText(context, "Search keyword required", Toast.LENGTH_SHORT).show()
                } else if (s.length > 2 && count > before) {
                    getProducts(text!!, makeId, modelId, year, serialNumber)
                    searchHistoryModel.query = text

                    recyclerViewSearchList.visibility = View.GONE
                    rvSearch.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

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

        imgProfile.setOnClickListener(View.OnClickListener {
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

        if (type == SearchFragment::class.java.simpleName) {
            val gson = Gson()
            val json = sharedPreferenceManager.getString(AppConstants.KEY_SAVESEARCH)
            if (json != "") {
                val type = object : TypeToken<java.util.ArrayList<SearchHistoryModel?>?>() {}.type
                if (type != null) {
                    arrData2 = gson.fromJson(json, type)
                    makeId = ""
                    modelId = ""
                    year = ""
                    serialNumber = ""
                    edtSearch?.setText(arrData2[position].query)
                }
            }
        } else {
            var product: ProductDetailModel = anyObject as ProductDetailModel
            baseActivity.openActivity(ProductDetailActivity::class.java, product.toString())
            insertItem(product.name)
        }

    }

    private fun getProducts(query: String, makeId: String, modelId: String, year: String, serialNumber: String) {

        recyclerViewSearchList.visibility = View.GONE
        rvSearch.showShimmer()
        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_QUERY] = query
        queryMap[WebServiceConstants.Q_MAKE_ID] = makeId
        queryMap[WebServiceConstants.Q_MODEL_ID] = modelId
        queryMap[WebServiceConstants.Q_YEAR] = year
        queryMap[WebServiceConstants.Q_SERIAL_NUMBER] = serialNumber
        webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_GET_PRODUCT, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : TypeToken<java.util.ArrayList<ProductDetailModel?>?>() {}.type
                val arrayList: java.util.ArrayList<ProductDetailModel> = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type)
                if (arrayList.isEmpty()) {
                    recyclerViewSearchList.visibility = View.VISIBLE
                    rvSearch.visibility = View.GONE
                } else {
                    recyclerViewSearchList.visibility = View.GONE
                    rvSearch.visibility = View.VISIBLE
                }

                rvSearch.hideShimmer()
                arrDataSearchBar.clear()
                arrDataSearchBar.addAll(arrayList)
                searchBarShimmerAdapter.notifyDataSetChanged()

            }

            override fun onError(`object`: Any?) {
                if (rvSearch == null) {
                    return
                }
                rvSearch.hideShimmer()
            }
        })
    }

    private fun loadData() {
        val gson = Gson()
        val json = sharedPreferenceManager.getString(AppConstants.KEY_SAVESEARCH)
        if (json != "") {
            val type = object : TypeToken<java.util.ArrayList<SearchHistoryModel?>?>() {}.type
            if (type != null) {
                arrData2 = gson.fromJson(json, type)
                arrData.addAll(arrData2)
                searchAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun insertItem(searchKeyword: String) {
        val jsonSaveData = sharedPreferenceManager.getString(AppConstants.KEY_SAVESEARCH)
        if (jsonSaveData != "") {
            val type = object : TypeToken<java.util.ArrayList<SearchHistoryModel?>?>() {}.type
            arrData2 = gson.fromJson(jsonSaveData, type)
            val firstOrNull = arrData2.firstOrNull { it.query.equals(searchKeyword, true) }

            if (firstOrNull == null) {
                if (arrData2.size > 4) {
                    arrData2.removeAt(0)
                    searchAdapter.notifyItemRemoved(0)
                    arrData.clear()
                    arrData.addAll(arrData2)
                    val json = gson.toJson(arrData)
                    sharedPreferenceManager.putValue(AppConstants.KEY_SAVESEARCH, json)
                    arrData.add(SearchHistoryModel(searchKeyword))
                    searchAdapter.notifyItemInserted(arrData.size)
                } else {
                    arrData.add(SearchHistoryModel(searchKeyword))
                    val json = gson.toJson(arrData)
                    sharedPreferenceManager.putValue(AppConstants.KEY_SAVESEARCH, json)
                    searchAdapter.notifyItemInserted(arrData.size)
                }
                return

            }
        } else {
            arrData.add(SearchHistoryModel(searchKeyword))
            val json = gson.toJson(arrData)
            sharedPreferenceManager.putValue(AppConstants.KEY_SAVESEARCH, json)
            searchAdapter.notifyItemInserted(arrData.size)
        }

    }

    override fun onDestroyView() {
        webCall?.cancel()
        super.onDestroyView()
    }
}

