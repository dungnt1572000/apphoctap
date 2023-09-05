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
import com.example.apphoctap.model.UserDetail
import com.example.apphoctap.repository.UserRepository
import com.google.firebase.firestore.auth.User

class CongratulationFragment : Fragment() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentCongratuationBinding
    private val userRepository = UserRepository()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
        binding = FragmentCongratuationBinding.inflate(inflater, container, false)
        binding.score.text = viewModel.getState().value?.score.toString()

        binding.button.setOnClickListener {
            activity?.finish()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewModel.getState().value?.isContest == true) {
            val currentUser = userRepository.getCurrentUser()
            val point = viewModel.getState().value?.score
            currentUser?.let {
                userRepository.addUserRank(it.uid, UserDetail(currentUser.email!!, point!!))
            }
        }
    }

}