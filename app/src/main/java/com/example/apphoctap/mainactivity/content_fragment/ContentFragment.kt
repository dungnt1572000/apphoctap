package com.example.apphoctap.mainactivity.content_fragment

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.apphoctap.R
import com.example.apphoctap.databinding.FragmentContentBinding
import com.example.apphoctap.mainactivity.MainActivityViewModel
import com.example.apphoctap.ultilities.QuestionType

class ContentFragment : Fragment(R.layout.fragment_content) {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentContentBinding
    private lateinit var correctAnswer: String
    private lateinit var type: String
    private lateinit var successDialog: AlertDialog
    private lateinit var failureDialog: AlertDialog
    private val listAnswer = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]
        val successDialogBuilder = AlertDialog.Builder(requireContext())
        viewModel.getScore().observe(viewLifecycleOwner){
            binding.score.text = it.toString()
        }
        successDialogBuilder.setView(
            inflater.inflate(
                R.layout.custom_sucessed_dialog_question,
                null
            )
        ).setCancelable(false)
        successDialog = successDialogBuilder.create()
        val failureDialogBuilder = AlertDialog.Builder(requireContext())
        failureDialogBuilder.setView(
            inflater.inflate(
                R.layout.custom_failure_dialog_question,
                null
            )
        ).setCancelable(false)
        failureDialog = failureDialogBuilder.create()
        viewModel.getQuestionIndex().observe(
            viewLifecycleOwner
        ) {
            Log.d("Day la dau", "Sao khong vao day ${it.incorrectAnswers} and ${it.correctAnswer}")
            binding.textCategory.text = it.category
            binding.textQuestion.text = it.question
            correctAnswer = it.correctAnswer
            type = it.type
            if (type == QuestionType.multiple.name) {
                binding.answer3.visibility = View.VISIBLE
                binding.answer3.visibility = View.VISIBLE
                listAnswer.add(correctAnswer)
                listAnswer.addAll(it.incorrectAnswers)
                listAnswer.shuffle()
                Log.d("Shuffle xong:","${it.incorrectAnswers}")
                binding.answer1.text = listAnswer[0]
                binding.answer2.text = listAnswer[1]
                binding.answer3.text = listAnswer[2]
                binding.answer4.text = listAnswer[3]
                listAnswer.clear()
            } else {
                binding.answer1.text = "True"
                binding.answer2.text = "Flase"
                binding.answer3.visibility = View.INVISIBLE
                binding.answer4.visibility = View.INVISIBLE
            }
            setClickListener(correctAnswer)
        }

        return binding.root
    }

    private fun View.setOnClickListenerWithCheck(answer: String) {
        setOnClickListener {
            if (this is Button && text == answer) {
                setBackgroundColor(ContextCompat.getColor(context, R.color.pinkBackground))
                viewModel.increaseScore()
                successDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                successDialog.show()
            } else {
                failureDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                failureDialog.show()
            }
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    successDialog.dismiss()
                    failureDialog.dismiss()
                    viewModel.increaseQuestionIndex()
                    background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.app_button_background
                    )
                }, 0
            )
        }
    }

    private fun setClickListener(answer: String) {
        binding.answer1.setOnClickListenerWithCheck(answer)
        binding.answer2.setOnClickListenerWithCheck(answer)
        binding.answer3.setOnClickListenerWithCheck(answer)
        binding.answer4.setOnClickListenerWithCheck(answer)
    }

}