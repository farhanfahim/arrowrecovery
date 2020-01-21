package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.widget.AnyTextView



/**
 */
class SearchAdapter(private val activity: Context?, private val arrData: List<String>, private val onItemClick: OnItemClickListener) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_search, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = arrData[i]
        setListener(holder, model)
    }

    private fun setListener(holder: ViewHolder, model: String) {

        //   holder.layoutItemMyOrder?.setOnClickListener(View.OnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model) })

    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {

        val txtSearched = view!!.findViewById<AnyTextView>(R.id.txtSearched)
    }

}