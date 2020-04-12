package com.mayankwadhwa.github_client.network

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


data class BuiltBy(
    @SerializedName("avatar") val avatar: String,
    @SerializedName("href") val href: String,
    @SerializedName("username") val username: String
)

data class RepoModel(
    @SerializedName("author")
    val author: String,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("builtBy")
    val builtBy: List<BuiltBy>,
    @SerializedName("currentPeriodStars")
    val currentPeriodStars: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("forks")
    val forks: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("stars")
    val stars: Int,
    @SerializedName("url")
    val url: String
)

interface GithubAPI {
    @GET("repositories")
    suspend fun getTrendingRepositories(): Call<RepoModel>

    companion object Factory {
        fun create(): GithubAPI {
            val gson = GsonBuilder().create()

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
