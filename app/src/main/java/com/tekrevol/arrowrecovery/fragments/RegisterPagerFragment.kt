package com.tekrevol.arrowrecovery.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.RegisterPagerAdapter
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.AnyTextView
import com.tekrevol.arrowrecovery.widget.CustomViewPager
import com.tekrevol.arrowrecovery.widget.TitleBar

private var adapter: RegisterPagerAdapter? = null


class RegisterPagerFragment : BaseFragment() {

    var viewpager: CustomViewPager? = null
    var title: AnyTextView? = null
    var txt_login: AnyTextView? = null
    var btnBack: LinearLayout? = null
    var positionToSelect: Int = 0
    var btnnext: TextView? = null
    var txtPersonal: TextView? = null
    var txtContact: TextView? = null
    var viewPersonal: View? = null
    var viewContact: View? = null
    var imgPersonal: ImageView? = null
    var imgContact: ImageView? = null

    companion object {
        @JvmStatic
        fun newInstance(i: Int): Fragment {

            val args = Bundle()

            val fragment = RegisterPagerFragment()
            fragment.positionToSelect = i

            fragment.arguments = args
            return fragment

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewpager = view.findViewById(R.id.viewpager)
        title = view.findViewById(R.id.title)
        txt_login = view.findViewById(R.id.txt_login)
        btnnext = view.findViewById(R.id.btnnext)
        btnBack = view.findViewById(R.id.btnBack)
        viewPersonal = view.findViewById(R.id.viewPersonal)
        imgPersonal = view.findViewById(R.id.imgPersonal)
        txtPersonal = view.findViewById(R.id.txtPersonal)
        viewContact = view.findViewById(R.id.viewContact)
        imgContact = view.findViewById(R.id.imgContact)
        txtContact = view.findViewById(R.id.txtContact)

        if (onCreated) {
            setViewPagerAdapter()
        } else {
            adapter = RegisterPagerAdapter(childFragmentManager)
            setViewPagerAdapter()
        }

    }

    override fun getDrawerLockMode(): Int {
        return 0

    }


    override fun getFragmentLayout(): Int {
        return R.layout.fragment_register
    }

    override fun setTitlebar(titleBar: TitleBar?) {
    }

    override fun setListeners() {

        btnnext?.setOnClickListener(View.OnClickListener {

            if (positionToSelect < 3) {

                if (positionToSelect == 0) {
                    viewPersonal?.setBackgroundColor(resources.getColor(R.color.green_bg))
                    imgPersonal?.setImageResource(R.drawable.personalcoloredlogo)
                    txtPersonal?.setTextColor(getResources().getColor(R.color.c_black))

                } else if (positionToSelect == 1) {
                    viewContact?.setBackgroundColor(resources.getColor(R.color.green_bg))
                    imgContact?.setImageResource(R.drawable.contact)
                    txtContact?.setTextColor(getResources().getColor(R.color.c_black))
                }else if(positionToSelect == 2)
                {
                    viewContact?.setBackgroundColor(resources.getColor(R.color.green_bg))
                    imgContact?.setImageResource(R.drawable.contact)
                    txtContact?.setTextColor(getResources().getColor(R.color.c_black))
                }

                val current = positionToSelect + 1
                setCurrentItem(current)

            }
        })

        btnBack?.setOnClickListener(View.OnClickListener {

            val previous = positionToSelect - 1
            setCurrentItem(previous)

        })

    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    private fun setViewPagerAdapter() {

        viewpager?.setAdapter(adapter)
        viewpager?.setPagingEnabled(false)
        viewpager?.setOffscreenPageLimit(4)
        setCurrentItem(positionToSelect)

        //        tabs.setupWithViewPager(viewpager);
    }

    private fun setCurrentItem(position: Int) {
        positionToSelect = position
        viewpager?.setCurrentItem(position, true)

        when (positionToSelect) {
            0 -> {
                title?.setText("Account Information")
                btnBack?.visibility = View.VISIBLE
                btnnext?.setText("Next")
            }
            1 -> {
                title?.setText("Personal Information")
                btnnext?.setText("Next")

                btnBack?.visibility = View.GONE
            }
            2 -> {
                title?.setText("Contact Information")
                btnnext?.setText("Next")

                btnBack?.visibility = View.GONE
            }

            3 -> {
                title?.setText("Address")
                btnnext?.setText("Sign Up")

                btnBack?.visibility = View.GONE
            }

        }
    }


}


