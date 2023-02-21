package com.kl3jvi.sysinfo.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sysinfo.R
import com.example.sysinfo.databinding.FragmentContainerBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.kl3jvi.sysinfo.ui.main.SectionsPagerAdapter

class ContainerFragment : Fragment(R.layout.fragment_container) {

    private var _binding: FragmentContainerBinding? = null
    private val binding get() = _binding!!

    private val tabIcons = intArrayOf(
        R.drawable.ic_dashboard,
        R.drawable.ic_devicee,
        R.drawable.ic_system,
        R.drawable.ic_storage,
        R.drawable.ic_cpu,
        R.drawable.ic_battery,
        R.drawable.ic_screen
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentContainerBinding.bind(view)
        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = SectionsPagerAdapter(requireContext(), requireActivity())
        with(binding) {
            viewPager.adapter = adapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = adapter.getPageTitle(position)
                tab.setIcon(tabIcons[position])
            }.attach()
        }
        binding.viewPager.offscreenPageLimit = 5
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
