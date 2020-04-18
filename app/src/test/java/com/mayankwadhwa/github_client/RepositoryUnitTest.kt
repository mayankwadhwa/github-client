package com.mayankwadhwa.github_client

import com.mayankwadhwa.github_client.network.GithubAPI
import com.mayankwadhwa.github_client.persistence.GithubDao
import com.mayankwadhwa.github_client.repository.GithubRepository
import com.mayankwadhwa.github_client.repository.GithubRepositoryImpl
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryUnitTest {

    private lateinit var repository: GithubRepository

    @Mock
    private lateinit var api: GithubAPI

    @Mock
    private lateinit var dao: GithubDao

    @Before
    fun setup() {
        repository = GithubRepositoryImpl(api, dao)
    }


}