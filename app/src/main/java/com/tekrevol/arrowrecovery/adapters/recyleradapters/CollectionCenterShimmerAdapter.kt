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
import com.tekrevol.arrowrecovery.models.receiving_model.CollectionModel
import com.tekrevol.arrowrecovery.models.receiving_model.NotificationModel
import com.tekrevol.arrowrecovery.models.receiving_model.OrderModel
import com.tekrevol.arrowrecovery.widget.AnyTextView
import java.text.ParseException
import java.text.SimpleDateFormat


/**
 */
class CollectionCenterShimmerAdapter(private val activity: Context, private val arrData: List<CollectionModel>, private val onItemClick: OnItemClickListener) : PagingAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_collection_center, parent, false)
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

    private fun setListener(holder: ViewHolder, model: CollectionModel?) {
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    override fun getPagingLayout(): Int {

        return R.layout.item_collection_center
    }

    override fun getPagingItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        var txtName: AnyTextView? = view.findViewById(R.id.txtName)
        var txtAddress: AnyTextView? = view.findViewById(R.id.txtAddress)
        var model: CollectionModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */

        fun bindTo(model: CollectionModel?) {
            this.model = model

            this.model?.let {

                txtName!!.text = it.name
                txtAddress!!.text = it.address

            /*    val date = it.createdAt
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                try {
                    val date1 = formatter.parse(date)
                    txtTime!!.text = (DateManager.getTimeDifference(date1))
                } catch (e: ParseException) {
                    e.printStackTrace()
                }*/
            }


        }
    }


}