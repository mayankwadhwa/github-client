package com.mayankwadhwa.github_client.viewmodel

import androidx.lifecycle.*
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.repository.GithubRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GithubViewModel(private val repository: GithubRepository) : ViewModel() {
    private val loadingLiveData = MutableLiveData<Boolean>()
    private var trendingListLiveData = repository.getTrendingRepositories()
    private val errorLiveData = MutableLiveData<Throwable?>()


    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        errorLiveData.value = throwable
    }

    fun getLoading(): LiveData<Boolean> = loadingLiveData
    fun getError(): LiveData<Throwable?> = errorLiveData
    fun getTrendingList(): LiveData<List<RepoModel>> = trendingListLiveData
    fun init() {

    }

    fun retry() {
        loadingLiveData.value = true
        viewModelScope.launch {
            repository.getTrendingRepositories()
        }.invokeOnCompletion {
            loadingLiveData.value = false
        }
    }
}
