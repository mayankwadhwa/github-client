package com.mayankwadhwa.github_client

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.javafaker.Faker
import com.mayankwadhwa.github_client.coroutines.ManagedCoroutineScope
import com.mayankwadhwa.github_client.model.BuiltBy
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.repository.GithubRepository
import com.mayankwadhwa.github_client.viewmodel.GithubViewModel
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class TestScope(override val coroutineContext: CoroutineContext) : ManagedCoroutineScope {
    val scope = TestCoroutineScope(coroutineContext)
    override fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return scope.launch {
            block.invoke(this)
        }
    }
}

@ExperimentalCoroutinesApi
class GithubViewModelUnitTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val managedCoroutineScope: ManagedCoroutineScope = TestScope(testDispatcher)


    private lateinit var repository: GithubRepository
    private lateinit var viewModel: GithubViewModel

    private lateinit var loadingObserver: Observer<Boolean>
    private lateinit var errorObserver: Observer<Throwable?>
    private lateinit var trendingRepoObserver: Observer<List<RepoModel>>

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
        listOf(
            BuiltBy(
                faker.avatar().image(), faker.avatar().image(), faker.name().username()
            )
        )
    )
    val listOfRepoModel = listOf(repoModel)


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        viewModel = GithubViewModel(repository)
        loadingObserver = mock()
        errorObserver = mock()
        trendingRepoObserver = mock()

        viewModel.getLoading().observeForever(loadingObserver)
        viewModel.getError().observeForever(errorObserver)
        viewModel.getTrendingList().observeForever(trendingRepoObserver)
    }


    @Test
    fun init_shouldReturnTrendingList() = runBlockingTest {

        whenever(repository.getTrendingRepositoriesFromNetwork()).thenReturn(listOfRepoModel)

        viewModel.init()
        verify(trendingRepoObserver).onChanged(eq(listOfRepoModel))

    }

    @Test
    fun init_shouldShowLoading() = runBlockingTest {
        viewModel.init()
        verify(loadingObserver).onChanged(eq(true))
    }


    @Test
    fun init_shouldHideLoading_whenInitReturnsList() = runBlockingTest {
        whenever(repository.getTrendingRepositoriesFromNetwork()).thenReturn(listOfRepoModel)
        viewModel.init()

        verify(loadingObserver).onChanged(eq(false))
    }

    @Test
    fun init_shouldShowError_whenErrorOccurs() = runBlockingTest {
        whenever(repository.getTrendingRepositoriesFromNetwork()).then { throw Exception() }
        viewModel.init()
        verify(errorObserver).onChanged(eq(Exception()))
    }


}