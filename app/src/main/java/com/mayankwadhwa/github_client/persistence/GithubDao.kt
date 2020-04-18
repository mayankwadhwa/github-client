package com.mayankwadhwa.github_client.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mayankwadhwa.github_client.model.RepoModel

@Dao
interface GithubDao {
    @Query("SELECT * FROM TrendingRepo")
    fun getTrendingList(): LiveData<List<RepoModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTrendingList(listOfRepoModel: List<RepoModel>)
}
