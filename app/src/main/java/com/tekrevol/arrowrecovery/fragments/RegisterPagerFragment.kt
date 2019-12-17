package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.RegisterPagerAdapter
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_register.*

private var adapter: RegisterPagerAdapter? = null


class RegisterPagerFragment : BaseFragment() {

    var positionToSelect: Int = 0


    companion object {
        fun newInstance(): Fragment {
            val args = Bundle()
            val fragment = RegisterPagerFragment()
            fragment.positionToSelect = 0
            fragment.arguments = args
            return fragment
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        txt_login.setOnClickListener {
            baseActivity.popBackStack()
            baseActivity.addDockableFragment(LoginFragmentt.newInstance(), true)
        }

        btnnext.setOnClickListener {
            if (positionToSelect < 3) {
                setCurrentItemByPosition(positionToSelect + 1)
            } else {
                baseActivity.addDockableFragment(OptVerification.newInstance(), true)
            }
        }

        btnBack.setOnClickListener {
            setCurrentItemByPosition(positionToSelect - 1)
        }

    }

    override fun onClick(v: View?) {
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    private fun setViewPagerAdapter() {

        viewpager.adapter = adapter
        viewpager.setPagingEnabled(false)
        viewpager.offscreenPageLimit = 4
        setCurrentItemByPosition(positionToSelect)

        //        tabs.setupWithViewPager(viewpager);
    }

    fun setCurrentItemByPosition(position: Int) {
        positionToSelect = position
        resetStates()
        setStates()
        viewpager.setCurrentItem(position, true)

        when (positionToSelect) {
            0 -> {
                title.text = "Account Information"
                btnnext.text = "Next"
                btnBack.visibility = View.GONE
                socialloginLayout.visibility = View.VISIBLE
            }
            1 -> {
                socialloginLayout.visibility = View.GONE
                title.text = "Personal Information"
                btnnext.text = "Next"
                btnBack.visibility = View.VISIBLE
            }
            2 -> {
                title.text = "Contact Information"
                btnnext.text = "Next"
                btnBack.visibility = View.VISIBLE
                socialloginLayout.visibility = View.GONE
            }

            3 -> {
                title.text = "Address"
                btnnext.text = "Sign Up"
                btnBack.visibility = View.VISIBLE
                socialloginLayout.visibility = View.GONE


            }
        }
    }


    private fun resetStates(){
        viewPersonal.setBackgroundColor(resources.getColor(R.color.star_grey))
        imgPersonal.setImageResource(R.drawable.personallogogrey)
        txtPersonal.setTextColor(resources.getColor(R.color.star_grey))

        viewContact.setBackgroundColor(resources.getColor(R.color.star_grey))
        imgContact.setImageResource(R.drawable.contactlogogrey)
        txtContact.setTextColor(resources.getColor(R.color.star_grey))

        viewAddress.setBackgroundColor(resources.getColor(R.color.star_grey))
        imgAddress.setImageResource(R.drawable.addressgrey)
        txtAddress.setTextColor(resources.getColor(R.color.star_grey))
    }

    private fun setStates(){
        when (positionToSelect) {
            1 -> {
                viewPersonal.setBackgroundColor(resources.getColor(R.color.green_bg))
                imgPersonal.setImageResource(R.drawable.personalcoloredlogo)
                txtPersonal.setTextColor(resources.getColor(R.color.c_black))
            }
            2 -> {
                viewPersonal.setBackgroundColor(resources.getColor(R.color.green_bg))
                imgPersonal.setImageResource(R.drawable.personalcoloredlogo)
                txtPersonal.setTextColor(resources.getColor(R.color.c_black))

                viewContact.setBackgroundColor(resources.getColor(R.color.green_bg))
                imgContact.setImageResource(R.drawable.contact)
                txtContact.setTextColor(resources.getColor(R.color.c_black))
            }
            3 -> {
                viewPersonal.setBackgroundColor(resources.getColor(R.color.green_bg))
                imgPersonal.setImageResource(R.drawable.personalcoloredlogo)
                txtPersonal.setTextColor(resources.getColor(R.color.c_black))

                viewContact.setBackgroundColor(resources.getColor(R.color.green_bg))
                imgContact.setImageResource(R.drawable.contact)
                txtContact.setTextColor(resources.getColor(R.color.c_black))

                viewAddress.setBackgroundColor(resources.getColor(R.color.green_bg))
                imgAddress.setImageResource(R.drawable.addresscolered)
                txtAddress.setTextColor(resources.getColor(R.color.c_black))
            }
        }
    }


}


