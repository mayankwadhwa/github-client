package com.mayankwadhwa.github_client.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.network.GithubAPI
import com.mayankwadhwa.github_client.persistence.GithubDao
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.TimeUnit

class GithubRepositoryImpl(
    private val githubAPI: GithubAPI,
    private val githubDao: GithubDao,
    private val coroutineScope: CoroutineScope
) :
    GithubRepository {

    private val errorLiveData = MutableLiveData<Throwable?>()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        errorLiveData.value = throwable
    }


    private suspend fun getTrendingRepositoriesFromNetwork() =
        withContext(coroutineScope.coroutineContext + coroutineExceptionHandler) {
            githubAPI.getTrendingRepositories()
        }

    override fun getTrendingRepositories(): LiveData<List<RepoModel>> {
        refreshTrendingList()
        return githubDao.getTrendingList()
    }


    override fun refreshTrendingList() {
        coroutineScope.launch(coroutineExceptionHandler) {
            val hasRecentlyFetched = githubDao.hasRecentlyFetched(Date().time)
            if (hasRecentlyFetched == null || hasRecentlyFetched == 0) {
                val trendingList = getTrendingRepositoriesFromNetwork()
                trendingList?.forEach { it.lastUpdated = Date().time + FRESH_TIMEOUT }
                saveTrendingRepositories(trendingList)
            }
        }
    }

    override suspend fun saveTrendingRepositories(trendingRepoModel: List<RepoModel>?) {
        trendingRepoModel?.let {
            githubDao.saveTrendingList(trendingRepoModel)
        }
    }

    companion object {
        val FRESH_TIMEOUT = TimeUnit.DAYS.toMillis(1)
    }

}
