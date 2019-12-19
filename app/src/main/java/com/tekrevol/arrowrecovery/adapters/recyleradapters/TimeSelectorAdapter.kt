package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.SpinnerModel
import com.tekrevol.arrowrecovery.widget.AnyTextView

/**
 */
class TimeSelectorAdapter(private val activity: Context, private val arrData: List<DummyModel>, private val onItemClick: OnItemClickListener) : RecyclerView.Adapter<TimeSelectorAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_time_selection, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = arrData[i]
        with(holder) {
            bindTo(model, activity)
            setListener(this, model)
        }
    }

    private fun setListener(holder: ViewHolder, model: DummyModel) {
        holder.contTime.setOnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model,v, TimeSelectorAdapter::class.java.simpleName) }
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contTime = view.findViewById<RoundKornerLinearLayout>(R.id.contTime)
        val txtTime = view.findViewById<TextView>(R.id.txtTime)
        var model: DummyModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: DummyModel?, context: Context) {
            this.model = model

            this.model?.let {
                txtTime?.text = it.text
                if (it.isSelected) {
                    contTime?.setBackgroundColor(context.resources.getColor(R.color.colorAccent))
                    txtTime?.setTextColor(context.resources.getColor(R.color.c_white))
                } else {
                    contTime?.setBackgroundColor(context.resources.getColor(R.color.c_white))
                    txtTime?.setTextColor(context.resources.getColor(R.color.txtDarkGrey))
                }
            }


        }
    }


}