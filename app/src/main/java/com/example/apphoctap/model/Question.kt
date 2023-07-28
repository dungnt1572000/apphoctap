package com.example.apphoctap.model

import com.google.gson.annotations.SerializedName

data class Question(
    @SerializedName("response_code") val responseCode: Int,
    @SerializedName("results") val results: ArrayList<QuestionDetails>,
)

data class QuestionDetails(
    val category:String,
    val type:String,
    val difficulty:String,
    val question:String,
    @SerializedName("correct_answer") val correctAnswer:String,
    @SerializedName("incorrect_answers") val incorrectAnswers:ArrayList<String>
)
