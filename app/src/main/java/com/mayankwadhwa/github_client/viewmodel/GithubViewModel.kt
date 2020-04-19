package com.mayankwadhwa.github_client.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.repository.GithubRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class GithubViewModel(private val repository: GithubRepository) : ViewModel() {
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable?>()
    private val trendingListLiveData = MutableLiveData<List<RepoModel>>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errorLiveData.value = throwable
    }

    private val coroutineContext = viewModelScope.coroutineContext + coroutineExceptionHandler

    fun getLoading(): LiveData<Boolean> = loadingLiveData
    fun getError(): LiveData<Throwable?> = errorLiveData
    fun getTrendingList(): LiveData<List<RepoModel>> = trendingListLiveData

    fun init() {
        loadingLiveData.value = true
        viewModelScope.launch(coroutineContext) {
            val trendingRepositories = repository.getTrendingRepositoriesFromNetwork()
            if (trendingRepositories != null)
                trendingListLiveData.value = trendingRepositories
        }.invokeOnCompletion {
            loadingLiveData.value = false
        }
    }
}