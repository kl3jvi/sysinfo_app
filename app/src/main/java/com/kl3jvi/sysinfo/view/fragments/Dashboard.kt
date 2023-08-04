package com.kl3jvi.sysinfo.view.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.sysinfo.R
import com.example.sysinfo.cpuProgress
import com.example.sysinfo.databinding.DashboardFragmentBinding
import com.github.lzyzsd.circleprogress.ArcProgress
import com.kl3jvi.sysinfo.domain.models.CpuData
import com.kl3jvi.sysinfo.domain.models.RamData
import com.kl3jvi.sysinfo.utils.UiResult
import com.kl3jvi.sysinfo.utils.launchAndCollectWithViewLifecycle
import com.kl3jvi.sysinfo.utils.nav
import com.kl3jvi.sysinfo.utils.parsePercentage
import com.kl3jvi.sysinfo.utils.setupActionBar
import com.kl3jvi.sysinfo.utils.showToast
import com.kl3jvi.sysinfo.viewmodel.DataViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent

/**
 * A placeholder fragment containing a simple view.
 */
class Dashboard : Fragment(R.layout.dashboard_fragment), KoinComponent {

    private val dataViewModel: DataViewModel by viewModel()
    private var _binding: DashboardFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DashboardFragmentBinding.bind(view)

        binding.arcProgress.setRamValueAsync(dataViewModel.ramInfo)
        setupUIElements()
        binding.listView.itemAnimator = null

        launchAndCollectWithViewLifecycle(dataViewModel.cpuInfo) {
            handleCpuInfoResult(it)
        }

        setupActionBar(R.menu.settings_menu) {
            when (it.itemId) {
                R.id.settings -> handleSettings()
            }
        }
    }

    private fun setupUIElements() = binding.apply {
        systemStorage.setProgressAndText(dataViewModel.systemStoragePercentage, textView4)
        internalProgress.setProgressAndText(
            dataViewModel.internalStoragePercentage,
            internalPercentage
        )
        launchAndCollectWithViewLifecycle(dataViewModel.batteryInfo) { type ->
            batteryPercentage.text = type.data.first().details
            batteryProgress.progress = type.data.first().details.parsePercentage()
        }
    }

    private fun ProgressBar.setProgressAndText(percentage: Int, textView: TextView) {
        progress = percentage
        textView.text = resources.getString(R.string.percentage_format, percentage.toString())
    }

    private fun handleCpuInfoResult(uiResult: UiResult<CpuData>) {
        when (uiResult) {
            is UiResult.Error -> showToast(uiResult.throwable.message.orEmpty())
            is UiResult.Success -> updateCpuInfoList(uiResult)
        }
    }

    private fun updateCpuInfoList(uiResult: UiResult.Success<CpuData>) {
        binding.listView.withModels {
            uiResult.data.frequencies.forEachIndexed { index, frequency ->
                cpuProgress {
                    id(uiResult.data.coreNumber)
                    position(index + 1)
                    cpuInfo(frequency)
                }
            }
        }
    }

    private fun handleSettings() = nav(
        R.id.containerFragment,
        ContainerFragmentDirections.actionDashboardToSettingsFragment()
    )

    private fun ArcProgress.setRamValueAsync(
        flow: Flow<UiResult<RamData>>
    ) = apply {
        launchAndCollectWithViewLifecycle(flow) { handleRamValueResult(it) }
    }

    private fun handleRamValueResult(result: UiResult<RamData>) {
        when (result) {
            is UiResult.Error -> showToast(result.throwable.message.orEmpty())
            is UiResult.Success -> {
                binding.arcProgress.animate(result.data.percentageAvailable)
                binding.ramTxt.text = resources.getString(
                    R.string.ram_text,
                    result.data.available,
                    result.data.total
                )
            }
        }
    }

    private fun ArcProgress.animate(percentageAvailable: Int) = apply {
        ObjectAnimator.ofInt(this, "progress", percentageAvailable).setDuration(1000).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
