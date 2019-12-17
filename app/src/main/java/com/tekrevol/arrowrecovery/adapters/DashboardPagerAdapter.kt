package com.tekrevol.arrowrecovery.adapters

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tekrevol.arrowrecovery.fragments.*

class DashboardPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    // CURRENT FRAGMENT
    private val registeredFragments = SparseArray<Fragment>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment.newInstance()
            1 -> ConverterFragment.newInstance()
            2 -> CartFragment.newInstance()
            3 -> CustomerSupportFragment.newInstance()
            4 -> ProfileFragment.newInstance()
            else -> ProfileFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 5
    }

}