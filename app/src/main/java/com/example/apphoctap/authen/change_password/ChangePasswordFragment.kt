package com.example.apphoctap.authen.change_password

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.apphoctap.R
import com.example.apphoctap.databinding.FragmentChangePasswordBinding
import com.example.apphoctap.repository.UserRepository
import com.example.apphoctap.ultilities.loading.LoadingDialogManager

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    lateinit var binding: FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        binding.changePasswordNewPasswordConfirm.addTextChangedListener {
            if (it.toString() != binding.changePasswordNewPassword.text.toString()) {
                binding.changePasswordError.text =
                    "New Password and New Password Confirm are not same"
            } else {
                binding.changePasswordError.text = ""
            }
        }
        binding.changePasswordBtn.setOnClickListener {
            run {
                if (binding.changePasswordError.text.isEmpty()) {
                    LoadingDialogManager.showDialog(requireContext())
                    UserRepository().updatePassword(binding.changePasswordNewPassword.text.toString())
                        ?.addOnCompleteListener { task ->
                            LoadingDialogManager.dismissLoadingDialog()
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    requireContext(),
                                    "Change Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                activity?.finish()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e("Errr", "${task.exception?.message}")
                            }
                        }

                } else {
                    Toast.makeText(requireContext(), "Check condition password", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        return binding.root
    }


}