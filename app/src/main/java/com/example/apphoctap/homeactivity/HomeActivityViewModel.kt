package com.example.apphoctap.homeactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apphoctap.api.ApiClient
import com.example.apphoctap.model.Category
import com.example.apphoctap.repository.Repository
import com.example.apphoctap.ultilities.loading.LoadingDialogManager
import io.reactivex.rxjava3.disposables.CompositeDisposable

class HomeActivityViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val repository = Repository(ApiClient.apiService)
    private val homeActivityState = MutableLiveData<HomeActivityState>()
    fun getState() = homeActivityState

    init {
        homeActivityState.value = HomeActivityState(categoriesList = emptyList())
    }

    fun getCategories() {
        compositeDisposable.add(repository.getCategory().doOnSubscribe {
            homeActivityState.value = homeActivityState.value?.copy(
                loadingStatus = true
            )
        }.doFinally {
            homeActivityState.value =
                homeActivityState.value?.copy(
                    loadingStatus = false
                )
        }.subscribe { it ->
            homeActivityState.value =
                homeActivityState.value?.copy(
                    categoriesList = it.listCategories
                )

        })
    }
}