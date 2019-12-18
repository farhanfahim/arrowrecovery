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
class DaysSelectorAdapter(private val activity: Context, private val arrData: List<DummyModel>, private val onItemClick: OnItemClickListener) : RecyclerView.Adapter<DaysSelectorAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_days, parent, false)
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
        holder.contDay.setOnClickListener(View.OnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model) })
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contDay = view.findViewById<RoundKornerLinearLayout>(R.id.contDay)
        val txtDay = view.findViewById<TextView>(R.id.txtDay)
        var model: DummyModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: DummyModel?, context: Context) {
            this.model = model

            this.model?.let {
                txtDay?.text = it.text
                if (it.isSelected) {
                    contDay?.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                    txtDay?.setTextColor(context.resources.getColor(R.color.c_white))
                } else {
                    contDay?.setBackgroundColor(context.resources.getColor(R.color.c_white))
                    txtDay?.setTextColor(context.resources.getColor(R.color.colorPrimary))
                }
            }


        }
    }


}