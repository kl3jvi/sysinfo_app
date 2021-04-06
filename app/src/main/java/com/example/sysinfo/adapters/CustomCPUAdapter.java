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

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
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
        String maxFreq = getItem(position).getMaxFreq();
        int percentage = getItem(position).getVector();

        CPUDetails cpuDetails = new CPUDetails(percentage,cpuNumber,frequency,maxFreq);
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        if(convertView == null){
            convertView = inflater.inflate(res,parent,false);
        }

        TextView cpuTitle =  convertView.findViewById(R.id.coreNo);
        TextView freqTxt = convertView.findViewById(R.id.freq_cpu);
        TextView maxFreqTxt = convertView.findViewById(R.id.maxFreq);
        IconRoundCornerProgressBar progressBar = convertView.findViewById(R.id.cpuProgress);
        progressBar.enableAnimation();
        progressBar.setMax(100);
        progressBar.setAnimationSpeedScale(2);
        progressBar.setProgress(percentage);
        cpuTitle.setText(cpuNumber);
        freqTxt.setText(frequency);
        maxFreqTxt.setText(maxFreq);
        return convertView;

    }


}
