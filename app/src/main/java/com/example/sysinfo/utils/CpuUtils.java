package com.example.sysinfo.utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CpuUtils {
    /**
     * Parses the output of the /proc/cpuinfo file and maps it. Search for specifics
     * i.e getCpuinfoMap().get('hardware')
     *
     * @return Map
     */
    public Map<String, String> getCpuInfoMap() {
        Map<String, String> map = new HashMap<>();
        try {
            Scanner s = new Scanner(new File("/proc/cpuinfo"));
            while (s.hasNextLine()) {
                String[] vals = s.nextLine().split(": ");
                Log.e("cpu-", Arrays.toString(vals));
                if (vals.length > 1) map.put(vals[0].trim(), vals[1].trim());
            }
        } catch (Exception e) {
            Log.e("getCpuInfoMap", Log.getStackTraceString(e));
        }
        return map;
    }

    /**
     * Method that returns the abi supported on runtime cpu, approach adapted from termux api;
     *
     * Note that we cannot use System.getProperty("os.arch") since that may give e.g. "aarch64"
     * while a 64-bit runtime may not be installed (like on the Samsung Galaxy S5 Neo).
     * Instead we search through the supported abi:s on the device, see:
     * http://developer.android.com/ndk/guides/abis.html
     * Note that we search for abi:s in preferred order (the ordering of the
     * Build.SUPPORTED_ABIS list) to avoid e.g. installing arm on an x86 system where arm
     * emulation is available.
     *
     * @return cpuAbi;
     */
    public String getCpuAbi() {

        for (String androidArch : Build.SUPPORTED_ABIS) {
            switch (androidArch) {
                case "arm64-v8a":
                    return "aarch64";
                case "armeabi-v7a":
                    return "arm";
                case "x86_64":
                    return "x86_64";
                case "x86":
                    return "i686";
            }
        }
        throw new RuntimeException("Unable to determine arch from Build.SUPPORTED_ABIS =  " +
                Arrays.toString(Build.SUPPORTED_ABIS));
    }



    public String getCpuArchitecture(){
        String bits=" ";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bits = TextUtils.join(", ", Build.SUPPORTED_ABIS).contains("64") ? "64-Bit" : "32-Bit";
        } else {
            bits = "32-Bit";
        }
        return bits;
    }

    /**
     * Get the minimum frequency of the cpu on device;
     * @param core
     * @return
     */
    public int getMinCPUFreq(int core) {
        int minFreq = -1;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("/sys/devices/system/cpu/cpu" + core + "/cpufreq/cpuinfo_min_freq", "r");
            boolean done = false;
            while (!done) {
                String line = randomAccessFile.readLine();
                if (null == line) {
                    done = true;
                    break;
                }
                int timeInState = Integer.parseInt(line);
                if (timeInState > 0) {
                    int freq = timeInState / 1000;
                    if (freq > minFreq) {
                        minFreq = freq;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return minFreq;
    }

    /**
     * Gets the cpu maximum frequency
     * @param core
     * @return
     */
    public int getMaxCPUFreq(int core) {
        int maxFreq = -1;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("/sys/devices/system/cpu/cpu" + core + "/cpufreq/cpuinfo_max_freq", "r");
            boolean done = false;
            while (!done) {
                String line = randomAccessFile.readLine();
                if (null == line) {
                    done = true;
                    break;
                }
                int timeInState = Integer.parseInt(line);
                if (timeInState > 0) {
                    int freq = timeInState / 1000;
                    if (freq > maxFreq) {
                        maxFreq = freq;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxFreq;
    }


    public String getCPUFreq() {
        return getMinCPUFreq(0) + " - " + getMaxCPUFreq(0) + " MHz";
    }

    public String getBogoMIPS() {
        String bogomips = String.valueOf(getCpuInfoMap().get("bogomips"));
        if (bogomips.equals("null")) {
            bogomips = String.valueOf(getCpuInfoMap().get("BogoMIPS"));
        }
        return bogomips;
    }

    public String getCPUGovernor(int core) {
        String governor = "";
        String file = "/sys/devices/system/cpu/cpu" + core + "/cpufreq/scaling_governor";

        if (new File(file).exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(file)));
                governor = bufferedReader.readLine();

                if (bufferedReader != null)
                    bufferedReader.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return governor;
    }

}

