package com.kl3jvi.sysinfo.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.viewpager.widget.ViewPager
import com.example.sysinfo.R
import com.example.sysinfo.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
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

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs

        val actionBar = supportActionBar
        actionBar?.elevation = 0f


        for (i in 0..tabs.size) {
            tabs.getTabAt(i)?.setIcon(tabIcons[i])
        }
        viewPager.offscreenPageLimit = 5
        tabs.setupWithViewPager(viewPager)


    }


}