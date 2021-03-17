package com.example.sysinfo.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ChargingReciever extends BroadcastReceiver {
    private final ProgressBar progressBar;
    private final BatteryInfo batteryInfo;
    private final TextView batteryPercentage;

    public ChargingReciever(ProgressBar progressBar,TextView batteryPercentage,Context context) {
        this.progressBar = progressBar;
        this.batteryPercentage = batteryPercentage;
        batteryInfo = new BatteryInfo(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            progressBar.setIndeterminate(true);
            batteryPercentage.setText("(Charging) "+batteryInfo.batteryPercentage()+" %");
        } else {
            intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED);
            progressBar.setIndeterminate(false);
            progressBar.setProgress(batteryInfo.batteryPercentage());
            batteryPercentage.setText(batteryInfo.batteryPercentage()+" %");

        }
    }


}