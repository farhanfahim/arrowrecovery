package com.tekrevol.arrowrecovery.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.synnapps.carouselview.ImageListener
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.KeyboardHelper
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.fragment_product_detail.*

class ProductDetailActivity : AppCompatActivity(), ImageListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        carouselView.setImageListener(this)
        carouselView.pageCount = Constants.sampleConverterBanners.size

        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setListener()
        KeyboardHelper.hideSoftKeyboard(this, edtQuantity)
    }

    override fun setImageForPosition(position: Int, imageView: ImageView?) {
        imageView?.let {
            it.scaleType = ImageView.ScaleType.CENTER_INSIDE
            it.setImageResource(Constants.sampleConverterBanners[position])
        }
    }

    private fun setListener() {
        btnAddToCart.setOnClickListener {
            Snackbar.make(it, "This item has been added in cart successfully!", Snackbar.LENGTH_SHORT).show()
        }

        btnCustomerSupport.setOnClickListener {
            Snackbar.make(it, "Customer support service is unavailable yet", Snackbar.LENGTH_SHORT).show()
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
                    edtQuantity.setText("0")
                } else{
                    if (s.toString().toInt() > 999) {
                        edtQuantity.setText("999")
                    } else if (s.toString().toInt() < 0){
                        edtQuantity.setText("0")
                    }
                }

             }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
             }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
             }
        }
        )
    }

}