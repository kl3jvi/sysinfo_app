package com.example.sysinfo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.sysinfo.R;
import com.example.sysinfo.utils.DeviceInformation;

public class DeviceFragment extends Fragment {

    private View fragmentView;
    private Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.device_fragment, container, false);

        if(context == null){
            context = requireContext();
        }
        DeviceInformation deviceClass = new DeviceInformation(context);

        TextView device = fragmentView.findViewById(R.id.device);
        device.setText(deviceClass.getDevice());

        TextView board = fragmentView.findViewById(R.id.board);
        board.setText(deviceClass.getBoard());

        TextView deviceName = fragmentView.findViewById(R.id.deviceName);
        deviceName.setText(deviceClass.getDeviceName());

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

        TextView device_id = fragmentView.findViewById(R.id.deviceId);
        device_id.setText(deviceClass.getAndroidId());

        TextView language = fragmentView.findViewById(R.id.language);
        language.setText(deviceClass.getLanguage());

        TextView deviceType = fragmentView.findViewById(R.id.deviceType);
        deviceType.setText(deviceClass.getPhoneType());

        TextView networkType = fragmentView.findViewById(R.id.networkType);
        networkType.setText(deviceClass.getNetworkType());

        TextView operator1 = (TextView) fragmentView.findViewById(R.id.operator1);
        operator1.setText(deviceClass.getOperator());

        TextView usbHost = fragmentView.findViewById(R.id.usbHost);
        if (deviceClass.checkInfo(requireContext())) {
            usbHost.setText("Supported");
        } else {
            usbHost.setText("Not Supported");
        }


        System.out.println(deviceClass.getDeviceName());

        return fragmentView;
    }
}
