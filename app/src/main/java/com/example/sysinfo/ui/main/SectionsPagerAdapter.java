package com.example.sysinfo.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sysinfo.R;
import com.example.sysinfo.fragments.BatteryFragment;
import com.example.sysinfo.fragments.CPU;
import com.example.sysinfo.fragments.Dashboard;
import com.example.sysinfo.fragments.DeviceFragment;
import com.example.sysinfo.fragments.ScreenFragment;
import com.example.sysinfo.fragments.StorageFragment;
import com.example.sysinfo.fragments.SystemFrag;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5, R.string.tab_text_6, R.string.tab_text_7};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Dashboard();
                break;
            case 1:
                fragment = new DeviceFragment();
                break;
            case 2:
                fragment = new SystemFrag();
                break;
            case 3:
                fragment = new StorageFragment();
                break;
            case 4:
                fragment = new CPU();
                break;
            case 5:
                fragment = new BatteryFragment();
                break;
            case 6:
                fragment = new ScreenFragment();
                break;
        }
        assert fragment != null;
        return fragment;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 7 total pages.
        return 7;
    }
}