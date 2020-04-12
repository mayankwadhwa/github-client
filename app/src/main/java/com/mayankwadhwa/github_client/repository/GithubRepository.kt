package com.mayankwadhwa.github_client.repository

import com.mayankwadhwa.github_client.model.RepoModel


interface GithubRepository {
    fun getTrendingRepositories(callback: RepositoryCallback<List<RepoModel>, String>)
}

interface RepositoryCallback<T, E> {
    fun onSuccess(t: T)
    fun onError(e: E)
}
