package com.tekrevol.arrowrecovery.fragments

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.reflect.TypeToken
import com.synnapps.carouselview.ImageListener
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.activities.ProductDetailActivity
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingDelegate
import com.tekrevol.arrowrecovery.adapters.recyleradapters.CategorySelectorShimmerAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.ConverterItemShimmerAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants.Q_ASC
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants.Q_ORDER_BY_NAME
import com.tekrevol.arrowrecovery.enums.BaseURLTypes
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.StringHelper
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.receiving_model.Product
import com.tekrevol.arrowrecovery.models.receiving_model.ProductDetailModel
import com.tekrevol.arrowrecovery.models.receiving_model.VehicleMakeModel
import com.tekrevol.arrowrecovery.models.sending_model.OrderProductSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.utils.PaginatedRecyclerOnScrollListener
import com.tekrevol.arrowrecovery.widget.AnyEditTextView
import com.tekrevol.arrowrecovery.widget.AnyTextView
import com.tekrevol.arrowrecovery.widget.TitleBar
import com.todkars.shimmer.ShimmerAdapter.ItemViewType
import kotlinx.android.synthetic.main.fragment_converter_dashboard.*
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ConverterDashboardFragment : BaseFragment(), ImageListener, OnItemClickListener {

    private var selectedPosition: Int = 0
    private var arrCategories: ArrayList<VehicleMakeModel> = ArrayList()
    private var arrFeatured: ArrayList<ProductDetailModel> = ArrayList()
    private var arrConverters: ArrayList<ProductDetailModel> = ArrayList()
    private lateinit var categorySelectorAdapter: CategorySelectorShimmerAdapter
    private lateinit var converterItemShimmerAdapter: ConverterItemShimmerAdapter
    var webCall: Call<WebResponse<Any>>? = null
    var webCallProductDetail: Call<WebResponse<Any>>? = null
    var webCallFeatured: Call<WebResponse<Any>>? = null
    var itemPos: Int = 0
    var productid: String? = null

    private var totalPages = -1
    private var currentPage = 0

    private val limit = 20

    val queryMap = HashMap<String, String?>()

    private var progressConverters: ProgressBar? = null

    val scrollListener = object : PaginatedRecyclerOnScrollListener(){
        override fun onLoadMore(page: Int) {

            if (page+1 < totalPages){
                currentPage = page+1

                getProductsList(buildHashMap(currentPage, productid))
            }
        }

        override fun onPageChanged(currentPage: Int) {

        }
    }

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
        progressConverters = view.findViewById(R.id.progressConverters) as ProgressBar

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

        val mLayoutManager1: RecyclerView.LayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        rvCategories.layoutManager = mLayoutManager1
        (rvCategories.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        rvCategories.adapter = categorySelectorAdapter
        rvCategories.setItemViewType(ItemViewType { type: Int, position: Int -> R.layout.shimmer_item_categories })

        val mLayoutManager2 = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        rvConverters.layoutManager = mLayoutManager2
        (rvConverters.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        scrollListener?.gridLayoutManager = mLayoutManager2
        scrollListener?.pageSize = 20

        rvConverters.addOnScrollListener(scrollListener)


        rvConverters.adapter = converterItemShimmerAdapter
        rvConverters.setItemViewType(ItemViewType { type: Int, position: Int -> R.layout.shimmer_converter_dashboard })
        arrConverters.clear()

        getFeaturedList()

        getVehicle()
    }

    private fun getFeaturedList() {
        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_FEATURED] = 1
        webCallFeatured = getBaseWebServices(true).getAPIAnyObject(WebServiceConstants.PATH_GET_PRODUCT, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {


                val product: Product = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , Product::class.java)

                arrFeatured.clear()
                arrFeatured.addAll(product.products)

                carouselView.setImageListener(imageListener)
                carouselView.pageCount = product.products.size


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

        pullToRefresh.setOnRefreshListener {
            totalPages = -1
            currentPage = 0

            arrConverters.clear()
            scrollListener.reset()
            getFeaturedList()
            getVehicle()
            pullToRefresh.isRefreshing = false
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

    private fun addToCart(it: View, anyObject: Any?) {

        val dialog1 = Dialog(context)
        dialog1.window.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1.setContentView(R.layout.dialog_message)
        dialog1.window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog1.show()

        val btnSave = dialog1.findViewById<Button>(R.id.btnSave)
        val imgSubtract = dialog1.findViewById<ImageView>(R.id.imgSubtract)
        val imgAdd = dialog1.findViewById<ImageView>(R.id.imgAdd)
        val edtQuantity = dialog1.findViewById<AnyEditTextView>(R.id.edtQuantity)
        val txtQuality = dialog1.findViewById<AnyTextView>(R.id.txtQuality)
        val contQuality = dialog1.findViewById<LinearLayout>(R.id.contQuality)
        txtQuality.text = Constants.qualities[0]


        edtQuantity.setKeyListener(null)

        contQuality.setOnClickListener {
            UIHelper.showCheckedDialogBox(activity, "Select Condition", Constants.qualities, selectedPosition) { dialog, which ->
                selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                txtQuality.text = Constants.qualities[selectedPosition]
                dialog.dismiss()
            }
        }
        imgAdd.setOnClickListener {
            var quantity = edtQuantity.text.toString().toInt()
            if (quantity < 999) {
                quantity++
            }
            edtQuantity.setText(quantity.toString())
        }
        imgSubtract.setOnClickListener {
            var quantity = edtQuantity.text.toString().toInt()
            if (quantity > 0) {
                quantity--
            }
            edtQuantity.setText(quantity.toString())
        }
        edtQuantity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (s!!.isEmpty()) {
                    edtQuantity.setText("1")
                } else {
                    if (StringHelper.IsInt_ByJonas(s.toString())) {
                        if (s.toString().toInt() > 999) {
                            edtQuantity.setText("999")
                        } else if (s.toString().toInt() < 1) {
                            edtQuantity.setText("1")
                        }
                    }
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        btnSave.setOnClickListener(View.OnClickListener {

            if (txtQuality.stringTrimmed.isEmpty()) {
                UIHelper.showAlertDialog(activity, "Please select Condition")
                return@OnClickListener
            }
            if (edtQuantity.equals(0)) {
                UIHelper.showAlertDialog(activity, "Quantity most not be zero")
                return@OnClickListener
            } else {

                var product: ProductDetailModel = anyObject as ProductDetailModel
                var orderProductSendingModel = OrderProductSendingModel()
                orderProductSendingModel.productId = product.id
                orderProductSendingModel.quantity = Integer.parseInt(edtQuantity.text.toString())

                var str: String
                str = txtQuality.text.toString().replace("%", "")
                orderProductSendingModel.quality = Integer.parseInt(str)

                WebServices(activity, sharedPreferenceManager?.getString(AppConstants.KEY_TOKEN), BaseURLTypes.BASE_URL, true).postAPIAnyObject(WebServiceConstants.PATH_ORDERPRODUCTS, orderProductSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
                    override fun requestDataResponse(webResponse: WebResponse<Any>) {
                        Snackbar.make(it, "This item has been added in cart successfully!", Snackbar.LENGTH_SHORT).show()
                        dialog1.dismiss()

                    }

                    override fun onError(`object`: Any?) {}
                })
            }
        })
    }

    override fun onItemClick(position: Int, anyObject: Any?, view: View?, type: String?) {

        if (type == ConverterItemShimmerAdapter::class.java.simpleName) {
            when (view?.id) {
                R.id.contParent -> {

                    var product: ProductDetailModel = anyObject as ProductDetailModel
                    baseActivity.openActivity(ProductDetailActivity::class.java, product.toString())
                }
                R.id.contAddToCart -> {
                    addToCart(view, anyObject)
                }
            }
        } else {
            var vehicleMakeModel = anyObject as VehicleMakeModel
            itemPos = vehicleMakeModel.id

            //Toast.makeText(context,itemPos.toString(),Toast.LENGTH_SHORT).show()

            arrConverters.clear()
            scrollListener.reset()
            totalPages = -1
            currentPage = 0

            if (position == 0) {

                productid = null
                getProductsList(buildHashMap(currentPage, productid))
            } else {
                productid = itemPos.toString()
                getProductsList(buildHashMap(currentPage, productid))
            }
            arrCategories.forEach { it.isSelected = false }
            arrCategories[position].isSelected = true
            categorySelectorAdapter.notifyDataSetChanged()
            rvCategories.scrollToPosition(position)
        }

    }

    private fun getVehicle() {

        rvCategories.showShimmer()
        val query: MutableMap<String, Any> = HashMap()
        query[WebServiceConstants.Q_SORTED] = Q_ASC
        query[WebServiceConstants.Q_ORDER_BY] = Q_ORDER_BY_NAME

        webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.Q_PARAM_VEHICLEMAKE, query, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val type = object : TypeToken<java.util.ArrayList<VehicleMakeModel?>?>() {}.type
                val arrayList: java.util.ArrayList<VehicleMakeModel> = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , type)

                arrCategories.clear()
                rvCategories.hideShimmer()
                val modelAllBrands = VehicleMakeModel()
                modelAllBrands.name = "All Brands"
                arrCategories.add(modelAllBrands)
                arrCategories.addAll(arrayList)
                categorySelectorAdapter.notifyDataSetChanged()
                arrCategories[0].isSelected = true

                getProductsList(buildHashMap(currentPage, productid))
            }

            override fun onError(`object`: Any?) {
                if (rvCategories == null) {
                    return
                }
                rvCategories.hideShimmer()
            }
        })

    }

    private fun getAllBrands() {

        val queryMap = HashMap<String, Any>()
        webCallProductDetail = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_GET_PRODUCT, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                val product: Product = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , Product::class.java)

                rvConverters.hideShimmer()

                totalPages = product.total_pages
                arrConverters.addAll(product.products)
                converterItemShimmerAdapter.notifyDataSetChanged()
                txtTotalItems.text = arrConverters.size.toString() + " items found"
            }

            override fun onError(`object`: Any?) {
                if (rvConverters == null) {
                    return
                }
                rvConverters.hideShimmer()
            }
        })
    }

    private fun getProductsList(queryMap : HashMap<String, String?>) {
//        productid = item
       rvConverters.showShimmer()

        webCallProductDetail = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_GET_PRODUCT, queryMap as Map<String, Any>?, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                rvConverters.hideShimmer()

                val product: Product = GsonFactory.getSimpleGson()
                        .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                , Product::class.java)
                totalPages = product.total_pages
                arrConverters.addAll(product.products)
                if (arrConverters.isNullOrEmpty()){
                    txtStatus.visibility = View.VISIBLE
                    rvConverters.visibility = View.GONE

                    rvConverters.hideShimmer()
                }else{

                    txtStatus.visibility = View.VISIBLE
                    rvConverters.hideShimmer()
                    rvConverters.visibility = View.VISIBLE
                }
                rvConverters.scrollToPosition(arrConverters.size-20)
                converterItemShimmerAdapter.notifyDataSetChanged()
                txtTotalItems.text = arrConverters.size.toString() + " items found"
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


    override fun onDestroyView() {
        webCall?.cancel()
        webCallFeatured?.cancel()
        webCallProductDetail?.cancel()
        super.onDestroyView()
    }

    fun buildHashMap(page : Int, id : String?) : HashMap<String, String?>{

        queryMap.clear()
        if(!id.isNullOrEmpty()){
            queryMap[WebServiceConstants.Q_MAKE_ID] = id
        }
        queryMap[WebServiceConstants.Q_PARAM_LIMIT] = limit.toString()
        queryMap[WebServiceConstants.Q_PARAM_OFFSET] = page.toString()

        return queryMap
    }

}