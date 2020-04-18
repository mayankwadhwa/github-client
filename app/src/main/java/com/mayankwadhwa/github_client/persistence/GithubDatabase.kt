package com.mayankwadhwa.github_client.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mayankwadhwa.github_client.model.RepoModel

@Database(entities = [RepoModel::class], version = 1)
abstract class GithubDatabase: RoomDatabase() {
    abstract fun githubDao(): GithubDao
}