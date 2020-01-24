package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout
import com.jcminarro.roundkornerlayout.RoundKornerRelativeLayout
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.models.receiving_model.ProductDetailModel

/**
 */
class ConverterItemShimmerAdapter(private val activity: Context, private val arrData: List<ProductDetailModel>, private val onItemClick: OnItemClickListener) : PagingAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_converter, parent, false)
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

    private fun setListener(holder: ViewHolder, model: ProductDetailModel?) {
        holder.contParent.setOnClickListener { onItemClick.onItemClick(holder.adapterPosition, model, it, ConverterItemShimmerAdapter::class.java.simpleName) }
        holder.contAddToCart.setOnClickListener { onItemClick.onItemClick(holder.adapterPosition, model, it, ConverterItemShimmerAdapter::class.java.simpleName) }
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    override fun getPagingLayout(): Int {

        return 0
    }

    override fun getPagingItemCount(): Int {
        return 0
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contParent = view.findViewById<RoundKornerLinearLayout>(R.id.contParent)
        val contAddToCart = view.findViewById<RoundKornerRelativeLayout>(R.id.contAddToCart)
        val txtMake = view.findViewById<TextView>(R.id.txtMake)
        val txtModel = view.findViewById<TextView>(R.id.txtModel)
        val txtPrice = view.findViewById<TextView>(R.id.txtPrice)
        val imgConverter = view.findViewById<ImageView>(R.id.imgConverter)
        var model: ProductDetailModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */

        fun bindTo(model: ProductDetailModel?, context: Context) {
            this.model = model

            this.model?.let {
                txtMake.text = it.name
                txtModel.text = it.car_variation
                txtPrice.text = it.estimatedAmount.toString()
                ImageLoaderHelper.loadImageWithouAnimationByPath(imgConverter, it.feature_image, true)
            }
        }
    }


}