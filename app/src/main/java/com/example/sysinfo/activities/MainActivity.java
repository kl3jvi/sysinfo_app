package com.example.sysinfo.activities;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.sysinfo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.ActionBar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import com.example.sysinfo.ui.main.SectionsPagerAdapter;
import com.example.sysinfo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    private ActivityMainBinding binding;

    private int[] tabIcons={
            R.drawable.ic_dashboard,
            R.drawable.ic_phone,
            R.drawable.ic_system,
            R.drawable.ic_cpu
    };
    private TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        setupTabsIcon();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
//            actionBar.setElevation(0);
        }
    }


    public void setupTabsIcon() {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.getTabAt(i).setIcon(tabIcons[i]);
        }
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

}