package com.mayankwadhwa.github_client.repository

import androidx.lifecycle.LiveData
import com.mayankwadhwa.github_client.model.RepoModel

interface GithubRepository {
    fun getTrendingRepositories(retry: Boolean = false): LiveData<Resource<List<RepoModel>>>
}

