package com.example.apphoctap.mainactivity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apphoctap.api.ApiClient
import com.example.apphoctap.model.Question
import com.example.apphoctap.model.QuestionDetails
import com.example.apphoctap.model.TriviaCategories
import com.example.apphoctap.repository.Repository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivityViewModel : ViewModel() {
    private var compositeDisposable = CompositeDisposable()
    private val repository = Repository(ApiClient.apiService)
    private var state = MutableLiveData<MainActivityState>()

    init {
        state.value = MainActivityState()
    }

    fun getState() = state
    fun increaseScore(points: Int = 10) {
        state.value?.let {
            val updateScore = it.score + points
            state.value = it.copy(score = updateScore)
        }
    }

    fun setContestMode(contestMode: Boolean) {
        state.value?.let {
            state.value = it.copy(isContest = contestMode)
        }
    }


    fun increaseQuestionIndex() {
        val myindex = state.value?.index?.plus(1)
        state.value = myindex?.let { state.value?.copy(index = it) }
        if (state.value?.index!! < 10) {
            val currentQuestion = state.value!!.question!!.results[myindex!!]
            val listAnswer = currentQuestion.incorrectAnswers
            listAnswer.add(currentQuestion.correctAnswer)
            state.value = state.value!!.copy(
                currentQuestion = currentQuestion,
                listAnswer = listAnswer.shuffled(),
                correctAnswer = currentQuestion.correctAnswer
            )
        }
    }

    fun fetchQuestionData(difficulty: String?, category: Int?) {
        compositeDisposable.add(repository.getQuestion(difficulty = difficulty, category = category)
            .subscribeOn(
                Schedulers.io()
            ).observeOn(
                AndroidSchedulers.mainThread()
            )
            .doOnSubscribe(
                Consumer {
                    state.value = state.value?.copy(loadingStatus = true)
                }
            ).doFinally {
                state.value = state.value?.copy(loadingStatus = false)
            }
            .subscribe(
                {
                    val currentQuestion = it.results[0]
                    val listAnswer = currentQuestion.incorrectAnswers
                    listAnswer.add(currentQuestion.correctAnswer)
                    state.value = state.value?.copy(
                        question = it,
                        currentQuestion = currentQuestion,
                        listAnswer = listAnswer.shuffled(),
                        correctAnswer = currentQuestion.correctAnswer
                    )
                }, {
                    state.value = state.value?.copy(errorResult = it)
                }
            )
        )
    }
}