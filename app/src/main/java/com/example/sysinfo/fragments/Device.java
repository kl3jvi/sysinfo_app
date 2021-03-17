package com.example.sysinfo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sysinfo.R;

import org.jetbrains.annotations.NotNull;

public class Device extends Fragment {

    private View fragmentView;



    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.device_fragment, container, false);

        return fragmentView;
    }
}
