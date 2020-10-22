package com.tekrevol.arrowrecovery.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.activities.ProductDetailActivity
import com.tekrevol.arrowrecovery.adapters.recyleradapters.FilterAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.SearchAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.SearchBarShimmerAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.enums.BaseURLTypes
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.FilterModel
import com.tekrevol.arrowrecovery.models.SearchHistoryModel
import com.tekrevol.arrowrecovery.models.receiving_model.Product
import com.tekrevol.arrowrecovery.models.receiving_model.ProductDetailModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.utils.RxSearchObserver
import com.tekrevol.arrowrecovery.widget.TitleBar
import com.todkars.shimmer.ShimmerAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.set

class SearchFragment : BaseFragment(), OnItemClickListener {

    private var arrData: ArrayList<SearchHistoryModel> = ArrayList()
    private var arrData2: ArrayList<SearchHistoryModel> = ArrayList()


    private var arrFilter: ArrayList<FilterModel> = ArrayList()
    private lateinit var filterAdapter: FilterAdapter
    private var text: String? = null
    private var arrDataSearchBar: ArrayList<ProductDetailModel> = ArrayList()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchBarShimmerAdapter: SearchBarShimmerAdapter
    var webCall: Call<WebResponse<Any>>? = null
    var searchHistoryModel: SearchHistoryModel = SearchHistoryModel()

