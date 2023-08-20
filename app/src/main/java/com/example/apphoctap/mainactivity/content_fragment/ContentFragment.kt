package com.example.apphoctap.mainactivity.content_fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.apphoctap.R
import com.example.apphoctap.databinding.FragmentContentBinding
import com.example.apphoctap.mainactivity.MainActivityViewModel
import com.example.apphoctap.mainactivity.congratuation_fragment.CongratulationFragment
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
        viewModel.getState().observe(viewLifecycleOwner){
            binding.score.text = it.score.toString()
//            Log.d("Day la dau", "Sao khong vao day ${it.incorrectAnswers} and ${it.correctAnswer}")
            binding.textCategory.text = it.currentQuestion?.category.toString()
            binding.textQuestion.text = Html.fromHtml(it.currentQuestion?.question, HtmlCompat.FROM_HTML_MODE_LEGACY)
            correctAnswer = it.currentQuestion?.correctAnswer.toString()
            type = it.currentQuestion?.type.toString()
            if (type == QuestionType.multiple.name) {
                binding.answer3.visibility = View.VISIBLE
                binding.answer4.visibility = View.VISIBLE
                listAnswer.add(correctAnswer)
                it.currentQuestion?.incorrectAnswers?.let { it1 -> listAnswer.addAll(it1) }
                listAnswer.shuffle()
                Log.d("Shuffle xong:","${it.currentQuestion?.incorrectAnswers}")
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
                    if (viewModel.getState().value?.isContest == true){
                        parentFragmentManager.beginTransaction().replace(R.id.contentPanel,CongratulationFragment()).commit()
                    }else {
                        viewModel.increaseQuestionIndex()
                        background = ContextCompat.getDrawable(
                            context,
                            R.drawable.app_button_white_background
                        )
                    }
                }, 1500
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