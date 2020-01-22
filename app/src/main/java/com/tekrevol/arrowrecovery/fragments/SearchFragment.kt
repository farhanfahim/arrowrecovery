package com.tekrevol.arrowrecovery.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Toast
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
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.managers.SharedPreferenceManager
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.SearchHistoryModel
import com.tekrevol.arrowrecovery.models.receiving_model.ProductDetailModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import com.todkars.shimmer.ShimmerAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import java.util.HashMap

class SearchFragment : BaseFragment(), OnItemClickListener {



    //private var arrData: ArrayList<DummyModel> = ArrayList()
    private var arrData: ArrayList<SearchHistoryModel> = ArrayList()
    private var text: String? = null
    private var arrDataSearchBar: ArrayList<ProductDetailModel> = ArrayList()
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchBarShimmerAdapter: SearchBarShimmerAdapter
    var webCall: Call<WebResponse<Any>>? = null

    var searchHistoryModel:SearchHistoryModel = SearchHistoryModel()





    companion object {
        var makeId:String = ""
        var modelId:String = ""
        var year:String = ""
        var serialNumber:String = ""
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

        //arrData.clear()
        //arrData.addAll()

        loadSearch()
        recyclerViewSearchList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        (recyclerViewSearchList.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        val resId = R.anim.layout_animation_fall_bottom
        val animation = AnimationUtils.loadLayoutAnimation(context, resId)
        recyclerViewSearchList.layoutAnimation = animation
        recyclerViewSearchList.adapter = searchAdapter

        rvSearch.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        (rvSearch.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
//        val resId2 = R.anim.layout_animation_fall_bottom
//        val animation2 = AnimationUtils.loadLayoutAnimation(context, resId2)
//        rvSearch.layoutAnimation = animation2
        rvSearch.adapter = searchBarShimmerAdapter
        rvSearch.setItemViewType(ShimmerAdapter.ItemViewType { type: Int, position: Int -> R.layout.shimmer_item_searchbar })

        /*if (onCreated) {
            return;
        }*/edtSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                    text = edtSearch.text.toString()
                    arrDataSearchBar.clear()

                    if (text == null){
                        Toast.makeText(context,"search keyword required",Toast.LENGTH_SHORT).show()
                    }else{
                        getProducts(text!!, makeId, modelId, year, serialNumber)
                        searchHistoryModel.query = text

                        recyclerViewSearchList.visibility = View.GONE
                        rvSearch.visibility = View.VISIBLE
                    }
                    if (text!! == "") {
                        arrDataSearchBar.clear()
                        recyclerViewSearchList.visibility = View.VISIBLE
                        rvSearch.visibility = View.GONE
                        makeId = ""
                        modelId = ""
                        year = ""
                        serialNumber = ""
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

        imgProfile.setOnClickListener(View.OnClickListener {
            baseActivity.popBackStack()
        })
        advSearch.setOnClickListener(View.OnClickListener {
            if (text == null){
                Toast.makeText(context,"search keyword required",Toast.LENGTH_SHORT).show()
            }else{
                baseActivity.addDockableFragment(AdvanceSearchFragment.newInstance(), true)
            }
        })
    }


    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, anyObject: Any?, view: View?, type: String?) {

        var product: ProductDetailModel = anyObject as ProductDetailModel
        baseActivity.openActivity(ProductDetailActivity::class.java, product.toString())

        //saveSearch()
        //sharedPreferenceManager.putValue("searchKeyword",product.name)

    }

    private fun getProducts(query: String,makeId:String,modelId:String,year:String,serialNumber:String) {

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

    fun loadSearch(){
        val gson = Gson()
        val json = sharedPreferenceManager.getString("Set")
        if (json.isEmpty())
        {
            Toast.makeText(context, "There is something error", Toast.LENGTH_LONG).show()
        }
        else
        {
            val type = object : TypeToken<java.util.ArrayList<SearchHistoryModel?>?>() {}.type
            var arrPackageData:List<SearchHistoryModel> = gson.fromJson(json, type)
            for (data in arrPackageData)
            {
                arrData.add(data)

            }
        }
    }

    fun saveSearch(query:String){

        arrData.add(searchHistoryModel)
        val gson = Gson()
        val json = gson.toJson(arrData)
        sharedPreferenceManager.putValue("Set", json)
    }
}

