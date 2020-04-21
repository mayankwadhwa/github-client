package com.mayankwadhwa.github_client.network

import androidx.lifecycle.LiveData
import com.mayankwadhwa.github_client.model.RepoModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit


interface GithubAPI {
    @GET("repositories")
    fun getTrendingRepositories(): LiveData<ApiResponse<List<RepoModel>>>

    companion object Factory {
        fun create(): GithubAPI {
            val client = OkHttpClient.Builder().apply {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BASIC
                addInterceptor(interceptor)
                readTimeout(10, TimeUnit.SECONDS)
            }.build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://github-trending-api.now.sh/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()

            return retrofit.create(GithubAPI::class.java)
        }
    }
}
