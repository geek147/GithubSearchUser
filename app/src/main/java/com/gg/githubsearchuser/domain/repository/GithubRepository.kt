package com.gg.githubsearchuser.domain.repository

import com.gg.githubsearchuser.base.Result
import com.gg.githubsearchuser.domain.entity.User

interface GithubRepository {
    suspend fun searchUser(query: String, page: Int): Result<List<User>>
}
