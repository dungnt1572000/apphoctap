package com.example.apphoctap.authen.account_detail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.apphoctap.R
import com.example.apphoctap.authen.change_password.ChangePasswordFragment
import com.example.apphoctap.databinding.FragmentAccountDetailBinding
import com.example.apphoctap.model.UserDetail
import com.example.apphoctap.repository.UserRepository
import com.example.apphoctap.ultilities.Constant
import com.example.apphoctap.ultilities.loading.LoadingDialogManager
import com.google.firebase.auth.FirebaseUser

class AccountDetailFragment : Fragment(R.layout.fragment_account_detail) {

    private lateinit var binding: FragmentAccountDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LoadingDialogManager.showDialog(requireContext())
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        val data = arguments?.getParcelable(Constant.KEY_AUTHEN, FirebaseUser::class.java)
        val userRepository = UserRepository()

        binding = FragmentAccountDetailBinding.inflate(inflater, container, false)

        data?.let { firebaseUser ->
            binding.userName.text = firebaseUser.email
            userRepository.getUserDetail(firebaseUser.uid)
                .addOnSuccessListener {
                    LoadingDialogManager.dismissLoadingDialog()
                    val userDetails = it.toObject(UserDetail::class.java)
                    Log.d("User: ", "${userDetails?.name}")
                    if (userDetails != null) {
                        binding.userName.text = userDetails.name
                        binding.maxPoint.text =
                            binding.maxPoint.text.toString().plus(userDetails.point.toString())
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Can't take user information",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        binding.changePassword.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.authen_content, ChangePasswordFragment()).commit()
        }

        binding.logout.setOnClickListener {
            run {
                userRepository.signOut()
                activity?.finish()
            }
        }
        binding.deleteAccountView.setOnClickListener {
            run {
                LoadingDialogManager.showDialog(requireContext())
                userRepository.deleteUser()
                    ?.addOnCompleteListener { task ->
                        LoadingDialogManager.dismissLoadingDialog()
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Account has been deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                            activity?.finish()
                        }
                    }
            }
        }
        return binding.root
    }
}