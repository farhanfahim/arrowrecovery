package com.tekrevol.arrowrecovery.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.reflect.TypeToken
import com.synnapps.carouselview.ImageClickListener
import com.synnapps.carouselview.ImageListener
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.activities.ProductDetailActivity
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingDelegate
import com.tekrevol.arrowrecovery.adapters.recyleradapters.CategorySelectorShimmerAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.ConverterItemShimmerAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.receiving_model.ProductDetailModel
import com.tekrevol.arrowrecovery.models.receiving_model.VehicleMakeModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import com.todkars.shimmer.ShimmerAdapter.ItemViewType
import kotlinx.android.synthetic.main.fragment_converter_dashboard.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList


class ConverterDashboardFragment : BaseFragment(), ImageListener, OnItemClickListener, PagingDelegate.OnPageListener {


    private var arrCategories: ArrayList<VehicleMakeModel> = ArrayList()
    private var arrFeatured: ArrayList<ProductDetailModel> = ArrayList()
    private var arrConverters: ArrayList<ProductDetailModel> = ArrayList()
    private lateinit var categorySelectorAdapter: CategorySelectorShimmerAdapter
    private lateinit var converterItemShimmerAdapter: ConverterItemShimmerAdapter
    var webCall: Call<WebResponse<Any>>? = null
    var webCallProductDetail: Call<WebResponse<Any>>? = null
    var webCallFeatured: Call<WebResponse<Any>>? = null
    var itemPos: Int = 0
    //lateinit var txtTotalItems:AnyTextView


