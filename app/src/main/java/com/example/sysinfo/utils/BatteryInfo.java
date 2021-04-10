package com.example.sysinfo.utils;

import android.content.Context;

import com.an.deviceinfo.device.model.Battery;

public class BatteryInfo {
    private Context context;
    private Battery battery;

    public BatteryInfo(Context context) {
        this.context = context;
        battery = new Battery(context);
    }

    /**
     * Returns the battery percentage;
     */

    public int batteryPercentage(){
        return battery.getBatteryPercent();
    }

    /**
     * Returns state of the battery;
     */
    public boolean isCharging(){
        return battery.isPhoneCharging();
    }

}
