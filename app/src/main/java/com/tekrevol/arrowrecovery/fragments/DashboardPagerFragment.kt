package com.tekrevol.arrowrecovery.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.drawerlayout.widget.DrawerLayout
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.tekrevol.arrowrecovery.R
import com.tekrevol.arrowrecovery.adapters.DashboardPagerAdapter
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment
import com.tekrevol.arrowrecovery.widget.TitleBar
import kotlinx.android.synthetic.main.fragment_dashboard_pager.*

class DashboardPagerFragment : BaseFragment() {

    var positionToSelect = 0
    private var adapter: DashboardPagerAdapter? = null

    override fun getDrawerLockMode(): Int {
        return DrawerLayout.LOCK_MODE_LOCKED_CLOSED
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_dashboard_pager
    }

    override fun setTitlebar(titleBar: TitleBar) {
        titleBar.resetViews()
        titleBar.visibility = View.GONE

        // Using own title bar
        dashboardTitleBar.resetViews()
        dashboardTitleBar.visibility = View.VISIBLE

        when (positionToSelect) {
            0 -> dashboardTitleBar.setTitle("Frag 1")
            1 -> dashboardTitleBar.setTitle("Frag 2")
            2 -> dashboardTitleBar.setTitle("Frag 3")
            3 -> dashboardTitleBar.setTitle("Frag 4")
            4 -> dashboardTitleBar.setTitle("Frag 5")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
//        setCurrentItem(positionToSelect)
        //        tabs.setupWithViewPager(viewpager);
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
            0 -> dashboardTitleBar.setTitle("Frag 1")
            1 -> dashboardTitleBar.setTitle("Frag 2")
            2 -> dashboardTitleBar.setTitle("Frag 3")
            3 -> dashboardTitleBar.setTitle("Frag 4")
            4 -> dashboardTitleBar.setTitle("Frag 5")
        }

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