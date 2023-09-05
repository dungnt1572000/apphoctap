package com.example.apphoctap.authen.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.apphoctap.R
import com.example.apphoctap.authen.account_detail.AccountDetailFragment
import com.example.apphoctap.authen.sign_up.SignUpFragment
import com.example.apphoctap.databinding.FragmentLoginBinding
import com.example.apphoctap.repository.UserRepository
import com.example.apphoctap.ultilities.loading.LoadingDialogManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {
            run {
                LoadingDialogManager.showDialog(requireContext())
                UserRepository().signIn(
                    binding.userName.text.toString(),
                    binding.password.text.toString()
                )
                    .addOnCompleteListener { task ->
                        LoadingDialogManager.dismissLoadingDialog()
                        if (task.isSuccessful) {
                            task.result.user?.let {
                                Log.e("User", "${it.email}")
                                val bundle = Bundle()
                                bundle.putParcelable("userInfor", it)
                                val accountDetailFragment = AccountDetailFragment()
                                accountDetailFragment.arguments = bundle
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.authen_content, accountDetailFragment).commit()
                            }

                        } else {
                            Toast.makeText(
                                requireContext(),
                                "${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        binding.registerBtn.setOnClickListener {
            run {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.authen_content, SignUpFragment()).commit()
            }
        }
        return binding.root
    }
}