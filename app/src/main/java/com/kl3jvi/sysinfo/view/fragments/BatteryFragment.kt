package com.kl3jvi.sysinfo.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sysinfo.R
import com.example.sysinfo.databinding.BatteryFragmentBinding
import com.example.sysinfo.information
import com.kl3jvi.sysinfo.viewmodel.DataViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        binding.listWithItems.layoutManager = LinearLayoutManager(requireContext())

        viewModel.batteryInfo.onEach { batteryData ->
            binding.listWithItems.withModels {
                batteryData.data.forEach { info ->
                    information {
                        id(info.details)
                        data(info)
                    }
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
