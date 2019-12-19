package com.tekrevol.arrowrecovery.adapters.recyleradapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.widget.AnyTextView

/**
 */
class CartAdapter(private val activity: Context?, private val arrData: List<DummyModel>, private val onItemClick: OnItemClickListener) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(activity)
                .inflate(R.layout.item_cart, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val model = arrData[i]
        with(holder) {
            bindTo(model, activity!!)
            setListener(this, model)
        }
    }

    private fun setListener(holder: ViewHolder, model: DummyModel) {
           holder.imgSelect.setOnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model, v, CartAdapter::class.java.simpleName) }
           holder.contSelectQuality.setOnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model, v, CartAdapter::class.java.simpleName) }
           holder.btnSubtract.setOnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model, v, CartAdapter::class.java.simpleName) }
           holder.btnAdd.setOnClickListener { v -> onItemClick.onItemClick(holder.adapterPosition, model, v, CartAdapter::class.java.simpleName) }
    }

    override fun getItemCount(): Int {
        return arrData.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgSelect = view.findViewById<ImageView>(R.id.imgSelect)
        val btnAdd = view.findViewById<ImageView>(R.id.btnAdd)
        val btnSubtract = view.findViewById<ImageView>(R.id.btnSubtract)
        val contSelectQuality = view.findViewById<LinearLayout>(R.id.contSelectQuality)
        var model: DummyModel? = null

        /**
         * Items might be null if they are not paged in yet. PagedListAdapter will re-bind the
         * ViewHolder when Item is loaded.
         */
        fun bindTo(model: DummyModel?, context: Context) {
            this.model = model

            this.model?.let {
                if (it.isSelected) {
                    imgSelect.setImageResource(R.drawable.img_selecte_check)
                } else {
                    imgSelect.setImageResource(R.drawable.img_unselected_check)
                }
            }


        }
    }

}