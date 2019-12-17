package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import com.google.android.libraries.places.internal.nu
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar


var carouselView: CarouselView? = null
var sampleImages = intArrayOf(R.drawable.graph, R.drawable.graph, R.drawable.graph)

class HomeFragment : BaseFragment() {

    companion object {

        fun newInstance(): HomeFragment {

            val args = Bundle()

            val fragment = HomeFragment()
            fragment.setArguments(args)
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carouselView = view.findViewById(R.id.carouselView)
        bindData();
    }

    private fun bindData() {
        carouselView?.setPageCount(sampleImages.size)
        carouselView?.setImageListener(imageListener)
    }

    private var imageListener: ImageListener = ImageListener { position, imageView -> imageView.setImageResource(sampleImages[position]) }


    override fun getDrawerLockMode(): Int {
        return 0;
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_home
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

}