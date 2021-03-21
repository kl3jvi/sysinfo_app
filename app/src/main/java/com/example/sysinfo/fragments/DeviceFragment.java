package com.example.sysinfo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.sysinfo.R;
import com.example.sysinfo.utils.DeviceClass;

public class DeviceFragment extends Fragment {

    private View fragmentView;



    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.device_fragment, container, false);
        DeviceClass deviceClass = new DeviceClass(getContext());

        TextView device = fragmentView.findViewById(R.id.device);
        device.setText(deviceClass.getDevice());

        TextView board = fragmentView.findViewById(R.id.board);
        board.setText(deviceClass.getBoard());

        TextView deviceName = fragmentView.findViewById(R.id.deviceName);
        deviceName.setText(deviceClass.getModel());

        TextView model = fragmentView.findViewById(R.id.model);
        model.setText(deviceClass.getModel());

        TextView manufacturer = fragmentView.findViewById(R.id.manufacturer);
        manufacturer.setText(deviceClass.getManufacturer());

        TextView hardware = fragmentView.findViewById(R.id.hardware);
        hardware.setText(deviceClass.getHardware());

        TextView brand = fragmentView.findViewById(R.id.brand);
        brand.setText(deviceClass.getBuildBrand());

        TextView buildFingerPrint = fragmentView.findViewById(R.id.fingerprint);
        buildFingerPrint.setText(deviceClass.getFingerprint());


        System.out.println();

        return fragmentView;
    }
}
