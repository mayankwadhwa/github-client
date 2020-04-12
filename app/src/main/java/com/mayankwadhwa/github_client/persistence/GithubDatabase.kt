package com.mayankwadhwa.github_client.persistence

import androidx.room.RoomDatabase

abstract class GithubDatabase: RoomDatabase() {
    abstract fun githubDao(): GithubDao
}