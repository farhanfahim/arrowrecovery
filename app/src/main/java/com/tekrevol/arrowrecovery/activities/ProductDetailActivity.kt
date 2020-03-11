package com.tekrevol.arrowrecovery.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.synnapps.carouselview.ImageListener
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.constatnts.Constants.qualities
import com.tekrevol.arrowrecovery.constatnts.WebServiceConstants
import com.tekrevol.arrowrecovery.enums.BaseURLTypes
import com.tekrevol.arrowrecovery.helperclasses.StringHelper
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.KeyboardHelper
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.managers.SharedPreferenceManager
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory
import com.tekrevol.arrowrecovery.managers.retrofit.WebServices
import com.tekrevol.arrowrecovery.models.receiving_model.ProductDetailModel
import com.tekrevol.arrowrecovery.models.sending_model.OrderProductSendingModel
import com.tekrevol.arrowrecovery.models.wrappers.WebResponse
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.fragment_product_detail.*
import retrofit2.Call

class ProductDetailActivity : AppCompatActivity(), ImageListener {
    private var selectedPosition: Int = 0
    var webCall: Call<WebResponse<Any>>? = null
    var model: String? = null
    var productDetailModel: ProductDetailModel? = null
    private var sharedPreferenceManager: SharedPreferenceManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this)

        setListener()
        KeyboardHelper.hideSoftKeyboard(this, edtQuantity)
        onBind()
    }

    private fun onBind() {

        edtQuantity.setKeyListener(null)
        model = intent.getStringExtra(AppConstants.JSON_STRING_KEY)
        productDetailModel = GsonFactory.getSimpleGson().fromJson(model, ProductDetailModel::class.java)
        txtCarNum.text = (productDetailModel?.name + " " + productDetailModel?.serial_number)
        txtYear.text = (productDetailModel?.year.toString())
        txtReference.text = (productDetailModel?.serial_number)
        txtPrice.text = (productDetailModel?.estimatedPrice.toString())
        txtQuality.text = qualities[0]

        if (productDetailModel?.vehicleModel == null) {
            txtMake.text = "-"
            txtModel.text = "-"
        } else {
            txtMake.text = (productDetailModel?.vehicleModel?.vehicleMake?.name)
            txtModel.text = (productDetailModel?.vehicleModel?.name)
        }

        if (productDetailModel?.description.isNullOrEmpty()) {
            txtDescription.setText("-")
        } else {
            txtDescription.setText(Html.fromHtml(productDetailModel?.description), TextView.BufferType.SPANNABLE)
        }
        carouselView.setImageListener(imageListener)
        carouselView.pageCount = productDetailModel?.attachments?.size!!

    }

    override fun setImageForPosition(position: Int, imageView: ImageView?) {
        imageView?.let {
            it.scaleType = ImageView.ScaleType.CENTER_INSIDE
            it.setImageResource(Constants.sampleConverterBanners[position])
        }
    }

    private fun setListener() {

        carouselView.setImageClickListener { pos ->
            val i = Intent(this, SliderActivity::class.java)
            i.putExtra(AppConstants.JSON_STRING_KEY, model.toString())
            startActivity(i)
        }

        contQuality.setOnClickListener {
            UIHelper.showCheckedDialogBox(this, "Select Condition", qualities, selectedPosition) { dialog, which ->
                selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                txtQuality.text = qualities[selectedPosition]
                dialog.dismiss()
            }
        }

        btnAddToCart.setOnClickListener {

            addToCart(it)
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
        }

        )
    }

    private fun addToCart(it: View) {

        if (txtQuality.stringTrimmed.isEmpty()) {
            UIHelper.showAlertDialog(this, "Please select Condition")
            return
        }

        if (edtQuantity.equals(0)) {
            UIHelper.showAlertDialog(this, "Quantity must not be 0")
            return
        }

        var orderProductSendingModel = OrderProductSendingModel()
        orderProductSendingModel.productId = productDetailModel!!.id
        orderProductSendingModel.quantity = Integer.parseInt(edtQuantity.text.toString())


        var str: String
        str = txtQuality.text.toString().replace("%", "")
        orderProductSendingModel.quality = Integer.parseInt(str)

        WebServices(this, sharedPreferenceManager?.getString(AppConstants.KEY_TOKEN), BaseURLTypes.BASE_URL, true).postAPIAnyObject(WebServiceConstants.PATH_ORDERPRODUCTS, orderProductSendingModel.toString(), object : WebServices.IRequestWebResponseAnyObjectCallBack {
            override fun requestDataResponse(webResponse: WebResponse<Any>) {
                Snackbar.make(it, "This item has been added in cart successfully!", Snackbar.LENGTH_SHORT).show()
            }

            override fun onError(`object`: Any?) {}
        })
    }

    var imageListener = ImageListener { position, imageView ->
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER)
        ImageLoaderHelper.loadImageWithAnimations(imageView, productDetailModel?.attachments?.get(position)?.attachment_url, true)
    }


}