    companion object {
        var makeId: String = ""
        var modelId: String = ""
        var modelString: String = ""
        var makeString: String = ""
        var year: String = ""
        var serialNumber: String = ""
        var searchKeyword: String = ""
        fun newInstance(): SearchFragment {

            val args = Bundle()
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("CheckResult")
    fun searchProductsByKeywords() {

        val observer : DisposableObserver<WebResponse<Any>> = getSearchObserver2()

        val services = WebServices(activity, sharedPreferenceManager!!.getString(AppConstants.KEY_TOKEN), BaseURLTypes.BASE_URL, false)

        val query = HashMap<String, Any>()

        if (query.isNullOrEmpty() && makeId.isNullOrEmpty() && modelId.isNullOrEmpty() && year.isNullOrEmpty() && serialNumber.isNullOrEmpty()) {
            recyclerViewSearchList.visibility = View.VISIBLE
            rvSearch.visibility = View.GONE
        } else {
            recyclerViewSearchList.visibility = View.GONE
            rvSearch.showShimmer()
            val queryMap = HashMap<String, Any>()
            if (query.isNotEmpty()) {
                queryMap[WebServiceConstants.Q_QUERY] = query
            }
            arrFilter.clear()
            if (makeId.isNotEmpty()) {
                queryMap[WebServiceConstants.Q_MAKE_ID] = makeId
                arrFilter.add(FilterModel(": " + makeString, "Make"))
            }

            if (modelId.isNotEmpty()) {
                queryMap[WebServiceConstants.Q_MODEL_ID] = modelId
                arrFilter.add(FilterModel(": " + modelString, "Model"))

            }

            if (year.isNotEmpty()) {
                queryMap[WebServiceConstants.Q_YEAR] = year
                arrFilter.add(FilterModel(": " + year, "Year"))

            }

            if (serialNumber.isNotEmpty()) {
                queryMap[WebServiceConstants.Q_SERIAL_NUMBER] = serialNumber
                arrFilter.add(FilterModel(": " + serialNumber, "Serial Number"))

            }
        }

            filterAdapter.notifyDataSetChanged()

        searchKeyword = edtSearch.stringTrimmed
        if (makeId.isNotEmpty() || modelId.isNotEmpty() || year.isNotEmpty() || serialNumber.isNotEmpty()){

            getProducts(edtSearch.stringTrimmed)

        }else{

            RxSearchObserver.fromView(edtSearch)
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .distinctUntilChanged()
                    .switchMap { s ->
                        query[WebServiceConstants.Q_QUERY] = s
                        services.getProductsBySearch (WebServiceConstants.PATH_GET_PRODUCT, query )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(observer)
        }

    }

    private fun getSearchObserver2(): DisposableObserver<WebResponse<Any>> {
        return object : DisposableObserver<WebResponse<Any>>() {
            override fun onComplete() {
            }

            override fun onNext(webResponse: WebResponse<Any>) {

                if (edtSearch.stringTrimmed != "" || edtSearch.stringTrimmed != null){
                    val product: Product = GsonFactory.getSimpleGson()
                            .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                    , Product::class.java)

                    if (recyclerViewSearchList != null || rvSearch != null) {
                        if (product.products.size > 0) {

                            searchHistoryModel.query = edtSearch.stringTrimmed
                            recyclerViewSearchList.visibility = View.GONE
                            rvSearch.visibility = View.VISIBLE

                            rvSearch.hideShimmer()
                            arrDataSearchBar.clear()
                            arrDataSearchBar.addAll(product.products)
                            searchBarShimmerAdapter.notifyDataSetChanged()

                        } else {
                            recyclerViewSearchList.visibility = View.VISIBLE
                            rvSearch.visibility = View.GONE

                            rvSearch.hideShimmer()
                            arrDataSearchBar.clear()
                            searchBarShimmerAdapter.resetAdapter()
                        }
                    }
                }else{
                    recyclerViewSearchList.visibility = View.VISIBLE
                    rvSearch.visibility = View.GONE

                    rvSearch.hideShimmer()
                    arrDataSearchBar.clear()
                    searchBarShimmerAdapter.resetAdapter()
                }



            }

            override fun onError(throwable: Throwable) {
                if (rvSearch == null) {
                    return
                }
                rvSearch.hideShimmer()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchAdapter = SearchAdapter(context!!, arrData, this)
        searchBarShimmerAdapter = SearchBarShimmerAdapter(context!!, arrDataSearchBar, this)
        filterAdapter = FilterAdapter(context!!, arrFilter, this)


    }

    private fun onBind() {

        arrData.clear()
        arrData2.clear()
        arrFilter.clear()

        recyclerViewFilter.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewFilter.adapter = filterAdapter
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

        /*edtSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                text = s.toString()
                arrDataSearchBar.clear()
                if (s.isEmpty()) {
                    recyclerViewSearchList.visibility = View.VISIBLE
                    rvSearch.visibility = View.GONE
                    if (makeId.isNullOrEmpty() && modelId.isNullOrEmpty() && year.isNullOrEmpty() && serialNumber.isNullOrEmpty()) {

                    } else {
                        getProducts(text!!)
                        searchHistoryModel.query = text
                        recyclerViewSearchList.visibility = View.GONE
                        rvSearch.visibility = View.VISIBLE
                    }
                    //Toast.makeText(context, "Search keyword required", Toast.LENGTH_SHORT).show()
                } else if (s.length >= 2 && count > before) {
                    getProducts(text!!)
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
        })*/

        edtSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch()
                    return true
                }
                return false
            }
        })



        searchProductsByKeywords()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        onBind()




    }
    private fun performSearch() {
        edtSearch.clearFocus()
        val `in` = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        `in`.hideSoftInputFromWindow(edtSearch.windowToken, 0)
        //...perform search
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

        when (view?.id) {
            R.id.btnCancel -> {
                val strFilter = arrFilter.firstOrNull { it.filter.equals(arrFilter[position].filter, true) }

                if (strFilter != null) {
                    if (strFilter.filter.equals("Model")) {

                        modelId = ""
                        modelString = ""

                    } else if (strFilter.filter.equals("Make")) {

                        makeId = ""
                        makeString = ""

                    } else if (strFilter.filter.equals("Year")) {

                        year = ""
                    } else if (strFilter.filter.equals("Serial Number")) {
                        serialNumber = ""

                    }

                    arrFilter.removeAt(position)
                    filterAdapter.notifyItemRemoved(position)
                    searchProductsByKeywords()
                }

            }
        }

        if (type == SearchAdapter::class.java.simpleName) {
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
                    makeString = ""
                    modelString = ""
                    edtSearch?.setText(arrData2[position].query)
                }
            }
        } else if (type == SearchBarShimmerAdapter::class.java.simpleName) {
            var product: ProductDetailModel = anyObject as ProductDetailModel
            baseActivity.openActivity(ProductDetailActivity::class.java, product.toString())
            product.vehicleMake?.name?.let { insertItem(it) }
        }

    }

    private fun getProducts(query: String) {
        edtSearch.setText("")

        if (query.isNullOrEmpty() && makeId.isNullOrEmpty() && modelId.isNullOrEmpty() && year.isNullOrEmpty() && serialNumber.isNullOrEmpty()) {
            recyclerViewSearchList.visibility = View.VISIBLE
            rvSearch.visibility = View.GONE
        } else {
            recyclerViewSearchList.visibility = View.GONE
            rvSearch.showShimmer()
            val queryMap = HashMap<String, Any>()
            if (query.isNotEmpty()) {
                queryMap[WebServiceConstants.Q_QUERY] = query
            }
            arrFilter.clear()
            if (makeId.isNotEmpty()) {
                queryMap[WebServiceConstants.Q_MAKE_ID] = makeId
                arrFilter.add(FilterModel(": " + makeString, "Make"))
            }

            if (modelId.isNotEmpty()) {
                queryMap[WebServiceConstants.Q_MODEL_ID] = modelId
                arrFilter.add(FilterModel(": " + modelString, "Model"))

            }

            if (year.isNotEmpty()) {
                queryMap[WebServiceConstants.Q_YEAR] = year
                arrFilter.add(FilterModel(": " + year, "Year"))

            }

            if (serialNumber.isNotEmpty()) {
                queryMap[WebServiceConstants.Q_SERIAL_NUMBER] = serialNumber
                arrFilter.add(FilterModel(": " + serialNumber, "Serial Number"))

            }

            filterAdapter.notifyDataSetChanged()

            webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_GET_PRODUCT, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
                override fun requestDataResponse(webResponse: WebResponse<Any?>) {


                    val product: Product = GsonFactory.getSimpleGson()
                            .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                    , Product::class.java)

                    if (product.products.isEmpty()) {
                        recyclerViewSearchList.visibility = View.VISIBLE
                        rvSearch.visibility = View.GONE
                    } else {
                        recyclerViewSearchList.visibility = View.GONE
                        rvSearch.visibility = View.VISIBLE
                    }

                    rvSearch.hideShimmer()
                    arrDataSearchBar.clear()
                    arrDataSearchBar.addAll(product.products)
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
        makeId = ""
        modelId = ""
        year = ""
        serialNumber = ""
        makeString = ""
        modelString = ""
        //edtSearch?.setText("")
        super.onDestroyView()
    }
}

