package com.mayankwadhwa.github_client.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "TrendingRepo")
data class RepoModel(

    @SerializedName("author")
    var author: String,

    @SerializedName("avatar")
    var avatar: String,

    @SerializedName("currentPeriodStars")
    var currentPeriodStars: Int,

    @SerializedName("description")
    var description: String,

    @SerializedName("forks")
    var forks: Int,

    @SerializedName("name")
    var name: String,

    @SerializedName("stars")
    var stars: Int,

    @PrimaryKey
    @SerializedName("url")
    var url: String,

    @Ignore
    @SerializedName("builtBy")
    val builtBy: List<BuiltBy>
){
    constructor(): this("","",0,"",0,"",0,"", emptyList())
}
