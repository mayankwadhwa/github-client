package com.mayankwadhwa.github_client

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.github.javafaker.Faker
import com.mayankwadhwa.github_client.coroutines.ManagedCoroutineScope
import com.mayankwadhwa.github_client.model.BuiltBy
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.network.GithubAPI
import com.mayankwadhwa.github_client.persistence.GithubDao
import com.mayankwadhwa.github_client.repository.GithubRepository
import com.mayankwadhwa.github_client.repository.GithubRepositoryImpl
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
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
import org.mockito.verification.VerificationMode
import java.util.*
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RepositoryUnitTest {

    @ExperimentalCoroutinesApi
    class TestScope(override val coroutineContext: CoroutineContext) : ManagedCoroutineScope {
        val scope = TestCoroutineScope(coroutineContext)
        override fun launch(block: suspend CoroutineScope.() -> Unit): Job {
            return scope.launch {
                block.invoke(this)
            }
        }
    }


    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()


    private val testDispatcher = TestCoroutineDispatcher()


    private lateinit var repository: GithubRepository

    @Mock
    private lateinit var api: GithubAPI

    @Mock
    private lateinit var dao: GithubDao

    val faker = Faker()
    val repoModel = RepoModel(
        faker.name().firstName(),
        faker.avatar().image(),
        faker.number().randomDigit(),
        faker.leagueOfLegends().champion(),
        faker.number().randomDigit(),
        faker.name().fullName(),
        faker.number().randomDigit(),
        faker.avatar().image(),
        Date().time,
        listOf(
            BuiltBy(
                faker.avatar().image(), faker.avatar().image(), faker.name().username()
            )
        )
    )
    val listOfRepoModel = listOf(repoModel)

    private val managedCoroutineScope: ManagedCoroutineScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = GithubRepositoryImpl(api, dao, managedCoroutineScope)
    }

    @Test
    fun `getTrendingRepositories fetch from database when it is not empty`() = runBlockingTest {
        val listLiveData: MutableLiveData<List<RepoModel>> = MutableLiveData(listOfRepoModel)
        whenever(dao.getTrendingList()).thenReturn(listLiveData)
        repository.getTrendingRepositories()
        verify(dao, times(2)).getTrendingList()
    }

    @Test
    fun `getTrendingRepositories fetch from network when database is empty`() = runBlockingTest {
        val emptyListLiveData: MutableLiveData<List<RepoModel>> = MutableLiveData(emptyList())
        whenever(dao.getTrendingList()).thenReturn(emptyListLiveData)
        repository.getTrendingRepositories()
        verify(api).getTrendingRepositories()
    }


    @Test
    fun `getTrendingRepositories save new data to database when it is empty`() = runBlockingTest {

        val emptyListLiveData: MutableLiveData<List<RepoModel>> = MutableLiveData(emptyList())
        whenever(dao.getTrendingList()).thenReturn(emptyListLiveData)
        whenever(api.getTrendingRepositories()).thenReturn(listOfRepoModel)
        repository.getTrendingRepositories()

        verify(dao).saveTrendingList(eq(listOfRepoModel))
    }

}