package com.example.sysinfo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sysinfo.R;
import com.example.sysinfo.utils.CpuUtils;
import com.example.sysinfo.utils.DeviceInformation;


public class CPU extends Fragment {

    private View fragmentView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.cpu_fragment, container, false);
        CpuUtils cpuUtils = new CpuUtils();
        System.out.println(cpuUtils.getCpuAbi()+"------------------");

        return fragmentView;
    }
}
