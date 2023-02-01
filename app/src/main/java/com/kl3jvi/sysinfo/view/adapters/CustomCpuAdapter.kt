package com.kl3jvi.sysinfo.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sysinfo.databinding.CpuListBinding

class CustomCpuAdapter :
    ListAdapter<Long, CustomCpuAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Long>() {
        override fun areItemsTheSame(oldItem: Long, newItem: Long): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Long, newItem: Long): Boolean {
            return oldItem == newItem
        }
    }) {

    class ViewHolder(private val view: CpuListBinding) : RecyclerView.ViewHolder(view.root) {
        init {
            view.cpuProgress.enableAnimation()
            view.cpuProgress.animationSpeedScale = 1f
        }

        fun bind(item: Long) {
            view.cpuProgress.progress = item.toFloat()
            view.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: CpuListBinding =
            CpuListBinding.inflate(LayoutInflater.from((parent.context)), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount() = currentList.size

    fun passFrequencies(list: List<Long>) {
        submitList(list)
    }
}
