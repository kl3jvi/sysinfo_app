package com.kl3jvi.sysinfo.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.example.sysinfo.R
import com.example.sysinfo.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.kl3jvi.sysinfo.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

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
        setupViewPager()
        /* Setting up the navigation controller and the action bar with the navigation controller. */
        navController = findNavController(binding.fragContainer)
        setupActionBarWithNavController(this, navController)
    }

    private fun setupViewPager() {
        val adapter = SectionsPagerAdapter(this, this)
        with(binding) {
            viewPager.adapter = adapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = adapter.getPageTitle(position)
                tab.setIcon(tabIcons[position])
            }.attach()
        }
        supportActionBar?.elevation = 0f
        binding.viewPager.offscreenPageLimit = 5
    }
}
