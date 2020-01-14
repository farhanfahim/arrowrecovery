package com.tekrevol.arrowrecovery.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.drawerlayout.widget.DrawerLayout
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.DashboardPagerAdapter
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_dashboard_pager.*
import kotlinx.android.synthetic.main.titlebar_main.*

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
            2 -> dashboardTitleBar.setTitle("Cart")
            3 -> dashboardTitleBar.setTitle("Customer Support")
            4 -> setProfileTitleBar()
        }
        if (positionToSelect == 2 || positionToSelect == 3) {
            dashboardTitleBar.hideSearch()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardTitleBar = view.findViewById(R.id.dashboardTitleBar)

        if (onCreated) {
            setViewPagerAdapter()
        } else {
            adapter = DashboardPagerAdapter(childFragmentManager)
            setViewPagerAdapter()
        }
    }


    private fun setViewPagerAdapter() {
        navigationBar.add(MeowBottomNavigation.Model(0, R.drawable.homecoloredicon))
        navigationBar.add(MeowBottomNavigation.Model(1, R.drawable.coloredconvertericon))
        navigationBar.add(MeowBottomNavigation.Model(2, R.drawable.coloredcarticon))
        navigationBar.add(MeowBottomNavigation.Model(3, R.drawable.coloredcontacticon))
        navigationBar.add(MeowBottomNavigation.Model(4, R.drawable.coloredprofileicon))

        viewpager.adapter = adapter
        viewpager.setPagingEnabled(false)
        viewpager.offscreenPageLimit = 4
//        setCurrentItem(positionToSelect
        navigationBar.show(positionToSelect, true)
    }


    override fun setListeners() {
        // Check model.id for position
        navigationBar.setOnClickMenuListener { model ->
            setCurrentItem(model.id)
        }
    }


    private fun setCurrentItem(position: Int) {
        positionToSelect = position
        viewpager!!.setCurrentItem(position, true)
        //        ((BaseFragment)adapter.getItem(position)).setTitlebar(getHomeActivity().getTitleBar());
        dashboardTitleBar.resetViews()
        dashboardTitleBar.visibility = View.VISIBLE

        when (position) {
            0 -> dashboardTitleBar.showTitleImage()
            1 -> dashboardTitleBar.showTitleImage()
            2 -> dashboardTitleBar.setTitle("Cart")
            3 -> dashboardTitleBar.setTitle("Customer Support")
            4 -> {
                setProfileTitleBar()

            }
        }

        if (positionToSelect == 2 || positionToSelect == 3) {
            btnRightSearch.visibility = View.GONE
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