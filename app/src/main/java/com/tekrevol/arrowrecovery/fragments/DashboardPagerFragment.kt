package com.tekrevol.arrowrecovery.fragments

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.activities.MainActivity
import com.tekrevol.arrowrecovery.adapters.DashboardPagerAdapter
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.helperclasses.GooglePlaceHelper
import com.tekrevol.arrowrecovery.helperclasses.RunTimePermissions
import com.tekrevol.arrowrecovery.helperclasses.ui.helper.UIHelper
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_dashboard_pager.*

class DashboardPagerFragment : BaseFragment() {

    var positionToSelect = 0
    private var adapter: DashboardPagerAdapter? = null
    lateinit var dashboardTitleBar: TitleBar

    override fun getDrawerLockMode(): Int {
        return DrawerLayout.LOCK_MODE_LOCKED_CLOSED
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_dashboard_pager
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.resetViews()
        titleBar.visibility = View.GONE
        dashboardTitleBar.btnSearch(homeActivity, View.OnClickListener {
            baseActivity.addDockableFragment(SearchFragment.newInstance(), true)
        })
        dashboardTitleBar.btnNotification(homeActivity, View.OnClickListener {
            baseActivity.addDockableFragment(NotificationFragment.newInstance(), true)
        })
        // Using own title bar
        dashboardTitleBar.resetViews()
        dashboardTitleBar.visibility = View.VISIBLE

        when (positionToSelect) {
            0 -> dashboardTitleBar.showTitleImage()
            1 -> dashboardTitleBar.showTitleImage()
            2 -> {
                dashboardTitleBar.hideSearch()
                dashboardTitleBar.setTitle("Cart")
            }
            3 -> {
                dashboardTitleBar.hideSearch()
                dashboardTitleBar.setTitle("Customer Support")
            }
            4 -> setProfileTitleBar()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardTitleBar = view.findViewById(R.id.dashboardTitleBar)
/*
        if (onCreated) {
            setViewPagerAdapter()
        } else {
            adapter = DashboardPagerAdapter(childFragmentManager)
            setViewPagerAdapter()
        }*/
        addFragment(HomeFragment.newInstance(), "HomeFragment")
        setCurrentItem(0)
        setViewPagerAdapter()
    }


    private fun setViewPagerAdapter() {
        navigationBar.add(MeowBottomNavigation.Model(0, R.drawable.homecoloredicon))
        navigationBar.add(MeowBottomNavigation.Model(1, R.drawable.coloredconvertericon))
        navigationBar.add(MeowBottomNavigation.Model(2, R.drawable.coloredcarticon))
        navigationBar.add(MeowBottomNavigation.Model(3, R.drawable.coloredcontacticon))
        navigationBar.add(MeowBottomNavigation.Model(4, R.drawable.coloredprofileicon))
//        viewpager.adapter = adapter
//        viewpager.setPagingEnabled(false)
//        viewpager.offscreenPageLimit = 4
        navigationBar.show(positionToSelect, false)

    }


    override fun setListeners() {
        // Check model.id for position

     /*   navigationBar.setOnClickMenuListener { model ->

            setCurrentItem(model.id)
        }

        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

                if (position == 2) {
                    adapter?.let {
                        val cartFragment = it.registeredFragments[2] as CartFragment?
                        cartFragment?.myCartApi(true)
                    }

                }

            }
        })*/

        navigationBar.setOnClickMenuListener { model ->

            setCurrentItem(model.id)

            when(model.id){
                0 -> replaceFragment(HomeFragment.newInstance(), "HomeFragment")
                1 -> replaceFragment(ConverterDashboardFragment.newInstance(), "ConverterDashboardFragment")
                2 -> replaceFragment(CartFragment.newInstance(), "CartFragment")
                3 -> replaceFragment(CustomerSupportFragment.newInstance(), "CustomerSupportFragment")
                4 -> replaceFragment(ProfileFragment.newInstance(), "ProfileFragment")
            }

            navigationBar.show(model.id, false)
        }
    }

    private fun setCurrentItem(position: Int) {

        positionToSelect = position
        //viewpager!!.setCurrentItem(position, true)
        //        ((BaseFragment)adapter.getItem(position)).setTitlebar(getHomeActivity().getTitleBar());
        dashboardTitleBar.resetViews()
        dashboardTitleBar.visibility = View.VISIBLE

        when (position) {
            0 -> dashboardTitleBar.showTitleImage()
            1 -> dashboardTitleBar.showTitleImage()
            2 -> {
                dashboardTitleBar.hideSearch()
                dashboardTitleBar.setTitle("Cart")

            }
            3 -> {
                dashboardTitleBar.hideSearch()
                dashboardTitleBar.setTitle("Customer Support")
            }
            4 -> setProfileTitleBar()

        }

    }

    private fun setProfileTitleBar() {
        dashboardTitleBar.setTitle("Profile")
        dashboardTitleBar.showEditProfile(View.OnClickListener {

            baseActivity.addDockableFragment(EditProfileFragment.newInstance(), false)

        })
    }

    override fun onClick(view: View) {}
    override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {}

    companion object {
        fun newInstance(): DashboardPagerFragment {
            val args = Bundle()
            val fragment = DashboardPagerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}