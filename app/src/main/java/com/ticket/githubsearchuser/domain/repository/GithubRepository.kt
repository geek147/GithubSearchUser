package com.ticket.githubsearchuser.domain.repository

import com.ticket.githubsearchuser.base.Result
import com.ticket.githubsearchuser.domain.entity.User

interface GithubRepository {
    suspend fun searchUser(query: String, page: Int): Result<List<User>>
}
