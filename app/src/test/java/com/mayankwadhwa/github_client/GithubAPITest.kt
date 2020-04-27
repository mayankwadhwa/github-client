package com.mayankwadhwa.github_client

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.javafaker.Faker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mayankwadhwa.github_client.model.BuiltBy
import com.mayankwadhwa.github_client.model.RepoModel
import com.mayankwadhwa.github_client.network.ApiResponse
import com.mayankwadhwa.github_client.network.GithubAPI
import com.mayankwadhwa.github_client.network.LiveDataCallAdapterFactory
import com.mayankwadhwa.github_client.repository.GithubRepositoryImpl
import com.mayankwadhwa.github_client.util.ApiUtil
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*


@ExperimentalCoroutinesApi
class GithubAPITestUsingMockServer {

    @get:Rule
    val mockWebServer = MockWebServer()

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()


    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
    }

    private val githubAPI by lazy {
        retrofit.create(GithubAPI::class.java)
    }

    private fun getJson(path: String): String {
        // Load the JSON response
        val uri = this.javaClass.classLoader?.getResource(path)
        val file = File(uri?.path)
        return String(file.readBytes())
    }


    @Test
    fun getTrendingListReturns_ListOfRepoModel() = runBlockingTest {
        mockWebServer.enqueue(
                MockResponse()
                        .setBody(getJson("trending_repositories.json"))
                        .setResponseCode(200)
        )


        val trendingList = githubAPI.getTrendingRepositories()
        val observer = mock<Observer<ApiResponse<List<RepoModel>>>>()
        val myType = object : TypeToken<List<RepoModel>>() {}.type
        trendingList.observeForever(observer)
        val value = Gson().fromJson<List<RepoModel>>(getJson("trending_repositories.json"), myType)
        verify(observer).onChanged(ApiResponse.create(Response.success(value)))
    }
}

