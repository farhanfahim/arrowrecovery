package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.models.receiving_model.OrderProductModel
import com.tekrevol.arrowrecovery.widget.AnyTextView

/**
 */
class OrderDetailShimmerAdapter(private val activity: Context, private val arrData: List<OrderProductModel>, private val onItemClick: OnItemClickListener) : PagingAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_order, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ViewHolder) {
            val model = arrData[position]

            val viewHolder: ViewHolder = holder
            with(viewHolder)
            {

                bindTo(model, activity)
                setListener(viewHolder, model)
            }
        }


    }

    private fun setListener(holder: ViewHolder, model: OrderProductModel?) {
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    override fun getPagingLayout(): Int {

        return R.layout.item_order
    }

    override fun getPagingItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtRef: AnyTextView? = view.findViewById(R.id.txtRef)
        var imgOrderItem: ImageView? = view.findViewById(R.id.imgStatus)
        var txtName: AnyTextView? = view.findViewById(R.id.txtName)
        var txtPrice: AnyTextView? = view.findViewById(R.id.txtPrice)
        var txtQtyAndPrice: AnyTextView? = view.findViewById(R.id.txtQtyAndPrice)
        var txtQuality: AnyTextView? = view.findViewById(R.id.txtQuality)
        var txtTotalPrice: AnyTextView? = view.findViewById(R.id.txtTotalPrice)
        var model: OrderProductModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */

        fun bindTo(model: OrderProductModel?, context: Context) {
            this.model = model

            this.model?.let {
                txtRef!!.text = it.product.serial_number
                txtPrice!!.text = it.amount.toString()
                txtName!!.text = it.product.name
                var calculatedAmount = it.amount * it.quantity
                txtQtyAndPrice!!.text = it.amount.toString() + "x" + it.quantity.toString()
                txtTotalPrice!!.text = calculatedAmount.toString()
                txtQuality!!.text = it.quality.toString() + "%"
                ImageLoaderHelper.loadImageWithouAnimationByPath(imgOrderItem, it.product.feature_image, true)
            }

        }
    }


}