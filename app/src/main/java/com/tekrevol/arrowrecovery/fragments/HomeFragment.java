package com.tekrevol.arrowrecovery.fragments;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;

import android.view.View;
import android.widget.AdapterView;

import com.tekrevol.arrowrecovery.R;
import com.tekrevol.arrowrecovery.fragments.abstracts.BaseFragment;
import com.tekrevol.arrowrecovery.widget.TitleBar;

public class HomeFragment extends BaseFragment {

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getDrawerLockMode() {
        return DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_dashboard;
    }

    @Override
    public void setTitlebar(TitleBar titleBar) {

    /*    titleBar.setVisibility(View.VISIBLE);
        titleBar.showSidebar(getBaseActivity());
        titleBar.showResideMenu(getHomeActivity());*/
    }

    @Override
    public void setListeners() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
