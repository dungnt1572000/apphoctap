package com.example.apphoctap.mainactivity.congratuation_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.apphoctap.R
import com.example.apphoctap.databinding.FragmentCongratuationBinding
import com.example.apphoctap.databinding.FragmentContentBinding
import com.example.apphoctap.mainactivity.MainActivityViewModel

class CongratulationFragment : Fragment() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentCongratuationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
        binding = FragmentCongratuationBinding.inflate(inflater, container, false)
        viewModel.getScore().observe(viewLifecycleOwner){
            binding.score.text = it.toString()
        }
        return binding.root
    }

}