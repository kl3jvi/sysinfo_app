package com.kl3jvi.sysinfo.view.fragments

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sysinfo.R
import com.example.sysinfo.databinding.SystemFragmentBinding
import com.example.sysinfo.information
import com.kl3jvi.sysinfo.utils.launchAndCollectWithViewLifecycle
import com.kl3jvi.sysinfo.viewmodel.DataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SystemFrag : Fragment(R.layout.system_fragment) {

    private var _binding: SystemFragmentBinding? = null
    private val binding get() = _binding!!
    private val dataViewModel: DataViewModel by viewModel()
    private var colorChangeJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SystemFragmentBinding.bind(view)
        setupUIElements()
        startColorAnimation(binding.cardView)
    }

    private fun setupUIElements() {
        binding.listWithItems.layoutManager = LinearLayoutManager(requireContext())
        launchAndCollectWithViewLifecycle(dataViewModel.uptimeFlow) { systemUptimeInfo ->
            binding.listWithItems.withModels {
                dataViewModel.systemInfo.forEach { info ->
                    information {
                        id(info.title)
                        data(info)
                    }
                }
                // ADDS SYSTEM UPTIME FLOW
                information {
                    id(systemUptimeInfo.title)
                    data(systemUptimeInfo)
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
        binding.sdk.text = getString(
            R.string.sdk_version,
            dataViewModel.systemInfo.find { it.title == "Android Version" }?.details
        )
    }

    private fun startColorAnimation(cardView: CardView) {
        colorChangeJob = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            val colors = listOf(
                Color.parseColor("#009688"),
                Color.parseColor("#FFD166"),
                Color.parseColor("#D2691E")
            )

            while (isActive) {
                val startColor = cardView.cardBackgroundColor.defaultColor
                val endColor = colors.random()

                val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
                colorAnimation.duration = 500 // duration in milliseconds
                colorAnimation.addUpdateListener { animator ->
                    cardView.setCardBackgroundColor(animator.animatedValue as Int)
                }
                colorAnimation.start()

                delay(2000) // delay in milliseconds
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        colorChangeJob?.cancel() // Cancel the animation coroutine
    }
}
