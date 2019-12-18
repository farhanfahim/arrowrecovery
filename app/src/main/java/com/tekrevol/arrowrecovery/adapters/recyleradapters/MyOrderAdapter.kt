package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener

/**
 */
class MyOrderAdapter(private val activity: Context?, private val arrData: List<String>, private val onItemClick: OnItemClickListener) : RecyclerView.Adapter<MyOrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_myorder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = arrData[i]
        setListener(holder, model)
    }

    private fun setListener(holder: ViewHolder, model: String) {

        holder.layoutItemMyOrder?.setOnClickListener(View.OnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model, v, null) })

    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {

        var layoutItemMyOrder: LinearLayout? = view?.findViewById(R.id.layoutItemMyOrder)
    }

}