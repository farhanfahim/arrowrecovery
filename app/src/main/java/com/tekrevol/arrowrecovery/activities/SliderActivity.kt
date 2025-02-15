package com.tekrevol.arrowrecovery.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.synnapps.carouselview.ImageListener
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.managers.SharedPreferenceManager
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.models.receiving_model.ProductDetailModel
import kotlinx.android.synthetic.main.activity_product_detail.carouselView

class SliderActivity : AppCompatActivity(), ImageListener {
    var model: String? = null
    var productDetailModel: ProductDetailModel? = null
    private var sharedPreferenceManager: SharedPreferenceManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_slider)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)
        onBind()
    }

    private fun onBind() {
        model = intent.getStringExtra(AppConstants.JSON_STRING_KEY)
        productDetailModel = GsonFactory.getSimpleGson().fromJson(model, ProductDetailModel::class.java)
        carouselView.setImageListener(imageListener)
        carouselView.pageCount = productDetailModel?.attachments?.size!!

    }

    override fun setImageForPosition(position: Int, imageView: ImageView?) {
        imageView?.let {
            it.scaleType = ImageView.ScaleType.CENTER_INSIDE
            it.setImageResource(Constants.sampleConverterBanners[position])
        }
    }

    var imageListener = ImageListener { position, imageView ->
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER)
        ImageLoaderHelper.loadImageWithAnimations(imageView, productDetailModel?.attachments?.get(position)?.attachment_url, true)
    }
}