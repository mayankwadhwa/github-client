package com.mayankwadhwa.github_client.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mayankwadhwa.github_client.model.RepoModel

@Dao
interface GithubDao {

    @Query("SELECT * FROM TrendingRepo")
    fun getTrendingList(): LiveData<List<RepoModel>>

    @Query("SELECT COUNT(*) FROM TrendingRepo WHERE lastUpdated >= :timeout")
    suspend fun hasRecentlyFetched(timeout: Long): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTrendingList(listOfRepoModel: List<RepoModel>)

}
