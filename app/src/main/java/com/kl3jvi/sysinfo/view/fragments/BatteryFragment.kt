package com.kl3jvi.sysinfo.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sysinfo.R
import com.example.sysinfo.databinding.BatteryFragmentBinding
import com.kl3jvi.sysinfo.viewmodel.DataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

class BatteryFragment : Fragment(R.layout.battery_fragment), KoinComponent {

    private val viewModel: DataViewModel by viewModel()
    private var _binding: BatteryFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = BatteryFragmentBinding.bind(view)
        initViews()
    }

    private fun initViews() {
        binding.apply bind@{
            viewModel.batteryInfo.apply {
                this@bind.health.text = health
                this@bind.batPercentage.text = level
                this@bind.powerSource.text = chargingType
                this@bind.batStatus.text = if (isCharging) "Charging" else "Full"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
