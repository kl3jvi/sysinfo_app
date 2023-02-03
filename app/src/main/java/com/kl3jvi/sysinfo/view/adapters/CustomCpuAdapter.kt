package com.kl3jvi.sysinfo.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sysinfo.databinding.CpuListBinding
import com.kl3jvi.sysinfo.data.model.CpuInfo

class CustomCpuAdapter : ListAdapter<CpuInfo.Frequency, CustomCpuAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<CpuInfo.Frequency>() {
        override fun areItemsTheSame(
            oldItem: CpuInfo.Frequency,
            newItem: CpuInfo.Frequency
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CpuInfo.Frequency,
            newItem: CpuInfo.Frequency
        ): Boolean {
            return oldItem.current == newItem.current &&
                    oldItem.max == newItem.max &&
                    oldItem.min == newItem.min
        }
    }) {

    class ViewHolder(private val view: CpuListBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.cpuProgress.enableAnimation()
            view.cpuProgress.animationSpeedScale = 1f
        }

        fun bind(item: CpuInfo.Frequency, position: Int) {
            view.cpuInfo = item
            view.position = position.plus(1)
            view.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CpuListBinding =
            CpuListBinding.inflate(LayoutInflater.from((parent.context)), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun getItemCount() = currentList.size

    fun passFrequencies(list: List<CpuInfo.Frequency>) {
        submitList(list)
    }
}
