package com.kl3jvi.sysinfo.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sysinfo.databinding.CpuListBinding

class CustomCpuAdapter(private val fragment: Fragment) :
    RecyclerView.Adapter<CustomCpuAdapter.ViewHolder>() {

    private var listOfFrequencies: List<Long> = listOf()

    class ViewHolder(view: CpuListBinding) : RecyclerView.ViewHolder(view.root) {
        val tvMinFreq = view.minFreqOfCpu
        val tvMaxFreq = view.maxFreqOfCpu
        val coreNumber = view.coreNo
        var coreProgress = view.cpuProgress
        val actualFreq = view.freqCpu

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CpuListBinding =
            CpuListBinding.inflate(LayoutInflater.from((fragment.context)), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val freq = listOfFrequencies[position]

        holder.coreProgress.progress = freq.toFloat()
    }

    override fun getItemCount() = listOfFrequencies.size

    fun passFrequencies(list: List<Long>) {
        listOfFrequencies = list
        notifyDataSetChanged()
    }
}