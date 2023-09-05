package com.example.apphoctap.mainactivity.content_fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.example.apphoctap.R
import com.example.apphoctap.databinding.FragmentContentBinding
import com.example.apphoctap.mainactivity.MainActivityViewModel
import com.example.apphoctap.mainactivity.congratuation_fragment.CongratulationFragment
import com.example.apphoctap.ultilities.QuestionType

class ContentFragment : Fragment(R.layout.fragment_content) {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: FragmentContentBinding
    private lateinit var type: String
    private lateinit var successDialog: AlertDialog
    private lateinit var failureDialog: AlertDialog
    private lateinit var countDownTimer: CountDownTimer
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startTimerCount()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[MainActivityViewModel::class.java]

        val successDialogBuilder = AlertDialog.Builder(requireContext())
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

        viewModel.getState().observe(viewLifecycleOwner) {
            Log.d("KEtqua","${it.correctAnswer}")
            binding.score.text = it.score.toString()
            if (it.currentQuestion != null) {
                binding.textCategory.text = it.currentQuestion.category
                binding.textQuestion.text =
                    Html.fromHtml(it.currentQuestion.question, HtmlCompat.FROM_HTML_MODE_LEGACY)
                Log.e("Answer", "${it.correctAnswer}")
                type = it.currentQuestion.type
                if (type == QuestionType.multiple.name) {
                    binding.answer3.visibility = View.VISIBLE
                    binding.answer4.visibility = View.VISIBLE
                    binding.answer1.text =
                        Html.fromHtml(it.listAnswer[0], HtmlCompat.FROM_HTML_MODE_LEGACY)
                    binding.answer2.text =
                        Html.fromHtml(it.listAnswer[1], HtmlCompat.FROM_HTML_MODE_LEGACY)
                    binding.answer3.text =
                        Html.fromHtml(it.listAnswer[2], HtmlCompat.FROM_HTML_MODE_LEGACY)
                    binding.answer4.text =
                        Html.fromHtml(it.listAnswer[3], HtmlCompat.FROM_HTML_MODE_LEGACY)
                } else {
                    binding.answer1.text = "True"
                    binding.answer2.text = "False"
                    binding.answer3.visibility = View.INVISIBLE
                    binding.answer4.visibility = View.INVISIBLE
                }
                setClickListener(it.correctAnswer)
                binding.score.text = it.score.toString()
            }
        }
        return binding.root
    }

    private fun View.setOnClickListenerWithCheck(answer: String) {
        setOnClickListener {
            if (this is Button) {
                if (type == QuestionType.multiple.name && text == Html.fromHtml(
                        answer,
                        HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                    || type == QuestionType.boolean.name && text == answer
                ) {
                    setBackgroundColor(ContextCompat.getColor(context, R.color.pinkBackground))
                    viewModel.increaseScore(10)
                    successDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    successDialog.show()
                } else {
                    failureDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    failureDialog.show()
                }
                countDownTimer.cancel()
            }
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    if (failureDialog.isShowing) {
                        if (viewModel.getState().value?.isContest == true )
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.contentPanel, CongratulationFragment()).commit()
                    } else {
                        viewModel.increaseQuestionIndex()
                        startTimerCount()
                        background = ContextCompat.getDrawable(
                            context,
                            R.drawable.app_button_white_background
                        )
                    }
                    successDialog.dismiss()
                    failureDialog.dismiss()
                }, 1500
            )
        }
    }

    private fun startTimerCount(){
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(p0: Long) {
                binding.timeCounter.text = (p0 / 1000).toString()
            }
            override fun onFinish() {
                countDownTimer.cancel()
                if (viewModel.getState().value?.isContest == true) {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.contentPanel, CongratulationFragment()).commit()
                } else {
                    viewModel.increaseQuestionIndex()
                    startTimerCount()
                }
            }

        }
        countDownTimer.start()
    }

    private fun setClickListener(answer: String) {
        binding.answer1.setOnClickListenerWithCheck(answer)
        binding.answer2.setOnClickListenerWithCheck(answer)
        binding.answer3.setOnClickListenerWithCheck(answer)
        binding.answer4.setOnClickListenerWithCheck(answer)
    }

}