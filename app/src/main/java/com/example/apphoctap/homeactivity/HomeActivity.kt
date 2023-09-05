package com.example.apphoctap.homeactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.apphoctap.R
import com.example.apphoctap.authen.AuthenActivity
import com.example.apphoctap.databinding.ActivityHomeBinding
import com.example.apphoctap.mainactivity.MainActivity
import com.example.apphoctap.model.MainActivityArgument
import com.example.apphoctap.ultilities.Constant
import com.example.apphoctap.ultilities.QuestionDifficulty
import com.example.apphoctap.ultilities.loading.LoadingDialogManager

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var difficultySelection: String
    private var category: Int = 0
    private lateinit var viewModel: HomeActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        viewModel = ViewModelProvider(this)[HomeActivityViewModel::class.java]
        val data = arrayListOf(
            QuestionDifficulty.easy.name,
            QuestionDifficulty.medium.name,
            QuestionDifficulty.hard.name
        )
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, data)
        binding.spinner.adapter = arrayAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                difficultySelection = data[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                difficultySelection = data[0]
            }

        }
        viewModel.getCategories()

        viewModel.getState().observe(this) { state ->
            if (state.loadingStatus == true) {
                LoadingDialogManager.showDialog(this)
            } else {
                LoadingDialogManager.dismissLoadingDialog()
                binding.categorySpinner.adapter = ArrayAdapter(
                    this,
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    state.categoriesList.map { it.name }
                )
                binding.categorySpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            state.categoriesList[p2].let { category1 ->
                                category1.id?.let {
                                    category = it
                                }
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }
                    }
            }

        }

        binding.btnTest.setOnClickListener {
            run {
                val mainActivityArgument = MainActivityArgument(category, difficultySelection)
                val intent = Intent(this, MainActivity::class.java).apply {
                    this.putExtra(Constant.TEST_INTENT, mainActivityArgument)
                }
                startActivity(intent)
            }
        }

        binding.btnContest.setOnClickListener {
            run {
                val intent = Intent(this, MainActivity::class.java).apply {
                    this.putExtra(Constant.TEST_INTENT, MainActivityArgument(null, null))
                }
                startActivity(intent)
            }
        }

        binding.accountDetail.setOnClickListener {
            run {
                val intent = Intent(this, AuthenActivity::class.java)
                startActivity(intent)
            }
        }

    }
}