package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.FilterModel

/**
 */
class FilterAdapter(private val activity: Context, private val arrData: ArrayList<FilterModel>, private val onItemClick: OnItemClickListener) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_filter, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = arrData[i]
        with(holder) {
            bindTo(model, activity)
            setListener(this, model)
        }
    }

    private fun setListener(holder: ViewHolder, model: FilterModel) {
        holder.btnCancel.setOnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model, v, FilterAdapter::class.java.simpleName) }
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contDay = view.findViewById<RoundKornerLinearLayout>(R.id.contDay)
        val txtFilter = view.findViewById<TextView>(R.id.txtFilter)
        val txtQuery = view.findViewById<TextView>(R.id.txtQuery)
        val btnCancel = view.findViewById<ImageView>(R.id.btnCancel)
        var model: FilterModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: FilterModel?, context: Context) {
            this.model = model

            this.model?.let {
                txtFilter?.text = it.filter
                txtQuery?.text = it.text
            }


        }
    }


}