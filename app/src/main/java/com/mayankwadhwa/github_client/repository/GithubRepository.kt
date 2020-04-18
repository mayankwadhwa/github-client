package com.mayankwadhwa.github_client.repository

import androidx.lifecycle.LiveData
import com.mayankwadhwa.github_client.model.RepoModel

interface GithubRepository {
    suspend fun getTrendingRepositories(): List<RepoModel>?
    fun getTrendingRepositoriesFromDatabase(): LiveData<List<RepoModel>>
    fun saveTrendingRepositories(trendingRepoModel: List<RepoModel>?)
}

