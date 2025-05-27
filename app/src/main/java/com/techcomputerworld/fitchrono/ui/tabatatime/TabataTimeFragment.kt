package com.techcomputerworld.fitchrono.ui.tabatatime

import android.app.Fragment
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techcomputerworld.fitchrono.R
import com.techcomputerworld.fitchrono.databinding.FragmentTabataTimeBinding


class TabataTimeFragment : Fragment() {

    private var _binding: FragmentTabataTimeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTabataTimeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}