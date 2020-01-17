package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.models.receiving_model.Order
import com.tekrevol.arrowrecovery.widget.AnyTextView

/**
 */
class MyOrderShimmerAdapter(private val activity: Context, private val arrData: List<Order>, private val onItemClick: OnItemClickListener) : PagingAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_myorder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ViewHolder) {
            val model = arrData[position]

            val viewHolder: ViewHolder = holder
            with(viewHolder)
            {

                bindTo(model, activity,position)
                setListener(viewHolder, model)
            }
        }


    }

    private fun setListener(holder: ViewHolder, model: Order?) {
        holder.layoutItemMyOrder!!.setOnClickListener { onItemClick.onItemClick(holder.adapterPosition, model, it, MyOrderShimmerAdapter::class.java.simpleName) }
        }

    override fun getItemCount(): Int {
        return arrData.size
    }

    override fun getPagingLayout(): Int {

        return R.layout.shimmer_item_order
    }

    override fun getPagingItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var layoutItemMyOrder: LinearLayout? = view.findViewById(R.id.layoutItemMyOrder)
        var imgOrderItem: ImageView? = view.findViewById(R.id.imgOrderItem)

        var txtUser: AnyTextView? = view.findViewById(R.id.txtUserName)
        var txtAddress: AnyTextView? = view.findViewById(R.id.txtAddress)
        var txtPhone: AnyTextView? = view.findViewById(R.id.txtPhone)
        var txtPrice: AnyTextView? = view.findViewById(R.id.txtTotalPrice)
        var txtStatus: AnyTextView? = view.findViewById(R.id.txtStatus)
        var txtDate: AnyTextView? = view.findViewById(R.id.txtDate)
        var model: Order? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */

        fun bindTo(model: Order?, context: Context,position :Int) {
            this.model = model

            this.model?.let {
                txtUser?.text = it.userModel.userDetails.fullName
                txtAddress?.text = it.userModel.userDetails.address
                txtPhone?.text = it.userModel.userDetails.phone
                txtPrice?.text = "$"+it.estimatedAmount.toString()
                txtDate?.text = it.created_at

                if (model!!.status == AppConstants.STATUS_COMPLETED){
                    txtStatus!!.text = "Completed"
                    txtStatus!!.setTextColor(ContextCompat.getColor(context, R.color.green_bg))
                }
                if (model!!.status == AppConstants.STATUS_RETURNED){
                    txtStatus!!.text = "Rejected"
                    txtStatus!!.setTextColor(ContextCompat.getColor(context, R.color.red_bg))
                }
                if (model!!.status == AppConstants.STATUS_CART){
                    txtStatus!!.text = "Pending"
                    txtStatus!!.setTextColor(ContextCompat.getColor(context, R.color.fbutton_color_sun_flower))
                }
                ImageLoaderHelper.loadImageWithouAnimationByPath(imgOrderItem, it.orderProducts[position].product.feature_image, true)
            }

        }
    }


}