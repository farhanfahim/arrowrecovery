package com.tekrevol.arrowrecovery.adapters

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tekrevol.arrowrecovery.fragments.AccountFragment
import com.tekrevol.arrowrecovery.fragments.AddressFragment
import com.tekrevol.arrowrecovery.fragments.ContactFragment
import com.tekrevol.arrowrecovery.fragments.PersonalFragment


class RegisterPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    // CURRENT FRAGMENT
    private val registeredFragments = SparseArray<Fragment>()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }


    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return AccountFragment.newInstance()

            1 -> return PersonalFragment.newInstance()

            2 -> return ContactFragment.newInstance()

            3 -> return AddressFragment.newInstance()

            else -> return AddressFragment.newInstance()
        }
    }


    override fun getCount(): Int {
        return 5
    }

}