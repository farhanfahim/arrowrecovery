package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.widget.AnyTextView

/**
 */
class NotificationAdapter(private val activity: Context?, private val arrData: List<DummyModel>, private val onItemClick: OnItemClickListener) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = arrData[i]
        with(holder){
            bindTo(model, activity!!)
            cbSelect?.setOnCheckedChangeListener { buttonView, isChecked -> arrData[i].isSelected = isChecked }
            setListener(this, model)
        }
    }

    private fun setListener(holder: ViewHolder, model: DummyModel) {
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cbSelect = view.findViewById<CheckBox>(R.id.cbSelect)
        val txtTitle = view.findViewById<AnyTextView>(R.id.txtTitle)
        var model: DummyModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: DummyModel?, context: Context) {
            this.model = model

            this.model?.let {
                cbSelect.isChecked = it.isSelected
                txtTitle.text = it.text
            }


        }
    }

}