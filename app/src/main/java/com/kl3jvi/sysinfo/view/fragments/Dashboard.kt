package com.kl3jvi.sysinfo.view.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sysinfo.R
import com.example.sysinfo.databinding.DashboardFragmentBinding
import com.kl3jvi.sysinfo.utils.launchAndRepeatWithViewLifecycle
import com.kl3jvi.sysinfo.view.adapters.CustomCpuAdapter
import com.kl3jvi.sysinfo.viewmodel.DashboardViewModel
import kotlinx.coroutines.launch
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

        launchAndRepeatWithViewLifecycle {
            launch{
                dashboardViewModel.ramInfo.collect {
                    val number = it.percentageAvailable
                    ObjectAnimator.ofInt(binding.arcProgress, "progress", number)
                        .setDuration(1000)
                        .start()
                }
            }
        }



//        ObjectAnimator.ofInt(binding.arcProgress, "progress", num.toInt())
//            .setDuration(1000)
//            .start()

//        lifecycleScope.launchWhenCreated {
//            mDashboardViewModel.randomFlow().collect { list ->
//                mCpuAdapter.passFrequencies(list)
//                Log.e("List", list.toString())
//            }
//
//        }

        binding.listView.layoutManager = GridLayoutManager(requireActivity(), 1)
        binding.listView.adapter = cpuAdapter
        binding.listView.itemAnimator = null
    }
}
