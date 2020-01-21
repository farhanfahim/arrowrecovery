package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.internal.it
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.AppConstants
import com.tekrevol.arrowrecovery.managers.DateManager
import com.tekrevol.arrowrecovery.models.receiving_model.NotificationModel
import com.tekrevol.arrowrecovery.models.receiving_model.OrderModel
import com.tekrevol.arrowrecovery.widget.AnyTextView
import java.text.ParseException
import java.text.SimpleDateFormat


/**
 */
class NotificationShimmerAdapter(private val activity: Context, private val arrData: List<NotificationModel>, private val onItemClick: OnItemClickListener) : PagingAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ViewHolder) {
            val model = arrData[position]

            val viewHolder: ViewHolder = holder
            with(viewHolder)
            {

                bindTo(model)
                setListener(viewHolder, model)
            }
        }


    }

    private fun setListener(holder: ViewHolder, model: NotificationModel?) {
        holder.imgSelect!!.setOnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model, v, NotificationShimmerAdapter::class.java.simpleName) }

    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    override fun getPagingLayout(): Int {

        return R.layout.item_notification
    }

    override fun getPagingItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        var imgSelect: ImageView? = view.findViewById(R.id.imgSelect)
        var txtTitle: AnyTextView? = view.findViewById(R.id.txtTitle)
        var txtMessage: AnyTextView? = view.findViewById(R.id.txtMessage)
        var txtTime: AnyTextView? = view.findViewById(R.id.txtTime)
        var model: NotificationModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */

        fun bindTo(model: NotificationModel?) {
            this.model = model

            this.model?.let {
                if (it.isSelected) {
                    imgSelect!!.setImageResource(R.drawable.img_selecte_check)
                } else {
                    imgSelect!!.setImageResource(R.drawable.img_unselected_check)
                }

                txtMessage!!.text = it.data.message

                val date = it.created_at
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                try {
                    val date1 = formatter.parse(date)
                    txtTime!!.text = (DateManager.getTimeDifference(date1))
                } catch (e: ParseException) {
                    e.printStackTrace()
                }


            }


        }
    }


}