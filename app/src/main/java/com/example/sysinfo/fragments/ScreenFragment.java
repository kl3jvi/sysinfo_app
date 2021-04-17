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
import com.example.sysinfo.utils.DeviceInformation;

public class ScreenFragment extends Fragment {
    private Context context;
    private Activity activity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.screen_fragment, container, false);


        if (context == null && activity == null) {
            context = requireContext();
            activity = requireActivity();
        }
        DeviceInformation dInfo = new DeviceInformation(context, activity);

        String width = String.valueOf(dInfo.getScreenWidth());
        String height = String.valueOf(dInfo.getScreenHeight());
        TextView resolution = fragmentView.findViewById(R.id.resolution);
        resolution.setText(width + "x" + height);

        TextView screenDensity = fragmentView.findViewById(R.id.dpi);
        screenDensity.setText(dInfo.getDPI(activity));

        TextView screenSize = fragmentView.findViewById(R.id.screenSize);
        screenSize.setText(DeviceInformation.getScreenSize(activity));

        TextView refreshRate = fragmentView.findViewById(R.id.refreshValue);
        refreshRate.setText(DeviceInformation.getRefreshValue(activity));


//        TextView
        return fragmentView;
    }


}
