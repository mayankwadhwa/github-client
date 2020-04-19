package com.mayankwadhwa.github_client.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mayankwadhwa.github_client.model.RepoModel
import kotlinx.coroutines.CoroutineScope

@Database(entities = [RepoModel::class], version = 1)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun githubDao(): GithubDao

    companion object {
        @Volatile
        private var INSTANCE: GithubDatabase? = null


        fun getDatabase(context: Context): GithubDatabase {
            return INSTANCE ?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GithubDatabase::class.java,
                    "github_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }

        }

    }
}