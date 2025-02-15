package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.models.receiving_model.OrderProductModel
import com.tekrevol.arrowrecovery.widget.AnyTextView
import kotlinx.android.synthetic.main.fragment_editprofile.*

class MyCartAdapter(private val activity: Context, private val arrData: List<OrderProductModel>, private val onItemClick: OnItemClickListener) : PagingAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_cart, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ViewHolder) {
            val model = arrData[position]

            val viewHolder: ViewHolder = holder
            with(viewHolder)
            {

                bindTo(model, activity, position)
                setListener(viewHolder, model)
            }
        }


    }

    private fun setListener(holder: ViewHolder, model: OrderProductModel?) {
        holder.imgSelect.setOnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model, v, CartAdapter::class.java.simpleName) }
        holder.contSelectQuality.setOnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model, v, CartAdapter::class.java.simpleName) }
        holder.btnSubtract.setOnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model, v, CartAdapter::class.java.simpleName) }
        holder.btnAdd.setOnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model, v, CartAdapter::class.java.simpleName) }
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    override fun getPagingLayout(): Int {

        return R.layout.item_cart
    }

    override fun getPagingItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val imgSelect = view.findViewById<ImageView>(R.id.imgSelect)
        val imgItem = view.findViewById<ImageView>(R.id.img_item)
        val btnAdd = view.findViewById<ImageView>(R.id.btnAdd)
        val txtSerialNo = view.findViewById<AnyTextView>(R.id.txtSerialNo)
        val btnSubtract = view.findViewById<ImageView>(R.id.btnSubtract)
        val txtName = view.findViewById<AnyTextView>(R.id.txtName)
        val txtQuality = view.findViewById<AnyTextView>(R.id.txtQuality)
        val edtQuantity = view.findViewById<AnyTextView>(R.id.edtQuantityCart)
        val txtPrice = view.findViewById<AnyTextView>(R.id.txtPrice)
        val contSelectQuality = view.findViewById<LinearLayout>(R.id.contSelectQuality)
        var model: OrderProductModel? = null

        fun bindTo(model: OrderProductModel?, context: Context, position: Int) {
            this.model = model

            this.model?.let {
                if (it.isSelected) {
                    imgSelect.setImageResource(R.drawable.img_selecte_check)
                } else {
                    imgSelect.setImageResource(R.drawable.img_unselected_check)
                }

                txtSerialNo.text = it.product?.serial_number
                txtName.text = it.product?.vehicleMake?.name
             //   txtName.text = it.product?.name
                txtPrice.text = "$" + it.amount
                txtQuality.text = it.quality.toString() + "%"
                edtQuantity.text = it.quantity.toString()

                ImageLoaderHelper.loadImageWithAnimations(imgItem, it.product.feature_image_url, true)

            }
        }
    }


}