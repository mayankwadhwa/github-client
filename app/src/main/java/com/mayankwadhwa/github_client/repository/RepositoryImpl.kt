package com.mayankwadhwa.github_client.repository

import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.network.GithubAPI

class RepositoryImpl(private val githubAPI: GithubAPI): GithubRepository {

    override suspend fun getTrendingRepositories(): List<RepoModel> {
        return githubAPI.getTrendingRepositories()
    }

}
