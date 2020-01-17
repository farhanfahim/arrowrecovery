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
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.libraries.imageloader.ImageLoaderHelper
import com.tekrevol.arrowrecovery.models.receiving_model.Order
import com.tekrevol.arrowrecovery.widget.AnyTextView

/**
 */
class MyOrderAdapter(private val activity: Context?, private val arrData: List<Order>, private val onItemClick: OnItemClickListener) : RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_myorder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = arrData[i]
        setListener(holder, model)
        holder.txtUser!!.text = model.userModel.userDetails.fullName
        holder.txtAddress!!.text = model.userModel.userDetails.address
        holder.txtPhone!!.text = model.userModel.userDetails.phone
        holder.txtPrice!!.text = model.orderProducts[i].amount.toString()

        if (model.status == AppConstants.STATUS_COMPLETED){
            holder.txtStatus!!.text = "Completed"
            holder.txtStatus!!.setTextColor(ContextCompat.getColor(activity!!, R.color.green_bg))
        }
        if (model.status == AppConstants.STATUS_RETURNED){
            holder.txtStatus!!.text = "Rejected"
            holder.txtStatus!!.setTextColor(ContextCompat.getColor(activity!!, R.color.red_bg))
        }
        if (model.status == AppConstants.STATUS_PENDING){
            holder.txtStatus!!.text = "Pending"
            holder.txtStatus!!.setTextColor(ContextCompat.getColor(activity!!, R.color.fbutton_color_sun_flower))
        }
        ImageLoaderHelper.loadImageWithouAnimationByPath(holder.imgOrderItem, model.orderProducts[i].product.feature_image, true)
    }

    private fun setListener(holder: ViewHolder, model: Order) {

        holder.layoutItemMyOrder?.setOnClickListener(View.OnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model, v, null) })

    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var layoutItemMyOrder: LinearLayout? = view.findViewById(R.id.layoutItemMyOrder)
        var imgOrderItem: ImageView? = view.findViewById(R.id.imgStatus)
        var txtStatus: AnyTextView? = view.findViewById(R.id.txtStatus)
        var txtDate: AnyTextView? = view.findViewById(R.id.txtDate)
        var txtAddress: AnyTextView? = view.findViewById(R.id.txtAddress)
        var txtPrice: AnyTextView? = view.findViewById(R.id.txtPrice)
        var txtUser: AnyTextView? = view.findViewById(R.id.txtUserName)
        var txtPhone: AnyTextView? = view.findViewById(R.id.txtPhone)
    }

}