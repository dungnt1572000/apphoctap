package com.example.apphoctap.mainactivity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.apphoctap.R
import com.example.apphoctap.databinding.ActivityMainBinding
import com.example.apphoctap.mainactivity.congratuation_fragment.CongratulationFragment
import com.example.apphoctap.mainactivity.content_fragment.ContentFragment
import com.example.apphoctap.ultilities.Constant

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        val msg = intent.getStringExtra(Constant.TEST_INTENT)
        fetchData(msg)
        viewModelObserver()
        val loadingDialog = Dialog(this)
        loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        loadingDialog.setContentView(R.layout.fragment_loading)
        viewModel.getLoadingStatus().observe(this) {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
        viewModel.getIndex().observe(this) {
            if (it in 0..9) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, ContentFragment())
                    .commit()
            } else if (it == 10) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.contentPanel, CongratulationFragment())
                    .commit()
            }
        }
    }

    private fun fetchData(msg: String? = null) {
        viewModel.fetchQuestionData(msg)
        viewModel.fetchCategoriesData()
    }

    private fun viewModelObserver() {
        viewModel.getError().observe(
            this
        ) {
            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }

        viewModel.getCategoriesData().observe(this) {
            Log.d("Categories", "We have ${it.listCategories.size}")
        }

        viewModel.getQuestionData().observe(this) {
            Log.d("Question", "Question: $it")
        }
    }
}