package com.example.apphoctap.homeactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.apphoctap.R
import com.example.apphoctap.databinding.ActivityHomeBinding
import com.example.apphoctap.mainactivity.MainActivity
import com.example.apphoctap.ultilities.Constant
import com.example.apphoctap.ultilities.QuestionDifficulty

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var selection: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        val data = arrayListOf(
            QuestionDifficulty.easy.name,
            QuestionDifficulty.medium.name,
            QuestionDifficulty.hard.name
        )
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, data)
        binding.spinner.adapter = arrayAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selection = data[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selection = data[0]
            }

        }

        binding.btnTest.setOnClickListener {
            run {
                val intent = Intent(this, MainActivity::class.java).apply {
                    this.putExtra(Constant.TEST_INTENT, selection)
                }
                startActivity(intent)
            }
        }

        binding.btnContest.setOnClickListener {
            run{
                val intent = Intent(this, MainActivity::class.java).apply {
                    this.putExtra(Constant.TEST_INTENT, "")
                }
                startActivity(intent)
            }
        }

    }
}