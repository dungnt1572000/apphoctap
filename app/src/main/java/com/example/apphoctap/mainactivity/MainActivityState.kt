package com.example.apphoctap.mainactivity

import com.example.apphoctap.model.Question
import com.example.apphoctap.model.QuestionDetails
import com.example.apphoctap.model.TriviaCategories

data class MainActivityState(
    val categories: TriviaCategories?=null,
    val question: Question?=null,
    val errorResult:Throwable?=null,
    val currentQuestion: QuestionDetails?=null,
    val index:Int=0,
    var score:Int=0,
    val loadingStatus: Boolean = true,
    val isContest:Boolean = false,
    val correctAnswer:String="",
    val listAnswer:List<String> = emptyList(),
)
