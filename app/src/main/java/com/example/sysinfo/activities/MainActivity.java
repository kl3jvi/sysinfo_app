package com.example.sysinfo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.sysinfo.R;
import com.example.sysinfo.databinding.ActivityMainBinding;
import com.example.sysinfo.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;

    private final int[] tabIcons = {
            R.drawable.ic_dashboard,
            R.drawable.ic_devicee,
            R.drawable.ic_system,
            R.drawable.ic_storage,
            R.drawable.ic_cpu,
            R.drawable.ic_battery,
            R.drawable.ic_screen
    };
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.sysinfo.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        int NUMBER =tabs.getTabCount()-1;
        viewPager.setOffscreenPageLimit(NUMBER); // TODO Never forget to increment this if you add another page ; (N-1) N->Number of pages
        setupTabsIcon();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
        }


    }

    public void setupTabsIcon() {


        for (int i = 0; i < tabs.getTabCount(); i++) {
            Objects.requireNonNull(tabs.getTabAt(i)).setIcon(tabIcons[i]);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if (id == R.id.about) {
            Intent about = new Intent(this, AboutActivity.class);
            startActivity(about);
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}