package com.kl3jvi.sysinfo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sysinfo.databinding.ScreenFragmentBinding


class ScreenFragment : Fragment() {


    private var _binding: ScreenFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ScreenFragmentBinding.inflate(inflater, container, false)
        val root = binding.root

        return root
    }


}