package com.example.apphoctap.authen.sign_up

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.apphoctap.R
import com.example.apphoctap.authen.account_detail.AccountDetailFragment
import com.example.apphoctap.databinding.FragmentSignUpBinding
import com.example.apphoctap.repository.UserRepository
import com.example.apphoctap.ultilities.Constant
import com.example.apphoctap.ultilities.loading.LoadingDialogManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {


    private lateinit var viewModel: SignUpViewModel

    private lateinit var binding: FragmentSignUpBinding

    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        binding.signUpConfirmPassword.addTextChangedListener { text ->
            if (text.toString() != binding.signUpPassword.text.toString()) {
                binding.errorTextView.text = "Password and Confirm Password are not same"
            } else {
                binding.errorTextView.text = ""
            }
        }
        binding.signUpBtn.setOnClickListener {
            run {
                LoadingDialogManager.showDialog(requireContext())
                if (binding.errorTextView.text.isEmpty()) {
                    UserRepository().signUp(
                        binding.signUpUserName.text.toString(),
                        binding.signUpPassword.text.toString()
                    )
                        .addOnCompleteListener { task ->
                            LoadingDialogManager.dismissLoadingDialog()
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    requireContext(),
                                    "Successed",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                task.result.user?.let {
                                    val bundle = Bundle()
                                    bundle.putParcelable(Constant.KEY_AUTHEN, it)
                                    val accountDetailFragment = AccountDetailFragment()
                                    accountDetailFragment.arguments = bundle
                                    parentFragmentManager.beginTransaction()
                                        .replace(R.id.authen_content, accountDetailFragment)
                                        .commit()
                                }

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(
                                    requireContext(),
                                    "${task.exception?.message}",
                                    Toast.LENGTH_SHORT,
                                ).show()

                            }
                        }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "You have error haven't been fixed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return binding.root
    }


}