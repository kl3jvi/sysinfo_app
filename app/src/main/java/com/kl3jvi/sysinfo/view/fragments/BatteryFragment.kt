package com.kl3jvi.sysinfo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sysinfo.R
import com.example.sysinfo.databinding.BatteryFragmentBinding

class BatteryFragment : Fragment(R.layout.battery_fragment) {

    private var _binding: BatteryFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BatteryFragmentBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
