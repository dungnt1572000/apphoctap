package com.example.apphoctap.mainactivity

import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.apphoctap.R
import com.example.apphoctap.databinding.ActivityMainBinding
import com.example.apphoctap.mainactivity.congratuation_fragment.CongratulationFragment
import com.example.apphoctap.mainactivity.content_fragment.ContentFragment
import com.example.apphoctap.model.MainActivityArgument
import com.example.apphoctap.ultilities.Constant
import com.example.apphoctap.ultilities.loading.LoadingDialogManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        val msg = intent.getParcelableExtra(Constant.TEST_INTENT, MainActivityArgument::class.java)
        msg?.let {
            if (msg.category==null && msg.difficulty==null) {
                viewModel.setContestMode(true)
            } else {
                viewModel.setContestMode(false)
            }
            fetchData(it.difficulty, it.category)
        }

        viewModel.getState().observe(this) {
            if (it.loadingStatus) {
                LoadingDialogManager.showDialog(this)
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                if (it.index == 10) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contentPanel, CongratulationFragment())
                        .commit()
                }
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentPanel, ContentFragment())
            .commit()
    }

    private fun fetchData(difficulty: String? = null, category: Int? = null) {
        viewModel.fetchQuestionData(difficulty, category)
    }

}