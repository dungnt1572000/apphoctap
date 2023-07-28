package com.example.apphoctap.mainactivity.content_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apphoctap.model.QuestionDetails

class ContentFragmentViewModel: ViewModel() {
    private val _data = MutableLiveData<QuestionDetails>()

}