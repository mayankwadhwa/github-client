package com.mayankwadhwa.github_client.repository

import androidx.lifecycle.LiveData
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.network.ApiResponse
import com.mayankwadhwa.github_client.network.GithubAPI
import com.mayankwadhwa.github_client.persistence.GithubDao
import kotlinx.coroutines.CoroutineScope
import java.util.*
import java.util.concurrent.TimeUnit

class GithubRepositoryImpl(
    private val githubAPI: GithubAPI,
    private val githubDao: GithubDao,
    private val coroutineScope: CoroutineScope
) :
    GithubRepository {

    override fun getTrendingRepositories(): LiveData<Resource<List<RepoModel>>> {
        return object : NetworkBoundResource<List<RepoModel>, List<RepoModel>>(coroutineScope) {
            override fun saveCallResult(item: List<RepoModel>) {
                githubDao.saveTrendingList(item)
            }

            override fun shouldFetch(data: List<RepoModel>?): Boolean {
                if (!data.isNullOrEmpty() && data.first().lastUpdated + FRESH_TIMEOUT > Date().time)
                    return false
                return true
            }

            override fun loadFromDb(): LiveData<List<RepoModel>> {
                return githubDao.getTrendingList()
            }

            override fun createCall(): LiveData<ApiResponse<List<RepoModel>>> {
                return githubAPI.getTrendingRepositories()
            }

        }.asLiveData()
    }


    companion object {
        val FRESH_TIMEOUT = TimeUnit.MINUTES.toMillis(1)
    }

}
