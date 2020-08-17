package com.tekrevol.arrowrecovery.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import com.tekrevol.arrowrecovery.R
import com.todkars.shimmer.ShimmerAdapter.ItemViewType
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingDelegate
import com.tekrevol.arrowrecovery.adapters.recyleradapters.CollectionCenterShimmerAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.NotificationShimmerAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.fragments.dialogs.CheckoutDialogFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.receiving_model.CollectionModel
import com.tekrevol.arrowrecovery.models.receiving_model.NotificationModel
import com.tekrevol.arrowrecovery.models.receiving_model.OrderModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_collection_center.*
import kotlinx.android.synthetic.main.fragment_myorder.*
import retrofit2.Call
import java.util.HashMap

class CollectionCentersFragment : BaseFragment(), OnItemClickListener, PagingDelegate.OnPageListener {


    private var arrData: ArrayList<CollectionModel> = ArrayList()
    private lateinit var collectionCenterShimmerAdapter: CollectionCenterShimmerAdapter
    var webCall: Call<WebResponse<Any>>? = null
    var webCallDelete: Call<WebResponse<Any>>? = null
    private var offset: Int = 0
    private val limit = 10
    private var x = 0

    companion object {

        fun newInstance(): CollectionCentersFragment {

            val args = Bundle()

            val fragment = CollectionCentersFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getDrawerLockMode(): Int {
        return 0

    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_collection_center
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.resetViews()
        titleBar.visibility = View.VISIBLE
        titleBar.hide()
        titleBar.showBackButton(activity)
        titleBar.setTitle("Collection Centers")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collectionCenterShimmerAdapter = CollectionCenterShimmerAdapter(context!!, arrData, this)

    }

    override fun setListeners() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBind()


    }

    private fun onBind() {
        arrData.clear()

        val mLayoutManager1: RecyclerView.LayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerViewCollectionCenter.layoutManager = mLayoutManager1
        (recyclerViewCollectionCenter.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        val pagingDelegate: PagingDelegate = PagingDelegate.Builder(collectionCenterShimmerAdapter)
                .attachTo(recyclerViewCollectionCenter)
                .listenWith(this@CollectionCentersFragment)
                .build()
        recyclerViewCollectionCenter.adapter = collectionCenterShimmerAdapter
        recyclerViewCollectionCenter.setItemViewType(ItemViewType { type: Int, position: Int -> R.layout.shimmer_item_collection_center })
        getCollectionCenter(limit, 0)
    }


    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, anyObject: Any?, view: View?, type: String?) {

    }


    private fun getCollectionCenter(limit: Int, offset: Int) {

        if (x == 0) {
            recyclerViewCollectionCenter.showShimmer()
        }

        val queryMap = HashMap<String, Any>()
        queryMap[WebServiceConstants.Q_PARAM_LIMIT] = limit
        queryMap[WebServiceConstants.Q_PARAM_OFFSET] = offset

        webCall = getBaseWebServices(false).getAPIAnyObject(WebServiceConstants.PATH_COLLECTIONCENTER, queryMap, object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any?>) {

                if (webResponse.result != null) {
                    val type = object : TypeToken<java.util.ArrayList<CollectionModel?>?>() {}.type
                    val arrayList: java.util.ArrayList<CollectionModel> = GsonFactory.getSimpleGson()
                            .fromJson(GsonFactory.getSimpleGson().toJson(webResponse.result)
                                    , type)
                    if (x == 0) {
                        recyclerViewCollectionCenter.hideShimmer()
                    }
                    arrData.addAll(arrayList)
                    if (arrData.isEmpty()) {
                        recyclerViewCollectionCenter.visibility = View.GONE

                    } else {
                        recyclerViewCollectionCenter.visibility = View.VISIBLE
                    }

                    collectionCenterShimmerAdapter.notifyDataSetChanged()
                    onDonePaging()

                } else {
                    if (recyclerViewCollectionCenter == null) {
                        return
                    }
                    recyclerViewCollectionCenter.hideShimmer()

                }

            }

            override fun onError(`object`: Any?) {
                if (recyclerViewCollectionCenter == null) {
                    return
                }
                recyclerViewCollectionCenter.hideShimmer()

            }
        })

    }


    override fun onDonePaging() {
        if (progressBarCollectionCenter != null) {
            progressBarCollectionCenter.visibility = View.GONE
        }
    }

    override fun onPage(i: Int) {
        if (offset < i) {
            offset = i
            x++
            progressBarCollectionCenter.visibility = View.VISIBLE
            getCollectionCenter(limit, i)
        }

    }

    override fun onDestroyView() {
        webCall?.cancel()
        webCallDelete?.cancel()
        super.onDestroyView()
    }


}

