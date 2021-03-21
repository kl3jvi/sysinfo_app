package com.example.sysinfo.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.example.sysinfo.R;

import java.util.ArrayList;

public class CustomCPUAdapter extends ArrayAdapter<CPUDetails> {

    private final Context mcontext;
    private int res;

    public CustomCPUAdapter(Context context, int cpu_list, ArrayList<CPUDetails> details) {
        super(context,cpu_list,details);
        mcontext=context;
        res = cpu_list;
    }



    public View getView(int position, View convertView, ViewGroup parent) {
        String cpuNumber = getItem(position).getCpuNumber();
        String frequency = getItem(position).getFrequency();
        int percentage = getItem(position).getVector();


        CPUDetails cpuDetails = new CPUDetails(percentage,cpuNumber,frequency);

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(res,parent,false);


        TextView cpuTitle =  convertView.findViewById(R.id.coreNo);
        TextView freqTxt = convertView.findViewById(R.id.freq_cpu);
        NumberProgressBar progressBar = convertView.findViewById(R.id.cpu_progress);

        cpuTitle.setText(cpuNumber);
        freqTxt.setText(frequency);

        ObjectAnimator smoothAnimation = ObjectAnimator.ofInt(progressBar, "progress", percentage);
        smoothAnimation.setDuration(600);
        smoothAnimation.setInterpolator(new AnticipateOvershootInterpolator());

        smoothAnimation.start();
        return convertView;

    }


}
