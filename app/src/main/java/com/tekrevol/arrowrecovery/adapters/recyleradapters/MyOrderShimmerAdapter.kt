package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.models.receiving_model.OrderModel
import com.tekrevol.arrowrecovery.widget.AnyTextView

/**
 */
class MyOrderShimmerAdapter(private val activity: Context, private val arrData: List<OrderModel>, private val onItemClick: OnItemClickListener) : PagingAdapter() {
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

                bindTo(model, activity, position)
                setListener(viewHolder, model)
            }
        }


    }

    private fun setListener(holder: ViewHolder, model: OrderModel?) {
        holder.layoutItemMyOrder!!.setOnClickListener { onItemClick.onItemClick(holder.adapterPosition, model, it, MyOrderShimmerAdapter::class.java.simpleName) }
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
        var layoutItemMyOrder: LinearLayout? = view.findViewById(R.id.layoutItemMyOrder)
        var imgStatus: ImageView? = view.findViewById(R.id.imgStatus)

        //  var txtUser: AnyTextView? = view.findViewById(R.id.txtUserName)
        //  var txtAddress: AnyTextView? = view.findViewById(R.id.txtAddress)
        var txtOrderid: AnyTextView? = view.findViewById(R.id.txtOrderid)
        //  var txtPhone: AnyTextView? = view.findViewById(R.id.txtPhone)
        var txtPrice: AnyTextView? = view.findViewById(R.id.txtTotalPrice)
        var txtStatus: AnyTextView? = view.findViewById(R.id.txtStatus)
        var txtDate: AnyTextView? = view.findViewById(R.id.txtDate)
        var model: OrderModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */

        fun bindTo(model: OrderModel?, context: Context, position: Int) {
            this.model = model

            this.model?.let {
                //txtUser?.text = it.userModel.userDetails.fullName
                //txtAddress?.text = it.userModel.userDetails.address
                txtOrderid?.text = "order id: " + it.id.toString()
                // txtPhone?.text = it.userModel.userDetails.phone
                txtPrice?.text = "Total: $" + it.estimatedAmount.toString()

                val dateParts = it.created_at.split(" ")
                val date = dateParts[0]
                txtDate?.text = date.split("-").reversed().joinToString("-")

                if (model!!.status == AppConstants.STATUS_RECEIVED) {
                    txtStatus!!.text = "Received"
                    txtStatus!!.setTextColor(ContextCompat.getColor(context, R.color.green_bg))
                    imgStatus!!.setImageResource(R.drawable.approved)
                }
                if (model!!.status == AppConstants.STATUS_ORDERED) {
                    txtStatus!!.text = "Ordered"
                    txtStatus!!.setTextColor(ContextCompat.getColor(context, R.color.green_bg))
                    imgStatus!!.setImageResource(R.drawable.approved)
                }
                if (model!!.status == AppConstants.STATUS_DELIVERED) {
                    txtStatus!!.text = "Delivered"
                    txtStatus!!.setTextColor(ContextCompat.getColor(context, R.color.green_bg))
                    imgStatus!!.setImageResource(R.drawable.approved)
                }
                if (model!!.status == AppConstants.STATUS_VERIFIED) {
                    txtStatus!!.text = "verified"
                    txtStatus!!.setTextColor(ContextCompat.getColor(context, R.color.green_bg))
                    imgStatus!!.setImageResource(R.drawable.approved)
                }
                if (model!!.status == AppConstants.STATUS_PAID) {
                    txtStatus!!.text = "Paid"
                    txtStatus!!.setTextColor(ContextCompat.getColor(context, R.color.green_bg))
                    imgStatus!!.setImageResource(R.drawable.approved)
                }
                if (model!!.status == AppConstants.STATUS_COMPLETED) {
                    txtStatus!!.text = "Completed"
                    txtStatus!!.setTextColor(ContextCompat.getColor(context, R.color.green_bg))
                    imgStatus!!.setImageResource(R.drawable.approved)
                }

                if (model!!.status == AppConstants.STATUS_RETURNED) {
                    txtStatus!!.text = "Rejected"
                    txtStatus!!.setTextColor(ContextCompat.getColor(context, R.color.red_bg))
                    imgStatus!!.setImageResource(R.drawable.rejected)
                }

                if (model!!.status == AppConstants.STATUS_PENDING) {
                    txtStatus!!.text = "Pending"
                    txtStatus!!.setTextColor(ContextCompat.getColor(context, R.color.fbutton_color_sun_flower))
                    imgStatus!!.setImageResource(R.drawable.pending)
                }
                if (model!!.status == AppConstants.STATUS_CART) {
                    txtStatus!!.text = "In Cart"
                    txtStatus!!.setTextColor(ContextCompat.getColor(context, R.color.fbutton_color_sun_flower))
                    imgStatus!!.setImageResource(R.drawable.pending)

                }
            }

        }
    }


}