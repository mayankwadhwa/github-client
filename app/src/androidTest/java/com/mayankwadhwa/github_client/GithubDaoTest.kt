package com.mayankwadhwa.github_client

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.javafaker.Faker
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.persistence.GithubDao
import com.mayankwadhwa.github_client.persistence.GithubDatabase
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import java.util.*
import kotlin.collections.ArrayList


@RunWith(AndroidJUnit4::class)
class GithubDaoTest {
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var tempDatabase: GithubDatabase
    private lateinit var realDatabase: GithubDatabase
    private lateinit var tempDao: GithubDao
    private lateinit var realDao: GithubDao

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
        Date().time,
        emptyList()
    )
    val listOfRepoModel = listOf(repoModel)


    @Before
    fun initDb() {
        tempDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            GithubDatabase::class.java
        ).build()

        tempDao = tempDatabase.githubDao()

        realDatabase = GithubDatabase.getDatabase(
            InstrumentationRegistry.getInstrumentation().targetContext)

        realDao = realDatabase.githubDao()
    }

    @After
    fun closeDb() {
        tempDatabase.close()
    }


    @Test
    fun getTrendingList_returnsEmptyList() {
        val testObserver: Observer<List<RepoModel>> = mock()
        tempDao.getTrendingList().observeForever(testObserver)
        verify(testObserver).onChanged(emptyList())
    }

    @Test
    fun saveTrendingList_savesData() = runBlockingTest{
        tempDao.saveTrendingList(listOfRepoModel)
        val testObserver: Observer<List<RepoModel>> = mock()

        tempDao.getTrendingList().observeForever(testObserver)

        val listClass = ArrayList::class.java as Class<ArrayList<RepoModel>>

        val argumentCaptor = ArgumentCaptor.forClass(listClass)

        verify(testObserver).onChanged(argumentCaptor.capture())

        assertTrue(argumentCaptor.value.size > 0)
    }

    @Test
    fun getAllRetrievesData() = runBlockingTest{
        tempDao.saveTrendingList(listOfRepoModel)
        val testObserver: Observer<List<RepoModel>> = mock()
        tempDao.getTrendingList().observeForever(testObserver)

        val listClass = ArrayList::class.java as Class<ArrayList<RepoModel>>

        val argumentCaptor = ArgumentCaptor.forClass(listClass)

        verify(testObserver).onChanged(argumentCaptor.capture())

        val capturedArgument = argumentCaptor.value
        assertTrue(capturedArgument.contains(repoModel))
    }


    @Test
    fun checkDatabaseContent(){
        val testObserver: Observer<List<RepoModel>> = mock()
        realDao.getTrendingList().observeForever(testObserver)

        val listClass = ArrayList::class.java as Class<ArrayList<RepoModel>>

        val argumentCaptor = ArgumentCaptor.forClass(listClass)

        verify(testObserver).onChanged(argumentCaptor.capture())

        val capturedArgument = argumentCaptor.value


    }

}