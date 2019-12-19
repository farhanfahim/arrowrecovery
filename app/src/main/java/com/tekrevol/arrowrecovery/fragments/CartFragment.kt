package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.recyleradapters.CartAdapter
import com.tekrevol.arrowrecovery.adapters.recyleradapters.MyOrderAdapter
import com.tekrevol.arrowrecovery.callbacks.OnItemClickListener
import com.tekrevol.arrowrecovery.constatnts.Constants
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.fragments.dialogs.CheckoutDialogFragment
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.models.DummyModel
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.btnDelete
import kotlinx.android.synthetic.main.fragment_cart.cbSelectAll
import kotlinx.android.synthetic.main.fragment_myorder.*
import kotlinx.android.synthetic.main.fragment_notification.*

class CartFragment : BaseFragment(), OnItemClickListener {

    private var arrData: ArrayList<DummyModel> = ArrayList()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cartAdapter = CartAdapter(context!!, arrData, this)

    }

    companion object {

        fun newInstance(): CartFragment {

            val args = Bundle()

            val fragment = CartFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBind()


    }

    private fun onBind() {
        arrData.clear()
        arrData.addAll(Constants.notifications())

        recyclerViewCart.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerViewCart.adapter = cartAdapter
    }


    override fun getDrawerLockMode(): Int {
        return 0
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_cart
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {
        btnCheckout.setOnClickListener{
            val checkoutDialogFragment = CheckoutDialogFragment()
            checkoutDialogFragment.show(baseActivity.supportFragmentManager, "CheckoutDialogFragment")
        }

        cbSelectAll.setOnCheckedChangeListener { buttonView, isChecked ->
            arrData.forEach { it.isSelected = isChecked }
            cartAdapter.notifyDataSetChanged()
        }

        btnDelete.setOnClickListener {
            UIHelper.showAlertDialog("Are you sure you want to delete selected Items?", "Delete Items", { dialog, which ->
                arrData.removeAll { it.isSelected }
                cartAdapter.notifyDataSetChanged()
                dialog.dismiss()
            }, context)
        }
    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun onItemClick(position: Int, `object`: Any?, view: View?, type: String?) {
        when(view?.id){
            R.id.imgSelect -> {
                arrData[position].isSelected = !arrData[position].isSelected
                cartAdapter.notifyDataSetChanged()
            }
            R.id.contSelectQuality -> {}
            R.id.imgAdd -> {}
            R.id.imgSubtract -> {}
        }
    }

}