package com.example.apphoctap.api

import com.example.apphoctap.model.Question
import com.example.apphoctap.model.TriviaCategories
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api_category.php")
    fun getCategory(): Observable<TriviaCategories>

    @GET("api.php")
    fun getQuestion(
        @Query("amount") amount: Int? = null,
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null
    ): Observable<Question>
}