package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.synnapps.carouselview.ImageListener
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.activities.ProductDetailActivity
import com.tekrevol.arrowrecovery.adapters.recyleradapters.CategorySelectorAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.ConverterItemAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_converter_dashboard.*


class ConverterDashboardFragment : BaseFragment(), ImageListener, OnItemClickListener {


    private var arrCategories: ArrayList<DummyModel> = ArrayList()
    private var arrConverters: ArrayList<DummyModel> = ArrayList()
    private lateinit var categorySelectorAdapter: CategorySelectorAdapter
    private lateinit var converterItemAdapter: ConverterItemAdapter

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

        categorySelectorAdapter = CategorySelectorAdapter(context!!, arrCategories, this)
        converterItemAdapter = ConverterItemAdapter(context!!, arrConverters, this)

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
        carouselView.setImageListener(this)
        carouselView.pageCount = Constants.sampleConverterBanners.size

        arrCategories.clear()
        arrCategories.addAll(Constants.categorySelector())

        rvCategories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvCategories.adapter = categorySelectorAdapter

        arrConverters.clear()
        arrConverters.addAll(Constants.categorySelector())

        rvConverters.layoutManager = GridLayoutManager(context, 2)
        rvConverters.adapter = converterItemAdapter
    }

    override fun setListeners() {
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

        if (type == ConverterItemAdapter::class.java.simpleName) {
            when (view?.id) {
                R.id.contParent -> {
                    baseActivity.openActivity(ProductDetailActivity::class.java)
                }
                R.id.contAddToCart -> {
                    Snackbar.make(view, "This item has been added in cart successfully!", Snackbar.LENGTH_SHORT).show()
                }
            }
        } else {
            arrCategories.forEach { it.isSelected = false }
            arrCategories[position].isSelected = true
            categorySelectorAdapter.notifyDataSetChanged()
            rvCategories.scrollToPosition(position)
        }


    }

}