package com.example.sysinfo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sysinfo.R;
import com.example.sysinfo.utils.CpuUtils;
import com.example.sysinfo.utils.DeviceInformation;


public class CPU extends Fragment {

    private View fragmentView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.cpu_fragment, container, false);

        if (context == null) {
            context = requireContext();
        }

        //  Classes that provide functions;

        CpuUtils cpuUtils = new CpuUtils();
        DeviceInformation deviceInformation = new DeviceInformation(context);

        TextView socName = fragmentView.findViewById(R.id.socName);
        socName.setText(cpuUtils.getCpuInfoMap().get("Hardware"));

        TextView abi = fragmentView.findViewById(R.id.abi);
        abi.setText(cpuUtils.getCpuAbi());

        TextView processor = fragmentView.findViewById(R.id.processor);
        processor.setText(deviceInformation.getDevice());

        TextView cpuType = fragmentView.findViewById(R.id.cpuType);
        cpuType.setText(cpuUtils.getCpuArchitecture());

        TextView cores = fragmentView.findViewById(R.id.cores);
        cores.setText(deviceInformation.getNumOfCores() + "");

        
        return fragmentView;
    }
}
