package com.kl3jvi.sysinfo.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sysinfo.R
import com.example.sysinfo.databinding.SystemFragmentBinding
import com.example.sysinfo.information
import com.kl3jvi.sysinfo.viewmodel.DataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SystemFrag : Fragment(R.layout.system_fragment) {

    private var _binding: SystemFragmentBinding? = null
    private val binding get() = _binding!!
    private val dataViewModel: DataViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SystemFragmentBinding.bind(view)
        setupUIElements()
    }

    private fun setupUIElements() {
        binding.listWithItems.layoutManager = LinearLayoutManager(requireContext())
        binding.listWithItems.withModels {
            dataViewModel.systemInfo.forEach {
                information {
                    id(it.title)
                    data(it)
                }
            }
        }

        binding.androidNo.text = getString(
            R.string.android_version,
            dataViewModel.systemInfo.find { it.title == "Android Version" }?.details
        )

        val isRoot =
            dataViewModel.systemInfo.firstOrNull { it.title == "Root Access" }?.details
        binding.isRoot.text = getString(R.string.is_root, isRoot)
        binding.sdk.text = getString(R.string.sdk_version,dataViewModel.systemInfo.find { it.title == "Android Version" }?.details)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
