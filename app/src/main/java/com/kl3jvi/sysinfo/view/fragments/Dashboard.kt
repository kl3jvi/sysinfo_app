package com.kl3jvi.sysinfo.view.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sysinfo.databinding.DashboardFragmentBinding
import com.kl3jvi.sysinfo.view.adapters.CustomCpuAdapter
import com.kl3jvi.sysinfo.viewmodel.DashboardViewModel

/**
 * A placeholder fragment containing a simple view.
 */
class Dashboard : Fragment() {


    private var _binding: DashboardFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mDashboardViewModel: DashboardViewModel
    private lateinit var mCpuAdapter: CustomCpuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DashboardFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        mDashboardViewModel.repeatFun()
        mDashboardViewModel.ram.observe(viewLifecycleOwner, { num ->
            ObjectAnimator.ofInt(binding.arcProgress, "progress", num.toInt())
                .setDuration(1000)
                .start()
            Log.e("Number", num.toString())


        })

        mDashboardViewModel.ramText.observe(viewLifecycleOwner, { num ->
            binding.ramTxt.text = num
        })

        mDashboardViewModel.frequencies.observe(viewLifecycleOwner, { list ->
            mCpuAdapter.passFrequencies(list)
            Log.e("List", list.toString())
        })

        binding.listView.layoutManager = GridLayoutManager(requireActivity(), 1)
        mCpuAdapter = CustomCpuAdapter(this)
        binding.listView.adapter = mCpuAdapter


    }


}