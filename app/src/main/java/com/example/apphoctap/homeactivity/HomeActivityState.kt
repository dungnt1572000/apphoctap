package com.example.apphoctap.homeactivity

import com.example.apphoctap.model.Category
import com.example.apphoctap.model.TriviaCategories

data class HomeActivityState(
    val loadingStatus: Boolean? = true,
    val categoriesList: List<Category>
)