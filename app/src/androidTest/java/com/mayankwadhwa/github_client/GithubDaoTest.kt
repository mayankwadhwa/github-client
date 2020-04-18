package com.mayankwadhwa.github_client

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.javafaker.Faker
import com.mayankwadhwa.github_client.model.BuiltBy
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.persistence.GithubDao
import com.mayankwadhwa.github_client.persistence.GithubDatabase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor


@RunWith(AndroidJUnit4::class)
class GithubDaoTest {
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var githubDatabase: GithubDatabase
    private lateinit var githubDao: GithubDao

    private val faker = Faker()
    private val repoModel = RepoModel(
        faker.name().firstName(),
        faker.avatar().image(),
        faker.number().randomDigit(),
        faker.leagueOfLegends().champion(),
        faker.number().randomDigit(),
        faker.name().fullName(),
        faker.number().randomDigit(),
        faker.avatar().image(),
        emptyList()
    )
    val listOfRepoModel = listOf(repoModel)


    @Before
    fun initDb() {
        githubDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            GithubDatabase::class.java
        ).build()

        githubDao = githubDatabase.githubDao()
    }

    @After
    fun closeDb() {
        githubDatabase.close()
    }


    @Test
    fun getTrendingList_returnsEmptyList() {
        val testObserver: Observer<List<RepoModel>> = mock()
        githubDao.getTrendingList().observeForever(testObserver)
        verify(testObserver).onChanged(emptyList())
    }

    @Test
    fun saveTrendingList_savesData() {
        githubDao.saveTrendingList(listOfRepoModel)
        val testObserver: Observer<List<RepoModel>> = mock()

        githubDao.getTrendingList().observeForever(testObserver)

        val listClass = ArrayList::class.java as Class<ArrayList<RepoModel>>

        val argumentCaptor = ArgumentCaptor.forClass(listClass)

        verify(testObserver).onChanged(argumentCaptor.capture())

        assertTrue(argumentCaptor.value.size > 0)
    }

    @Test
    fun getAllRetrievesData() {
        githubDao.saveTrendingList(listOfRepoModel)
        val testObserver: Observer<List<RepoModel>> = mock()
        githubDao.getTrendingList().observeForever(testObserver)

        val listClass = ArrayList::class.java as Class<ArrayList<RepoModel>>

        val argumentCaptor = ArgumentCaptor.forClass(listClass)

        verify(testObserver).onChanged(argumentCaptor.capture())

        val capturedArgument = argumentCaptor.value
        assertTrue(capturedArgument.contains(repoModel))
    }


}