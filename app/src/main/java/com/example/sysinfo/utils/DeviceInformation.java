package com.example.sysinfo.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;

import com.an.deviceinfo.device.DeviceInfo;
import com.an.deviceinfo.device.model.Memory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class DeviceInformation {
    private Context context;
    private Memory memory;
    private Activity activity;

    public DeviceInformation(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
        memory = new Memory(context);
    }


    /**
     *  Returns the size of internal storage in bytes;
     * @return {long}
     */
    public long totalExternalMemory(){
        return bytesToMB(memory.getTotalExternalMemorySize());
    }


    /**
     *  Returns the size of internal storage in bytes;
     * @return {long}
     */
    public long availableExternalMemory(){
        return bytesToMB(memory.getAvailableExternalMemorySize());
    }


    /**
     * Converts bytes to mega-bytes
     * @return {long}
     **/
    public long bytesToMB(long B) {
        long  MB = 1024L * 1024L;
        return B / MB;
    }

    public long getTotalRam(){
        return bytesToMB(memory.getTotalRAM());
    }

    public long getAvailableRam(){
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;

        return availableMegs;
    }

    /**
     * Calculate the percentage to put in progressbar
     * @return {Integer}
     */

    public int calculatePercentage(int toCalculate,int maximum){
        return (100 * toCalculate) / maximum;
    }


    public int getNumOfCores() {
            int i = Objects.requireNonNull(new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File params) {
                    return Pattern.matches("cpu[0-9]", params.getName());
                }
            })).length;
            return i;
    }


    public int getFrequencyOfCore(int coreNo){
        int currentFReq=0;
        try {
            double currentFreq;
            RandomAccessFile readerCurFreq;
            readerCurFreq = new RandomAccessFile("/sys/devices/system/cpu/cpu"+coreNo+"/cpufreq/scaling_cur_freq", "r");
            String curfreg = readerCurFreq.readLine();
            currentFreq =  Double.parseDouble(curfreg) / 1000;
            readerCurFreq.close();
            currentFReq  = (int) currentFreq;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentFReq;
    }

    public int getMaxCpuFrequency(){

        int currentFReq=0;
        try {
            double currentFreq;
            RandomAccessFile readerCurFreq;
            readerCurFreq = new RandomAccessFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq", "r");
            String curfreg = readerCurFreq.readLine();
            currentFreq =  Double.parseDouble(curfreg) / 1000;
            readerCurFreq.close();
            currentFReq  = (int) currentFreq;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentFReq;
    }
/**
    Returns total number of available sensors in app;
 */
    public int getNumberOfSensors(){
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensor = sensorManager.getSensorList(Sensor.TYPE_ALL);

        return sensor.size();
    }

/**
 * Returns the total number of apps in your mobile phone;
 */
    public int getNumberOfApps(){
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities( mainIntent, 0);
        return  pkgAppsList.size();
    }
}