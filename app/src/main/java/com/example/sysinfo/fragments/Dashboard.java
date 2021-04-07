package com.example.sysinfo.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sysinfo.R;
import com.example.sysinfo.adapters.CPUDetails;
import com.example.sysinfo.adapters.CustomCPUAdapter;
import com.example.sysinfo.ui.main.BatteryInfo;
import com.example.sysinfo.ui.main.ChargingReciever;
import com.example.sysinfo.utils.DeviceInformation;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.util.ArrayList;
import java.util.Random;


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
    private ArrayList arrayList;
    private CustomCPUAdapter customCPUAdapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.dashboard_fragment, container, false);
        if(context == null){
            context = requireContext();
        }
        init();
        updateView();
//        MAX = deviceInformation.getMaxCpuFrequency();
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
//        sensorsNo = fragmentView.findViewById(R.id.textView5);
//        appNo = fragmentView.findViewById(R.id.textView6);


        //        Storage variables
        totalExternalStorage = (int) deviceInformation.totalExternalMemory();
        availableExternalStorage = (int) deviceInformation.availableExternalMemory();
        storagePercentage = deviceInformation.calculatePercentage(totalExternalStorage - availableExternalStorage, totalExternalStorage);

        //        Battery Variables
        batteryInt = batteryInfo.batteryPercentage();

        //         ArrayList
        arrayList = new ArrayList();
        for (int i = 0; i < deviceInformation.getNumOfCores(); i++) {
            arrayList.add(new CPUDetails(R.drawable.ic_cpu, "Core " + (i), deviceInformation.getFrequencyOfCore(i) + " MHz",deviceInformation.getMaxCpuFrequency(i)+"MHz"));
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

//        sensorsNo.setText(deviceInformation.getNumberOfSensors() + " Sensors");
//        appNo.setText(deviceInformation.getNumberOfApps() + " Apps");

        // ram progress
        checkForRamChanges();

    }


    public void checkForRamChanges() {
        totalRam = (int) deviceInformation.getTotalRam();
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                availableRam = (int) deviceInformation.getAvailableRam();
                memoryProgress = deviceInformation.calculatePercentage(totalRam - availableRam, totalRam);
                arcProgress.setProgress(memoryProgress);

                ramTxt.setText(totalRam - availableRam + " / " + totalRam + " MB");

                for (int i = 0; i < deviceInformation.getNumOfCores(); i++) {
                    int coreFreq = deviceInformation.getFrequencyOfCore(i);
                    int progressPercentage = deviceInformation.calculatePercentage(coreFreq, deviceInformation.getMaxCpuFrequency(i));
                    arrayList.set(i, new CPUDetails(progressPercentage, "Core " + (i), deviceInformation.getFrequencyOfCore(i) + "MHz",deviceInformation.getMaxCpuFrequency(i)+" MHz"));
                    customCPUAdapter.notifyDataSetChanged();

                }
                handler.postDelayed(this, 2000);



            }
        };
    }


    public void cpuHandler() {

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
        handler.postDelayed(runnable, 2000);
    }
}
