package com.example.sysinfo.fragments;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sysinfo.R;
import com.example.sysinfo.adapters.CPUDetails;
import com.example.sysinfo.adapters.CustomCPUAdapter;
import com.example.sysinfo.ui.main.BatteryInfo;
import com.example.sysinfo.ui.main.ChargingReciever;
import com.example.sysinfo.utils.DeviceInformation;
import com.github.lzyzsd.circleprogress.ArcProgress;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class Dashboard extends Fragment {

    private View fragmentView;
    private ProgressBar internalP, batteryP;
    private TextView internalPercentage, batteryPercentage, ramTxt;
    private DeviceInformation deviceInformation;
    private BatteryInfo batteryInfo;
    private int totalExternalStorage;
    private int availableExternalStorage;
    private int storagePercentage;

    private int batteryInt;
    private ArcProgress arcProgress;
    private int availableRam, totalRam;
    private int memoryProgress;
    private Handler handler;
    private ChargingReciever chargingReciever;
    private Runnable runnable;
    private ListView listView;
    private ArrayList<CPUDetails> arrayList;
    private CustomCPUAdapter customCPUAdapter;
    private TextView sensorsNo;
    private TextView appNo;
    private int MAX;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.dashboard_fragment, container, false);
        init();
        updateView();
        MAX = deviceInformation.getMaxCpuFrequency();
        return fragmentView;
    }

    public void init() {
        deviceInformation = new DeviceInformation(requireContext(), requireActivity());
        batteryInfo = new BatteryInfo(getContext());

        //       Find Viewt
        internalP = fragmentView.findViewById(R.id.internalProgress);
        batteryP = fragmentView.findViewById(R.id.batteryProgress);
        internalPercentage = fragmentView.findViewById(R.id.internalPercentage);
        batteryPercentage = fragmentView.findViewById(R.id.batteryPercentage);
        arcProgress = fragmentView.findViewById(R.id.arc_progress);
        totalRam = (int) deviceInformation.getTotalRam();
        availableRam = (int) deviceInformation.getAvailableRam();
        memoryProgress = deviceInformation.calculatePercentage(totalRam - availableRam, totalRam);
        ObjectAnimator.ofInt(arcProgress, "progress", memoryProgress)
                .setDuration(1000)
                .start();

        ramTxt = fragmentView.findViewById(R.id.ramTxt);
        listView = fragmentView.findViewById(R.id.listView);
        sensorsNo = fragmentView.findViewById(R.id.textView5);
        appNo = fragmentView.findViewById(R.id.textView6);

        //        Storage variables
        totalExternalStorage = (int) deviceInformation.totalExternalMemory();
        availableExternalStorage = (int) deviceInformation.availableExternalMemory();
        storagePercentage = deviceInformation.calculatePercentage(totalExternalStorage - availableExternalStorage, totalExternalStorage);

        //        Battery Variables
        batteryInt = batteryInfo.batteryPercentage();

        //         ArrayList
        arrayList = new ArrayList();
        for (int i = 0; i < deviceInformation.getNumOfCores(); i++) {
            arrayList.add(new CPUDetails(R.drawable.ic_cpu, "Core " + (i + 1), deviceInformation.getFrequencyOfCore(i) + " MHz"));
        }


        customCPUAdapter = new CustomCPUAdapter(getContext(), R.layout.cpu_list, arrayList);
        listView.setAdapter(customCPUAdapter);


    }

    public void updateView() {
        // storage progress
        internalP.setProgress(storagePercentage);
        internalPercentage.setText(storagePercentage + " %");

        // battery progress
        batteryPercentage.setText(batteryInt + " %");
        batteryP.setProgress(batteryInt);

        sensorsNo.setText(deviceInformation.getNumberOfSensors() + " Sensors");
        appNo.setText(deviceInformation.getNumberOfApps() + " Apps");

        // ram progress
        checkForRamChanges();

    }


    public void checkForRamChanges() {

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                totalRam = (int) deviceInformation.getTotalRam();
                availableRam = (int) deviceInformation.getAvailableRam();
                memoryProgress = deviceInformation.calculatePercentage(totalRam - availableRam, totalRam);
                arcProgress.setProgress(memoryProgress);


                ramTxt.setText(totalRam - availableRam + " / " + totalRam + " MB");

                System.out.println(deviceInformation.getNumberOfSensors());
                handler.postDelayed(this, 1000);
                for (int i = 0; i < deviceInformation.getNumOfCores(); i++) {
                    int coreFreq = deviceInformation.getFrequencyOfCore(i);
                    int progressPercentage = deviceInformation.calculatePercentage(coreFreq, MAX);
                    arrayList.set(i, new CPUDetails(progressPercentage, "Core " + (i + 1), deviceInformation.getFrequencyOfCore(i) + "MHz"));
                    customCPUAdapter.notifyDataSetChanged();
                }
//
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
        requireActivity().unregisterReceiver(chargingReciever);
    }

    @Override
    public void onResume() {
        super.onResume();
        chargingReciever = new ChargingReciever(batteryP, batteryPercentage, getContext());
        IntentFilter ifilter = new IntentFilter();
        ifilter.addAction(Intent.ACTION_POWER_CONNECTED);
        ifilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        getActivity().registerReceiver(chargingReciever, ifilter);
        handler.postDelayed(runnable, 1000);
    }

}
