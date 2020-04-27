package com.mayankwadhwa.github_client.util

import com.github.javafaker.Faker
import com.mayankwadhwa.github_client.model.RepoModel
import java.util.*

object TestFactory {
    private val faker = Faker()

    fun makeTrendingList(): List<RepoModel> {
        val repoModel = RepoModel(
                faker.name().firstName(),
                faker.avatar().image(),
                faker.number().randomDigit(),
                faker.gameOfThrones().quote(),
                faker.number().randomDigit(),
                faker.name().fullName(),
                faker.number().randomDigit(),
                faker.hacker().noun(),
                faker.color().name(),
                faker.avatar().image(),
                Date().time,
                emptyList()
        )
        val listOfRepoModel = listOf(repoModel)

        return listOfRepoModel
    }

}