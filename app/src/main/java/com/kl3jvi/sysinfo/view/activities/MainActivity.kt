package com.kl3jvi.sysinfo.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.sysinfo.R
import com.example.sysinfo.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kl3jvi.sysinfo.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val tabIcons = intArrayOf(
        R.drawable.ic_dashboard,
        R.drawable.ic_devicee,
        R.drawable.ic_system,
        R.drawable.ic_storage,
        R.drawable.ic_cpu,
        R.drawable.ic_battery,
        R.drawable.ic_screen
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, this)
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabs
        viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = sectionsPagerAdapter.getPageTitle(position)
            tab.setIcon(tabIcons[position])
        }.attach()

        val actionBar = supportActionBar
        actionBar?.elevation = 0f
        viewPager.offscreenPageLimit = 5
    }
}
