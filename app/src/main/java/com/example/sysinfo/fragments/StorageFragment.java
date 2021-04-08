package com.example.sysinfo.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.example.sysinfo.R;
import com.example.sysinfo.utils.DeviceInformation;

public class StorageFragment extends Fragment {
    private Context context;
    private Activity activity;
    private TextView internalTxt, externalTxt;
    private IconRoundCornerProgressBar internalProgressBar, externalProgressBar;
    private DeviceInformation dInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.storage_fragment, container, false);

        if (context == null && activity == null) {
            context = requireContext();
            activity = requireActivity();
        }
        dInfo = new DeviceInformation(context, activity);

        internalTxt = fragmentView.findViewById(R.id.internal);
        externalTxt = fragmentView.findViewById(R.id.external);

        internalProgressBar = fragmentView.findViewById(R.id.progress_bar_internal);

        externalProgressBar = fragmentView.findViewById(R.id.externalSize);

        long totalInternalMemorySize = dInfo.getTotalInternalMemorySize();
        long availableInternalMemorySize = dInfo.getAvailableInternalMemorySize();
        long internalPercentage = dInfo.calculateLongPercentage(totalInternalMemorySize-availableInternalMemorySize, totalInternalMemorySize);

        String avail = dInfo.humanReadableByteCountBin(totalInternalMemorySize - availableInternalMemorySize);
        String total = dInfo.humanReadableByteCountBin(totalInternalMemorySize);

        internalTxt.setText("Internal: " + avail + " / " + total + " ("+internalPercentage+"%)");
        internalProgressBar.setProgress(internalPercentage);


        long totalExternalMemorySize = dInfo.getTotalExternalMemorySize();
        long availableExternalMemorySize = dInfo.getAvailableExternalMemorySize();
        long externalPercentage = dInfo.calculateLongPercentage(totalExternalMemorySize-availableExternalMemorySize,totalExternalMemorySize);

        String exAvail = dInfo.humanReadableByteCountBin(totalExternalMemorySize-availableExternalMemorySize);
        String exTotal = dInfo.humanReadableByteCountBin(totalExternalMemorySize);
        externalTxt.setText("External: " + exAvail + " / " + exTotal + " ("+externalPercentage+"%)");
        externalProgressBar.setProgress(externalPercentage);


        return fragmentView;
    }

}
