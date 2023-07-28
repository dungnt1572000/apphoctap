package com.example.apphoctap.repository

import com.example.apphoctap.api.ApiClient
import com.example.apphoctap.api.ApiService
import com.example.apphoctap.model.Question
import com.example.apphoctap.model.QuestionDetails
import com.example.apphoctap.model.TriviaCategories
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class Repository(private val apiService: ApiService) {
    fun getCategory(): Observable<TriviaCategories> =
        apiService.getCategory().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getQuestion(
        mount: Int? = 10,
        category: String? = null,
        difficulty: String? = "easy"
    ): Observable<Question> =
        apiService.getQuestion(mount, category, difficulty).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}