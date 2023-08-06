package com.kl3jvi.sysinfo.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sysinfo.R
import com.kl3jvi.sysinfo.view.fragments.BatteryFragment
import com.kl3jvi.sysinfo.view.fragments.CPU
import com.kl3jvi.sysinfo.view.fragments.Dashboard
import com.kl3jvi.sysinfo.view.fragments.DeviceFragment
import com.kl3jvi.sysinfo.view.fragments.ScreenFragment
import com.kl3jvi.sysinfo.view.fragments.StorageFragment
import com.kl3jvi.sysinfo.view.fragments.SystemFrag

class SectionsPagerAdapter(
    private val context: Context,
    fm: FragmentActivity
) : FragmentStateAdapter(fm) {

    enum class TabType(val titleResId: Int, val fragment: () -> Fragment) {
        DASHBOARD(R.string.tab_text_1, ::Dashboard),
        DEVICE(R.string.tab_text_2, ::DeviceFragment),
        SYSTEM(R.string.tab_text_3, ::SystemFrag),
        STORAGE(R.string.tab_text_4, ::StorageFragment),
        CPU(R.string.tab_text_5, { CPU() }),
        BATTERY(R.string.tab_text_6, ::BatteryFragment),
        SCREEN(R.string.tab_text_7, ::ScreenFragment);

        companion object {
            fun fromPosition(position: Int) = values().getOrElse(position) {
                throw IllegalStateException("Invalid position: $position")
            }
        }
    }

    override fun getItemCount(): Int = TabType.values().size

    override fun createFragment(position: Int): Fragment {
        return TabType.fromPosition(position).fragment.invoke()
    }

    fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TabType.fromPosition(position).titleResId)
    }
}