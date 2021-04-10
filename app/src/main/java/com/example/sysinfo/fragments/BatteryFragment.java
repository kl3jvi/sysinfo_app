package com.example.sysinfo.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sysinfo.R;
import com.example.sysinfo.utils.BatteryInfo;
import com.example.sysinfo.utils.DeviceInformation;

public class BatteryFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.battery_fragment, container, false);

        if (context == null && activity == null) {
            context = requireContext();
            activity = requireActivity();
        }
        DeviceInformation deviceInformation = new DeviceInformation(context, activity);
        BatteryInfo batteryInfo = new BatteryInfo(context);

        TextView health = fragmentView.findViewById(R.id.health);
        health.setText(deviceInformation.getBatteryHealth().toUpperCase());

        TextView percentage = fragmentView.findViewById(R.id.batPercentage);
        percentage.setText(deviceInformation.getBatteryPercent()+"%");

        TextView powerSource = fragmentView.findViewById(R.id.power_source);
        if (batteryInfo.isCharging()) {
            powerSource.setText("Connected");
        } else {
            powerSource.setText("Disconnected");
        }

        TextView status = fragmentView.findViewById(R.id.bat_status);
        if (batteryInfo.isCharging()) {
            status.setText("Charging");
        } else {
            status.setText("Discharging");
        }

        TextView temperature = fragmentView.findViewById(R.id.bat_temperature);
        temperature.setText(deviceInformation.getBatteryTemperature()+" °C");

        TextView technology = fragmentView.findViewById(R.id.technology);
        technology.setText(deviceInformation.getBatteryTechnology());

        TextView voltage = fragmentView.findViewById(R.id.voltage);
        voltage.setText(deviceInformation.getBatteryVoltage()+" mV");

        TextView capacity = fragmentView.findViewById(R.id.capacity);
        capacity.setText(deviceInformation.getBatteryCapacity(context)+" mAh");

        return fragmentView;
    }
}
