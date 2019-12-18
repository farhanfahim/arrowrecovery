package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout
import com.jcminarro.roundkornerlayout.RoundKornerRelativeLayout
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.models.SpinnerModel
import com.tekrevol.arrowrecovery.widget.AnyTextView

/**
 */
class ConverterItemAdapter(private val activity: Context, private val arrData: List<DummyModel>, private val onItemClick: OnItemClickListener) : RecyclerView.Adapter<ConverterItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_converter, parent, false)
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
        holder.contParent.setOnClickListener { onItemClick.onItemClick(holder.adapterPosition, model, it, ConverterItemAdapter::class.java.simpleName) }
        holder.contAddToCart.setOnClickListener { onItemClick.onItemClick(holder.adapterPosition, model, it, ConverterItemAdapter::class.java.simpleName) }
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contParent = view.findViewById<RoundKornerLinearLayout>(R.id.contParent)
        val contAddToCart = view.findViewById<RoundKornerRelativeLayout>(R.id.contAddToCart)
        val txtMake = view.findViewById<TextView>(R.id.txtMake)
        val txtModel = view.findViewById<TextView>(R.id.txtModel)
        val txtPrice = view.findViewById<TextView>(R.id.txtPrice)
        val imgConverter = view.findViewById<ImageView>(R.id.imgConverter)
        var model: DummyModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: DummyModel?, context: Context) {
            this.model = model

            this.model?.let {
                txtMake.text = it.text
            }


        }
    }


}