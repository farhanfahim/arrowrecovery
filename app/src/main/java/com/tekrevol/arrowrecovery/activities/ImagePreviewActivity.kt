package com.tekrevol.arrowrecovery.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.jsibbold.zoomage.ZoomageView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.widget.AnyTextView
import kotlinx.android.synthetic.main.activity_image_preview.*

class ImagePreviewActivity : AppCompatActivity() {

    var url: String? = null
    var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)
        ButterKnife.bind(this)
        data
    }

    private val data: Unit
        get() {
            url = intent.getStringExtra(AppConstants.IMAGE_PREVIEW_URL)
            title = intent.getStringExtra(AppConstants.IMAGE_PREVIEW_TITLE)
            contProfilePicOptions.visibility = View.VISIBLE
            txtTitle.text = title
            loadImage(url)
        }

    private fun loadImage(url: String?) {
        ImageLoaderHelper.loadImageWithConstantHeaders(this, image, url, true)
    }

    @OnClick(R.id.btnBack)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.btnBack -> finish()
        }
    }
}