    companion object {

        fun newInstance(): ConverterDashboardFragment {

            val args = Bundle()

            val fragment = ConverterDashboardFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun getFragmentLayout(): Int {

        return R.layout.fragment_converter_dashboard
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categorySelectorAdapter = CategorySelectorShimmerAdapter(context!!, arrCategories, this)
        converterItemShimmerAdapter = ConverterItemShimmerAdapter(context!!, arrConverters, this)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBind()
        calculateRecyclerHeight(view)
    }

    private fun calculateRecyclerHeight(view: View) {
        dummyViews.post {
            val params: ViewGroup.LayoutParams = rvConverters.layoutParams

            val dimension = context!!.resources.getDimension(R.dimen.x10dp)
            val dimension2 = context!!.resources.getDimension(R.dimen.x12dp)
            params.height = dummyViews.height + dimension.toInt() + dimension2.toInt()
            rvConverters.layoutParams = params

        }
    }

    private fun onBind() {
        arrFeatured.clear()
        arrCategories.clear()
        arrConverters.clear()

        //arrCategories.addAll(Constants.categorySelector())

//        rvCategories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//        rvCategories.adapter = categorySelectorAdapter

        val mLayoutManager1: RecyclerView.LayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rvCategories.layoutManager = mLayoutManager1
        (rvCategories.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        val pagingDelegate: PagingDelegate = PagingDelegate.Builder(categorySelectorAdapter)
                .attachTo(rvCategories)
                .listenWith(this@ConverterDashboardFragment)
                .build()
        rvCategories.adapter = categorySelectorAdapter
        rvCategories.setItemViewType(ItemViewType { type: Int, position: Int -> R.layout.shimmer_item_categories })

        //
        //arrConverters.addAll()


        val mLayoutManager2: RecyclerView.LayoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        rvConverters.layoutManager = mLayoutManager2
        (rvConverters.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        val pagingDelegate2: PagingDelegate = PagingDelegate.Builder(converterItemShimmerAdapter)
                .attachTo(rvCategories)
                .listenWith(this@ConverterDashboardFragment)
                .build()
        rvConverters.adapter = converterItemShimmerAdapter
        rvConverters.setItemViewType(ItemViewType { type: Int, position: Int -> R.layout.shimmer_converter_dashboard })

//        rvConverters.layoutManager = GridLayoutManager(context, 2)
//        rvConverters.adapter = converterItemShimmerAdapter

        getVehicle()
        getFeaturedList()
        //getProductDetail(1)


    }

    private fun getFeaturedList() {
        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_FEATURED] = 1
        webCallFeatured = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_GET_PRODUCT, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : TypeToken<java.util.ArrayList<ProductDetailModel?>?>() {}.type
                val arrayList: java.util.ArrayList<ProductDetailModel> = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type)

                arrFeatured.clear()
                arrFeatured.addAll(arrayList)

                carouselView.setImageListener(imageListener)
                carouselView.pageCount = arrayList.size


            }

            override fun onError(`object`: Any?) {
            }
        })
    }


    override fun setListeners() {

        carouselView.setImageClickListener { pos ->
            var product: ProductDetailModel = arrFeatured[pos]
            baseActivity.openActivity(ProductDetailActivity::class.java, product.toString())
        }

    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun setImageForPosition(position: Int, imageView: ImageView?) {
        imageView?.let {
            it.scaleType = ImageView.ScaleType.CENTER_INSIDE
            it.setImageResource(Constants.sampleConverterBanners[position])
        }
    }

    override fun onItemClick(position: Int, anyObject: Any?, view: View?, type: String?) {

        if (type == ConverterItemShimmerAdapter::class.java.simpleName) {
            when (view?.id) {
                R.id.contParent -> {

                    var product: ProductDetailModel = anyObject as ProductDetailModel
                    baseActivity.openActivity(ProductDetailActivity::class.java, product.toString())
                }
                R.id.contAddToCart -> {
                    Snackbar.make(view, "This item has been added in cart successfully!", Snackbar.LENGTH_SHORT).show()
                }
            }
        } else {
            var vehicleMakeModel = anyObject as VehicleMakeModel
            itemPos = vehicleMakeModel.id

            //Toast.makeText(context,itemPos.toString(),Toast.LENGTH_SHORT).show()

            getProductDetail(itemPos)

            arrCategories.forEach { it.isSelected = false }
            arrCategories[position].isSelected = true
            categorySelectorAdapter.notifyDataSetChanged()
            rvCategories.scrollToPosition(position)
        }

    }

    override fun onDonePaging() {
    }

    override fun onPage(offset: Int) {

    }

    private fun getVehicle() {

        rvCategories.showShimmer()
        val mquery: Map<String, Any> = HashMap()

        webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.Q_PARAM_VEHICLEMAKE, mquery, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : TypeToken<java.util.ArrayList<VehicleMakeModel?>?>() {}.type
                val arrayList: java.util.ArrayList<VehicleMakeModel> = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type)

//                val mediaModel: VehicleMakeModel = GsonFactory.getSimpleGson().fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result), MediaModel::class.java)
                // arrCategory.clear();

                rvCategories.hideShimmer()

                arrCategories.addAll(arrayList)
                categorySelectorAdapter.notifyDataSetChanged()

                arrCategories[0].isSelected = true
                getProductDetail(arrCategories[0].id)

                onDonePaging()
            }

            override fun onError(`object`: Any?) {
                if (rvCategories == null) {
                    return
                }
                rvCategories.hideShimmer()
            }
        })

    }

    private fun getProductDetail(item: Int) {

        rvConverters.showShimmer()

        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_MAKE_ID] = item
        webCallProductDetail = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_GET_PRODUCT, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : TypeToken<java.util.ArrayList<ProductDetailModel?>?>() {}.type
                val arrayList: java.util.ArrayList<ProductDetailModel> = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type)

                rvConverters.hideShimmer()

                arrConverters.clear()
                arrConverters.addAll(arrayList)
                converterItemShimmerAdapter.notifyDataSetChanged()
                onDonePaging()

            }

            override fun onError(`object`: Any?) {
                if (rvConverters == null) {
                    return
                }
                rvConverters.hideShimmer()
            }
        })
    }

    var imageListener = ImageListener { position, imageView ->
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        ImageLoaderHelper.loadImageWithAnimations(imageView, arrFeatured[position].feature_image_url, true)
    }

}