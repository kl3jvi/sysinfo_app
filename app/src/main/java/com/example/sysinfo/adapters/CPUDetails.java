package com.example.sysinfo.adapters;

public class CPUDetails {
    int vector;
    String cpuNumber;
    String frequency;
    String maxFreq;


    public CPUDetails(int vector, String cpuNumber, String frequency, String maxFreq) {
        this.vector = vector;
        this.cpuNumber = cpuNumber;
        this.frequency = frequency;
        this.maxFreq = maxFreq;
    }

    public String getMaxFreq() {
        return maxFreq;
    }

    public void setMaxFreq(String maxFreq) {
        this.maxFreq = maxFreq;
    }

    public int getVector() {
        return vector;
    }

    public void setVector(int vector) {
        this.vector = vector;
    }

    public String getCpuNumber() {
        return cpuNumber;
    }

    public void setCpuNumber(String cpuNumber) {
        this.cpuNumber = cpuNumber;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

}
