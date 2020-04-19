package com.mayankwadhwa.github_client.repository

import androidx.lifecycle.LiveData
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.network.GithubAPI
import com.mayankwadhwa.github_client.persistence.GithubDao

class GithubRepositoryImpl(private val githubAPI: GithubAPI, private val githubDao: GithubDao) :
    GithubRepository {

    override suspend fun getTrendingRepositoriesFromNetwork(): List<RepoModel>? {
        return githubAPI.getTrendingRepositories()
    }

    override suspend fun getTrendingRepositories(): LiveData<List<RepoModel>> {
        val trendingList = githubDao.getTrendingList().value
        if (trendingList.isNullOrEmpty())
            saveTrendingRepositories(getTrendingRepositoriesFromNetwork())
        return githubDao.getTrendingList()
    }

    override fun saveTrendingRepositories(trendingRepoModel: List<RepoModel>?) {
        trendingRepoModel?.let {
            githubDao.saveTrendingList(trendingRepoModel)
        }
    }


}
