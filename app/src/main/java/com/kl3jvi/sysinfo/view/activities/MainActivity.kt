package com.kl3jvi.sysinfo.view.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.example.sysinfo.R
import com.example.sysinfo.databinding.ActivityMainBinding
import com.kl3jvi.sysinfo.utils.WifiConnectionMonitor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivity : AppCompatActivity(), KoinComponent {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* Setting up the navigation controller and the action bar with the navigation controller. */
        navController = findNavController(this, R.id.nav_host_fragment_activity_main)
        setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}


