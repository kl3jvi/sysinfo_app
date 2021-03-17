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

public class CPU extends Fragment {

    private View fragmentView;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.cpu_fragment, container, false);

        return fragmentView;
    }
}
