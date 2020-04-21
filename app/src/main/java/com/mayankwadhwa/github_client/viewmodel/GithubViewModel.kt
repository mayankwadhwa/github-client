package com.mayankwadhwa.github_client.viewmodel

import androidx.lifecycle.*
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.repository.GithubRepository
import com.mayankwadhwa.github_client.repository.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GithubViewModel(private val repository: GithubRepository) : ViewModel() {

    private var _init: MutableLiveData<String?> = MutableLiveData("")
    private var trendingListLiveData = Transformations.switchMap(_init){
        repository.getTrendingRepositories()
    }

    fun getTrendingList(): LiveData<Resource<List<RepoModel>>> = trendingListLiveData


    fun retry(){
        _init.value?.let {
            _init.value = it
        }
    }
}
