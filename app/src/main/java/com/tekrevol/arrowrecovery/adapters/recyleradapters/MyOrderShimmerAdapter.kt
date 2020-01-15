package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout
import com.jcminarro.roundkornerlayout.RoundKornerRelativeLayout
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.receiving_model.Order
import com.tekrevol.arrowrecovery.models.receiving_model.VehicleMakeModel
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
        var layoutItemMyOrder: LinearLayout? = view?.findViewById(R.id.layoutItemMyOrder)
        var imgOrderItem: ImageView? = view?.findViewById(R.id.imgOrderItem)
        var txtRef: AnyTextView? = view?.findViewById(R.id.txtRef)
        var txtMake: AnyTextView? = view?.findViewById(R.id.txtMake)
        var txtPrice: AnyTextView? = view?.findViewById(R.id.txtPrice)
        var txtUserWithAddress: AnyTextView? = view?.findViewById(R.id.txtUserWithAddress)
        var txtPhone: AnyTextView? = view?.findViewById(R.id.txtPhone)
        var model: Order? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */

        fun bindTo(model: Order?, context: Context,position :Int) {
            this.model = model

            this.model?.let {
                txtUserWithAddress?.text = it.userModel.userDetails.fullName
                txtRef!!.text = model!!.orderProducts[position].product.serial_number
                txtUserWithAddress?.text = it.userModel.userDetails.fullName +", "+it.userModel.userDetails.address
                txtPhone!!.text = it.userModel.userDetails.phone
                txtPrice!!.text = it.orderProducts[position].amount.toString()
                ImageLoaderHelper.loadImageWithouAnimationByPath(imgOrderItem, it.orderProducts[position].product.feature_image, true)
            }

        }
    }


}