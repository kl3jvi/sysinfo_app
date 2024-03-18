package com.kl3jvi.sysinfo.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sysinfo.R
import com.example.sysinfo.databinding.CpuFragmentBinding
import com.example.sysinfo.information
import com.kl3jvi.sysinfo.utils.Do
import com.kl3jvi.sysinfo.utils.UiResult
import com.kl3jvi.sysinfo.utils.showToast
import com.kl3jvi.sysinfo.viewmodel.DataViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class CPUFragment : Fragment(R.layout.cpu_fragment) {

    private var _binding: CpuFragmentBinding? = null
    private val binding get() = _binding!!
    private val dataViewModel: DataViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = CpuFragmentBinding.bind(view)
        setupUIElements()
    }

    private fun setupUIElements() {
        binding.listWithItems.layoutManager = LinearLayoutManager(requireContext())
        dataViewModel.cpuInfo.onEach { result ->
            Do exhaustive when (result) {
                is UiResult.Error -> showToast(result.throwable.message.orEmpty())
                is UiResult.Success -> binding.listWithItems.withModels {
                    result.data.layoutInfo.forEach { information ->
                        information {
                            id(information.title)
                            data(information)
                        }
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
