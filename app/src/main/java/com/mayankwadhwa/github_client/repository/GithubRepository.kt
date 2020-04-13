package com.mayankwadhwa.github_client.repository

import com.mayankwadhwa.github_client.model.RepoModel

interface GithubRepository {
    suspend fun getTrendingRepositories(): List<RepoModel>
}

interface RepositoryCallback<T, E> {
    fun onSuccess(t: T)
    fun onError(e: E)
}
