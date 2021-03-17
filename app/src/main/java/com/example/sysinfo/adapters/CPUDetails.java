package com.example.sysinfo.adapters;

public class CPUDetails {
    int vector;
    String cpuNumber;
    String frequency;

    public CPUDetails(int vector, String cpuNumber, String frequency) {
        this.vector = vector;
        this.cpuNumber = cpuNumber;
        this.frequency = frequency;
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
