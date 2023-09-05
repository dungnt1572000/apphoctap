package com.example.apphoctap.model

import com.example.apphoctap.ultilities.QuestionDifficulty
import java.io.Serializable

data class MainActivityArgument(
    val category: Int?,
    val difficulty: String?
) : Serializable