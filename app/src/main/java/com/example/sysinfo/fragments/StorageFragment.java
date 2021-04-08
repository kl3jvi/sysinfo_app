package com.example.sysinfo.fragments;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
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
    private TextView internalTxt,externalTxt;
    private IconRoundCornerProgressBar internalProgressBar,externalProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.storage_fragment, container, false);

        if (context == null && activity == null) {
            context = requireContext();
            activity = requireActivity();
        }
        DeviceInformation dInfo = new DeviceInformation(context,activity);

        internalTxt = fragmentView.findViewById(R.id.internal);
        externalTxt = fragmentView.findViewById(R.id.external);

        internalProgressBar = fragmentView.findViewById(R.id.internalSize);
        externalProgressBar = fragmentView.findViewById(R.id.externalSize);

        long totalInternalMemorySize = dInfo.getTotalInternalMemorySize();
        long availableInternalMemorySize = dInfo.getAvailableInternalMemorySize();
        int internalPercentage = dInfo.calculatePercentage((int)availableInternalMemorySize,(int)totalInternalMemorySize);
        System.out.println(internalPercentage+"-----------------------------------------------");
        return fragmentView;
    }

}
