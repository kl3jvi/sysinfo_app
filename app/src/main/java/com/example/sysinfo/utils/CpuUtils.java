package com.example.sysinfo.utils;

import android.util.Log;

import java.io.File;
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

    public String getCpuAbi() {
        String ABI = "";
        try {
            ABI = System.getProperty("os.arch");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ABI;
    }
}
