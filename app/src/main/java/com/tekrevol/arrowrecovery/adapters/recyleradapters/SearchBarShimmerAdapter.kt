package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.models.receiving_model.ProductDetailModel

/**
 */
class SearchBarShimmerAdapter(private val activity: Context, private val arrData: List<ProductDetailModel>, private val onItemClick: OnItemClickListener) : PagingAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_searchbar, parent, false)
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
        holder.contSearch!!.setOnClickListener { onItemClick.onItemClick(holder.adapterPosition, model, it, SearchBarShimmerAdapter::class.java.simpleName) }


    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    override fun getPagingLayout(): Int {

        return R.layout.item_searchbar
    }

    override fun getPagingItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var contSearch: RoundKornerLinearLayout? = view.findViewById(R.id.contSearch)
        val txtName = view.findViewById<TextView>(R.id.txtName)
        val txtRef = view.findViewById<TextView>(R.id.txtRef)
        val txtPrice = view.findViewById<TextView>(R.id.txtPrice)
        val imgConverter = view.findViewById<ImageView>(R.id.img)
        var model: ProductDetailModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */

        fun bindTo(model: ProductDetailModel?, context: Context) {
            this.model = model

            this.model?.let {
                txtName.text = it.name
                txtRef.text = it.serial_number
                txtPrice.text = "$"+it.estimatedPrice
                ImageLoaderHelper.loadImageWithouAnimationByPath(imgConverter, it.feature_image, true)
            }

        }
    }


}