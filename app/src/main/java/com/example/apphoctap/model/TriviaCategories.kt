package com.example.apphoctap.model

import com.google.gson.annotations.SerializedName

data class TriviaCategories(
    @SerializedName("trivia_categories") var listCategories: ArrayList<Category>
)

data class Category(
    @SerializedName("id") var id:Int?=null,
    @SerializedName("name") var name:String?=null
)
