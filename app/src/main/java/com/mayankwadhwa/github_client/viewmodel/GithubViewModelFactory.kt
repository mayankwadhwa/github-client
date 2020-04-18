package com.mayankwadhwa.github_client.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mayankwadhwa.github_client.repository.GithubRepository

class GithubViewModelFactory (private val repository: GithubRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GithubViewModel(repository) as T
    }

}