package com.kl3jvi.sysinfo.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sysinfo.R
import com.kl3jvi.sysinfo.view.fragments.BatteryFragment
import com.kl3jvi.sysinfo.view.fragments.CPU
import com.kl3jvi.sysinfo.view.fragments.Dashboard
import com.kl3jvi.sysinfo.view.fragments.DeviceFragment
import com.kl3jvi.sysinfo.view.fragments.ScreenFragment
import com.kl3jvi.sysinfo.view.fragments.StorageFragment
import com.kl3jvi.sysinfo.view.fragments.SystemFrag

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentActivity) :
    FragmentStateAdapter(fm) {
    private val TAB_TITLES = intArrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2,
        R.string.tab_text_3,
        R.string.tab_text_4,
        R.string.tab_text_5,
        R.string.tab_text_6,
        R.string.tab_text_7
    )

    override fun getItemCount(): Int {
        return 6
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Dashboard()
            1 -> DeviceFragment()
            2 -> SystemFrag()
            3 -> StorageFragment()
            4 -> CPU()
            5 -> BatteryFragment()
            6 -> ScreenFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }

    fun currentPosition(position: Int) {
        notifyItemChanged(position)
    }


    fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }
}

