package com.kl3jvi.sysinfo.view.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sysinfo.R
import com.example.sysinfo.databinding.StorageFragmentBinding
import com.kl3jvi.sysinfo.viewmodel.DataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StorageFragment : Fragment(R.layout.storage_fragment) {

    private var _binding: StorageFragmentBinding? = null
    private val binding get() = _binding!!
    private val dataViewModel: DataViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = StorageFragmentBinding.bind(view)
        setupUIElements()
    }

    private fun setupUIElements() {
        binding.progressBarInternal.progress = dataViewModel.internalStoragePercentage.toFloat()
        binding.externalSize.progress = dataViewModel.externalStoragePercentage.toFloat()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
