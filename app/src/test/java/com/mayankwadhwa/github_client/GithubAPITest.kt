package com.mayankwadhwa.github_client

import com.github.javafaker.Faker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mayankwadhwa.github_client.model.BuiltBy
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.network.GithubAPI
import com.mayankwadhwa.github_client.repository.GithubRepositoryImpl
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*


class GithubAPITestUsingMockServer {

    @get:Rule
    val mockWebServer = MockWebServer()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val githubAPI by lazy {
        retrofit.create(GithubAPI::class.java)
    }

    /**
     * Helper function which will load JSON from
     * the path specified
     *
     * @param path : Path of JSON file
     * @return json : JSON from file at given path
     */
    private fun getJson(path: String): String {
        // Load the JSON response
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }


    @Test
    fun getTrendingListReturns_ListOfRepoModel() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(getJson("trending_repositories.json"))
                .setResponseCode(200)
        )


        val trendingList = githubAPI.getTrendingRepositories()
        val myType = object : TypeToken<List<RepoModel>>() {}.type
        Assert.assertEquals(
            Gson().fromJson(getJson("trending_repositories.json"), myType),
            trendingList
        )

    }
}

class GithubAPITestMockingService {
    private val faker = Faker()
    private val githubAPI: GithubAPI = mock()
    private val repository = GithubRepositoryImpl(githubAPI, any(), any())

    @Test
    fun getTrendingList_returnsList() = runBlockingTest {
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
        whenever(githubAPI.getTrendingRepositories()).thenReturn(listOfRepoModel)
        Assert.assertEquals(repository.getTrendingRepositoriesFromNetworkAsync(), listOfRepoModel)

    }
}
