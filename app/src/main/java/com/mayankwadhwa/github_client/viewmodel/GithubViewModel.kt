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

    private var _init: MutableLiveData<SortedBy> = MutableLiveData(SortedBy.NORMAL)
    private var trendingListLiveData = Transformations.switchMap(_init) {
        when (it) {
            SortedBy.STARS -> Transformations.map(repository.getTrendingRepositories()) { resource ->
                val data = resource.data?.sortedByDescending { list -> list.stars }
                resource.data = data
                resource
            }

            SortedBy.NORMAL -> repository.getTrendingRepositories()
            SortedBy.NAME -> Transformations.map(repository.getTrendingRepositories()) { resource ->
                val data = resource.data?.sortedByDescending { list -> list.name }
                resource.data = data
                resource
            }
            SortedBy.RETRY -> repository.getTrendingRepositories(true)
        }
    }

    fun getTrendingList(): LiveData<Resource<List<RepoModel>>> = trendingListLiveData


    fun retry() {
        _init.value = SortedBy.RETRY
    }

    fun sortByStars() {
        _init.value = SortedBy.STARS
    }

    fun sortByName() {
        _init.value = SortedBy.NAME
    }
}

enum class SortedBy {
    STARS, NORMAL, NAME, RETRY
}
