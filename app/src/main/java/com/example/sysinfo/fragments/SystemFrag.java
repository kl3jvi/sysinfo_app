package com.example.sysinfo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sysinfo.R;
import com.example.sysinfo.utils.DeviceInformation;

public class SystemFrag extends Fragment {

    private View fragmentView;
    private Handler handler;
    private Runnable runnable;
    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.system_fragment, container, false);
        if(context == null){
            context = requireContext();
        }
        DeviceInformation deviceClass = new DeviceInformation(context);

        TextView androidNo = fragmentView.findViewById(R.id.androidNo);
        androidNo.setText("Android " + deviceClass.getOSVersion());

        TextView root = fragmentView.findViewById(R.id.root);
        if (!deviceClass.isDeviceRooted()) {
            root.setText("Root: Not Rooted");
        } else if (deviceClass.isDeviceRooted()) {
            root.setText("Root: Rooted");
        }

        TextView sdkV = fragmentView.findViewById(R.id.sdk);
        sdkV.setText("SDK Version: " + deviceClass.getSdkVersion());

        TextView codeName = fragmentView.findViewById(R.id.codeName);
        codeName.setText("Android " + deviceClass.getOSVersion());

        TextView secPatch = fragmentView.findViewById(R.id.security);
        secPatch.setText(deviceClass.getSecurityPatchLevel());

        TextView buildNo = fragmentView.findViewById(R.id.buildNo);
        buildNo.setText(deviceClass.getBuildHost());

        TextView basband = fragmentView.findViewById(R.id.baseband);
        basband.setText(deviceClass.getRadioVer());

        TextView javaVm = fragmentView.findViewById(R.id.javaVm);
        javaVm.setText(deviceClass.systemProperty());

        TextView kernel = fragmentView.findViewById(R.id.kernel);
        kernel.setText(deviceClass.readKernelVersion());

        TextView openGl = fragmentView.findViewById(R.id.opengl);
        openGl.setText(deviceClass.openGlVersion(context) + "");

        TextView rootAccess = fragmentView.findViewById(R.id.rootAccess);
        if (deviceClass.isDeviceRooted()) {
            rootAccess.setText("Rooted");
        } else rootAccess.setText("Not Rooted");

        TextView seLinux = fragmentView.findViewById(R.id.seLinux);
        seLinux.setText(deviceClass.isSeLinuxEnforcing());

        TextView gService = fragmentView.findViewById(R.id.gplay);
        gService.setText(deviceClass.getPlayVersion(context));


        TextView uptime = fragmentView.findViewById(R.id.sysUptime);

        long millis = SystemClock.uptimeMillis();
        uptime.setText(deviceClass.formatTime(millis));

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                long millis = SystemClock.uptimeMillis();
                uptime.setText(deviceClass.formatTime(millis));
                handler.postDelayed(this, 1000);
            }
        };

        TextView treble = fragmentView.findViewById(R.id.treble);
        treble.setText(deviceClass.getTreble(context));
        return fragmentView;
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 1000);
    }
}
