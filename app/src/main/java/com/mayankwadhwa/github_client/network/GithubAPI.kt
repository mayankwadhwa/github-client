package com.mayankwadhwa.github_client.network

import com.mayankwadhwa.github_client.model.RepoModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface GithubAPI {
    @GET("repositories")
    suspend fun getTrendingRepositories(): List<RepoModel>?

    companion object Factory {
        fun create(): GithubAPI {
            val client = OkHttpClient.Builder().apply {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BASIC
                addInterceptor(interceptor)
            }.build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://github-trending-api.now.sh/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(GithubAPI::class.java)
        }
    }
}
