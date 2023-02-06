package com.kl3jvi.sysinfo.view.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sysinfo.R
import com.example.sysinfo.databinding.DashboardFragmentBinding
import com.github.lzyzsd.circleprogress.ArcProgress
import com.kl3jvi.sysinfo.data.model.CpuInfo
import com.kl3jvi.sysinfo.domain.models.RamData
import com.kl3jvi.sysinfo.utils.UiResult
import com.kl3jvi.sysinfo.utils.launchAndRepeatWithViewLifecycle
import com.kl3jvi.sysinfo.utils.showToast
import com.kl3jvi.sysinfo.view.adapters.CustomCpuAdapter
import com.kl3jvi.sysinfo.viewmodel.DashboardViewModel
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

/**
 * A placeholder fragment containing a simple view.
 */
class Dashboard : Fragment(R.layout.dashboard_fragment), KoinComponent {

    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var _binding: DashboardFragmentBinding? = null
    private val binding get() = _binding!!
    private var cpuAdapter: CustomCpuAdapter = CustomCpuAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = DashboardFragmentBinding.bind(view)
        binding.arcProgress.setRamValueAsync(dashboardViewModel.ramInfo)
        cpuAdapter.passFrequencies(dashboardViewModel.cpuInfo)

        binding.apply {
            systemStorage.progress = dashboardViewModel.systemStoragePercentage
            textView4.text = resources.getString(
                R.string.percentage_format,
                dashboardViewModel.systemStoragePercentage.toString()
            )

            internalProgress.progress = dashboardViewModel.internalStoragePercentage
            internalPercentage.text = resources.getString(
                R.string.percentage_format,
                dashboardViewModel.internalStoragePercentage.toString()
            )
        }

        binding.listView.layoutManager = LinearLayoutManager(requireContext())
        binding.listView.adapter = cpuAdapter
        binding.listView.itemAnimator = null
    }

    private fun ArcProgress.setRamValueAsync(
        flow: StateFlow<UiResult<RamData>>
    ) = apply {
        launchAndRepeatWithViewLifecycle {
            flow.collect {
                when (it) {
                    is UiResult.Error -> requireContext().showToast(it.throwable.message.orEmpty())
                    UiResult.Idle -> requireContext().showToast("Loading Data")
                    is UiResult.Success -> {
                        ObjectAnimator.ofInt(
                            this@setRamValueAsync,
                            "progress",
                            it.data.percentageAvailable
                        ).setDuration(1000).start()
                        binding.apply {
                            ramTxt.text = resources.getString(
                                R.string.ram_text,
                                it.data.available,
                                it.data.total
                            )
                        }
                    }
                }
            }
        }
    }


    private fun CustomCpuAdapter.passFrequencies(
        flow: StateFlow<UiResult<CpuInfo>>
    ) = launchAndRepeatWithViewLifecycle {
        flow.collect {
            when (it) {
                is UiResult.Error -> requireContext().showToast(it.throwable.message.orEmpty())
                UiResult.Idle -> requireContext().showToast("Loading Data")
                is UiResult.Success -> {
                    passFrequencies(it.data.frequencies)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
