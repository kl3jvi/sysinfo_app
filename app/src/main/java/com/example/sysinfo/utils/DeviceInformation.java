package com.example.sysinfo.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.an.deviceinfo.device.DeviceInfo;
import com.an.deviceinfo.device.model.Memory;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class DeviceInformation extends DeviceInfo {
    private Context context;
    private Memory memory;
    private Activity activity;

    public DeviceInformation(Context context) {
        super(context);
    }

    public DeviceInformation(Context context, Activity activity) {
        super(context);
        this.context = context;
        this.activity = activity;
        memory = new Memory(context);
    }

    /**
     * @see FeatureInfo#getGlEsVersion()
     */
    private static int getMajorVersion(int glEsVersion) {
        return ((glEsVersion & 0xffff0000) >> 16);
    }

    /**
     * Get Values from android properties
     *
     * @param key
     * @return
     */
    public static String getSystemProperty(String key) {
        String value = null;

        try {
            value = (String) Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class).invoke(null, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    /**
     * Returns the size of internal storage in bytes;
     *
     * @return {long}
     */
    public long totalExternalMemory() {
        return bytesToMB(memory.getTotalExternalMemorySize());
    }

    /**
     * Returns the size of internal storage in bytes;
     *
     * @return {long}
     */
    public long availableExternalMemory() {
        return bytesToMB(memory.getAvailableExternalMemorySize());
    }

    /**
     * Converts bytes to mega-bytes
     *
     * @return {long}
     **/
    public long bytesToMB(long B) {
        long MB = 1024L * 1024L;
        return B / MB;
    }

    public long getTotalRam() {
        return bytesToMB(memory.getTotalRAM());
    }

    public long getAvailableRam() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;

        return availableMegs;
    }

    /**
     * Calculate the percentage to put in progressbar
     *
     * @return {Integer}
     */

    public int calculatePercentage(int toCalculate, int maximum) {
        if (maximum != 0) {
            return (100 * toCalculate) / maximum;
        } else return 30;
    }

    /**
     * returns the number of cpu cores
     *
     * @return integer;
     */
    public int getNumOfCores() {
        int i = Objects.requireNonNull(new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
            public boolean accept(File params) {
                return Pattern.matches("cpu[0-9]", params.getName());
            }
        })).length;
        return i;
    }

    /**
     * Returns frequency of custom number of cores;
     *
     * @param coreNo
     * @return
     */
    public int getFrequencyOfCore(int coreNo) {
        int currentFReq = 0;
        try {
            double currentFreq;
            RandomAccessFile readerCurFreq;
            readerCurFreq = new RandomAccessFile("/sys/devices/system/cpu/cpu" + coreNo + "/cpufreq/scaling_cur_freq", "r");
            String curfreg = readerCurFreq.readLine();
            currentFreq = Double.parseDouble(curfreg) / 1000;
            readerCurFreq.close();
            currentFReq = (int) currentFreq;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentFReq;
    }

    /**
     * Maximum frequency of the cpu (used to calculate the percentage for frequency bars);
     *
     * @return
     */
    public int getMaxCpuFrequency(int core) {
        int currentFReq = 0;
        try {
            double currentFreq;
            RandomAccessFile readerCurFreq;
            readerCurFreq = new RandomAccessFile("/sys/devices/system/cpu/cpu" + core + "/cpufreq/cpuinfo_max_freq", "r");
            String curfreg = readerCurFreq.readLine();
            currentFreq = Double.parseDouble(curfreg) / 1000;
            readerCurFreq.close();
            currentFReq = (int) currentFreq;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return currentFReq;
    }

    /**
     * Returns total number of available sensors in app;
     */
    public int getNumberOfSensors() {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensor = sensorManager.getSensorList(Sensor.TYPE_ALL);
        return sensor.size();
    }

    /**
     * Returns the total number of apps in your mobile phone;
     */
    public int getNumberOfApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities(mainIntent, 0);
        return pkgAppsList.size();
    }

    /**
     * Returns security patch level of the device;
     *
     * @return
     */
    public String getSecurityPatchLevel() {
        String str = "";
        try {
            Process process = new ProcessBuilder()
                    .command("/system/bin/getprop")
                    .redirectErrorStream(true)
                    .start();

            InputStream is = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = br.readLine()) != null) {
                str += line + "\n";
                if (str.contains("security_patch")) {
                    String[] splitted = line.split(":");
                    if (splitted.length == 2) {
                        return splitted[1];
                    }
                    break;
                }
            }

            br.close();
            process.destroy();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * Checks if the device supports USB Host;
     *
     * @param context
     * @return
     */
    public boolean checkInfo(Context context) {
        UsbManager mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> devlist = mUsbManager.getDeviceList();
        return !devlist.isEmpty();
    }

    /**
     * Returns if the device is using dalvik or any other jvm and its version;
     *
     * @return
     */
    public String systemProperty() {
        String vm = System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version");
        return vm;
    }

    /**
     * Returns the kernel version of the device;
     *
     * @return
     */
    public String readKernelVersion() {
        String ver = System.getProperty("os.version");
        return ver;
    }

    /**
     * Gets the openGl version
     *
     * @param context
     * @return
     */
    public String openGlVersion(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return configurationInfo.getGlEsVersion();
    }

    /**
     * Check for SELinux;
     *
     * @return
     */
    public String isSeLinuxEnforcing() {
        StringBuffer output = new StringBuffer();
        Process p;
        String TAG = "SELINUX";
        try {
            p = Runtime.getRuntime().exec("getenforce");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
        } catch (Exception e) {
            Log.e(TAG, "OS does not support getenforce");
            // If getenforce is not available to the device, assume the device is not enforcing
            e.printStackTrace();
            return "Isn't Available";
        }
        String response = output.toString();
        if ("Enforcing".equals(response)) {
            return "Available";
        } else if ("Permissive".equals(response)) {
            return "Unavailable";
        } else {
            Log.e(TAG, "getenforce returned unexpected value, unable to determine selinux!");
            // If getenforce is modified on this device, assume the device is not enforcing
            return "Unable to determine";
        }
    }

    public String getPlayVersion(Context context) {
        int apkVersion = GoogleApiAvailability.getInstance().getApkVersion(context);
        return String.valueOf(apkVersion);
    }

    public String formatTime(long millis) {
        long seconds = Math.round((double) millis / 1000);
        long hours = TimeUnit.SECONDS.toHours(seconds);
        if (hours > 0)
            seconds -= TimeUnit.HOURS.toSeconds(hours);
        long minutes = seconds > 0 ? TimeUnit.SECONDS.toMinutes(seconds) : 0;
        if (minutes > 0)
            seconds -= TimeUnit.MINUTES.toSeconds(minutes);
        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Checks if treble is supported;
     *
     * @param context
     * @return
     */
    public String getTreble(Context context) {
        String output = getSystemProperty("ro.treble.enabled");
        if (output.equals("true")) {
            return "Supported";
        } else {
            return "Not Supported";
        }
    }


    public long getTotalStorageInfo(String path) {
        StatFs statFs = new StatFs(path);
        long t;
        t = statFs.getTotalBytes();
        return t;    // remember to convert in GB,MB or KB.
    }

    public long getUsedStorageInfo(String path) {
        StatFs statFs = new StatFs(path);
        long u;
        u = statFs.getTotalBytes() - statFs.getAvailableBytes();
        return u;  // remember to convert in GB,MB or KB.
    }


    public long getTotalOsStorage(){
        return getTotalStorageInfo(Environment.getRootDirectory().getPath());
    }

    public long getUsedOsStorage(){
        return getUsedStorageInfo(Environment.getRootDirectory().getPath());
    }


    public String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

}