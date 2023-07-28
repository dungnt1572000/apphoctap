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
    private val _categories: MutableLiveData<TriviaCategories> = MutableLiveData()
    private val _question: MutableLiveData<Question> = MutableLiveData()
    private val errorResult = MutableLiveData<Throwable>()
    private val currentQuestion = MutableLiveData<QuestionDetails>()
    private var index: MutableLiveData<Int> = MutableLiveData(-1)
    private var loadingStatus = MutableLiveData<Boolean>(true)
    fun getCategoriesData() = _categories
    fun getQuestionData() = _question
    fun getError() = errorResult
    fun getQuestionIndex() = currentQuestion
    fun getIndex() = index
    fun getLoadingStatus() = loadingStatus

    fun increaseQuestionIndex() {
        index.value = index.value!! + 1
        if (index.value!! < 10) {
            currentQuestion.value = _question.value!!.results[index.value!!]
        }
    }

    fun refreshIndex() {
        index.value = 0
    }

    fun fetchCategoriesData() {
        compositeDisposable.add(repository.getCategory().subscribe(
            {
                _categories.value = it
            }, {
                errorResult.value = it
            }
        ))
    }

    fun fetchQuestionData() {
        compositeDisposable.add(repository.getQuestion()
            .doOnSubscribe(
                Consumer {
                    loadingStatus.value = true
                }
            ).doFinally {
                increaseQuestionIndex()
                loadingStatus.value = false
            }
            .subscribe(
                {
                    _question.value = it
                }, {
                    errorResult.value = it
                }
            )
        )
    }
}