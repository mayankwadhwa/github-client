package com.mayankwadhwa.github_client.repository

import androidx.lifecycle.LiveData
import com.mayankwadhwa.github_client.model.RepoModel
import kotlinx.coroutines.Deferred

interface GithubRepository {
    fun refreshTrendingList()
    fun getTrendingRepositories(): LiveData<List<RepoModel>>
    suspend fun saveTrendingRepositories(trendingRepoModel: List<RepoModel>?)
}

