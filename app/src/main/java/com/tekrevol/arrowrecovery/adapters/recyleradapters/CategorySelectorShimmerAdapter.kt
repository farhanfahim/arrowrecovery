package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.pagingadapter.PagingAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.receiving_model.VehicleMakeModel

/**
 */
class CategorySelectorShimmerAdapter(private val activity: Context, private val arrData: List<VehicleMakeModel>, private val onItemClick: OnItemClickListener) : PagingAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_categories, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder is ViewHolder) {
            val model = arrData[position]
            if (arrData[position].equals(0)){

            }

            val viewHolder: ViewHolder = holder as ViewHolder
            with(viewHolder)
            {

                bindTo(model, activity)
                setListener(viewHolder, model)
            }
        }


    }

    private fun setListener(holder: ViewHolder, model: VehicleMakeModel?) {
        holder.contParent.setOnClickListener(View.OnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model, v, CategorySelectorShimmerAdapter::class.java.simpleName) })
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    override fun getPagingLayout(): Int {

        return R.layout.item_categories
    }

    override fun getPagingItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contParent = view.findViewById<RoundKornerLinearLayout>(R.id.contParent)
        val txtCategory = view.findViewById<TextView>(R.id.txtCategory)
        var model: VehicleMakeModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: VehicleMakeModel?, context: Context) {
            this.model = model
            this.model?.let {
                txtCategory?.text = it.name
                if (it.isSelected) {
                    contParent?.setBackgroundColor(context.resources.getColor(R.color.material_grey600))
                    txtCategory?.setTextColor(context.resources.getColor(R.color.c_white))
                } else {
                    contParent?.setBackgroundColor(context.resources.getColor(R.color.transparent))
                    txtCategory?.setTextColor(context.resources.getColor(R.color.material_grey600))
                }
            }


        }
    }


}