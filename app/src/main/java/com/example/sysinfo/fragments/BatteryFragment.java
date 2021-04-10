package com.example.sysinfo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sysinfo.R;

public class BatteryFragment extends Fragment {

    private View fragmentView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.battery_fragment, container, false);

        if (context == null) {
            context = requireContext();
        }
        return fragmentView;
    }
}
