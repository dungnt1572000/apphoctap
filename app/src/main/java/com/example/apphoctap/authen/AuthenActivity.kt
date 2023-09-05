package com.example.apphoctap.authen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.apphoctap.R
import com.example.apphoctap.authen.account_detail.AccountDetailFragment
import com.example.apphoctap.authen.login.LoginFragment
import com.example.apphoctap.repository.UserRepository
import com.example.apphoctap.ultilities.Constant

class AuthenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authen)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = UserRepository().getCurrentUser()
        if (currentUser == null) {
            supportFragmentManager.beginTransaction().replace(R.id.authen_content, LoginFragment())
                .commit()
        } else {
            currentUser.let {
                Log.e("USSER","${it.email}")
                val bundle = Bundle()
                bundle.putParcelable(Constant.KEY_AUTHEN, it)
                val accountDetailFragment = AccountDetailFragment()
                accountDetailFragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .replace(R.id.authen_content, accountDetailFragment).commit()
            }

        }
    }
}