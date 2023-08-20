package com.example.apphoctap.mainactivity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apphoctap.api.ApiClient
import com.example.apphoctap.model.Question
import com.example.apphoctap.model.QuestionDetails
import com.example.apphoctap.model.TriviaCategories
import com.example.apphoctap.repository.Repository
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer

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
            val updateScore = it.score
            state.value = it.copy(score = updateScore)
        }
    }

    fun setContestMode(contestMode:Boolean){
        state.value?.let {
            state.value = it.copy(isContest = contestMode)
        }
    }


    fun increaseQuestionIndex() {
       val myindex = state.value?.index?.plus(1)
        state.value = myindex?.let { state.value?.copy(index = it) }
        if (state.value?.index!! < 10) {
            val currentQuestion = state.value!!.question!!.results[myindex!!]
            state.value = state.value!!.copy(currentQuestion = currentQuestion)
        }
    }

    fun fetchCategoriesData() {
        compositeDisposable.add(repository.getCategory().subscribe(
            {
                state.value = state.value?.copy(categories = it)
            }, {
                state.value = state.value?.copy(errorResult = it)
            }
        ))
    }

    fun fetchQuestionData(difficulty:String?) {
        compositeDisposable.add(repository.getQuestion(difficulty = difficulty)
            .doOnSubscribe(
                Consumer {
                    state.value = state.value?.copy(loadingStatus = true)
                }
            ).doFinally {
                increaseQuestionIndex()
                state.value = state.value?.copy(loadingStatus = false)
            }
            .subscribe(
                {
                    state.value = state.value?.copy(question = it)
                }, {
                    state.value = state.value?.copy(errorResult = it)
                }
            )
        )
    }
}