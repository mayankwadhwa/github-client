package com.mayankwadhwa.github_client.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mayankwadhwa.github_client.TestScope
import com.mayankwadhwa.github_client.util.Factory
import com.mayankwadhwa.github_client.coroutines.ManagedCoroutineScope
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.network.GithubAPI
import com.mayankwadhwa.github_client.persistence.GithubDao
import com.mayankwadhwa.github_client.util.ApiUtil
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryUnitTest {


    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()


    private val testDispatcher = TestCoroutineDispatcher()


    private lateinit var repository: GithubRepository

    @Mock
    private lateinit var api: GithubAPI

    @Mock
    private lateinit var dao: GithubDao


    private val testScope: ManagedCoroutineScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = GithubRepositoryImpl(api, dao, testScope)
    }


    @Test
    fun loadRepoFromNetwork() = runBlockingTest {
        val dbData = MutableLiveData<List<RepoModel>>()
        whenever(dao.getTrendingList()).thenReturn(dbData)

        val repoList = Factory.makeTrendingList()
        val call = ApiUtil.successCall(repoList)
        whenever(api.getTrendingRepositories()).thenReturn(call)

        val data = repository.getTrendingRepositories()
        // Check the database content was fetched the first time
        verify(dao).getTrendingList()
        verifyNoMoreInteractions(api)


        val observer = mock<Observer<Resource<List<RepoModel>>>>()
        data.observeForever(observer)
        verifyNoMoreInteractions(api)

        verify(observer).onChanged(Resource.Loading(null))

        val updatedDbData = MutableLiveData<List<RepoModel>>()
        whenever(dao.getTrendingList()).thenReturn(updatedDbData)

        dbData.postValue(null)
        verify(api).getTrendingRepositories()

        Thread.sleep(1000)
        verify(dao).saveTrendingList(repoList)
    }

}