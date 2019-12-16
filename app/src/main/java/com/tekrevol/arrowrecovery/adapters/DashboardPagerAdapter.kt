package com.tekrevol.arrowrecovery.adapters

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tekrevol.arrowrecovery.fragments.DummyFragment
import com.tekrevol.arrowrecovery.fragments.LoginFragment

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
            0 -> DummyFragment.newInstance("a")
            1 -> LoginFragment.newInstance()
            2 -> DummyFragment.newInstance("a")
            3 -> LoginFragment.newInstance()
            4 -> DummyFragment.newInstance("a")
            else -> DummyFragment.newInstance("a")
        }
    }

    override fun getCount(): Int {
        return 5
    }

}