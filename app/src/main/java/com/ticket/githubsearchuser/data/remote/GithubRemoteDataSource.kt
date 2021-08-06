package com.ticket.githubsearchuser.data.remote

import com.ticket.githubsearchuser.base.NetworkResult
import com.ticket.githubsearchuser.data.remote.entity.GithubUserSearchResponse

interface GithubRemoteDataSource {
    suspend fun searchUser(query: String, page: Int): NetworkResult<GithubUserSearchResponse>
}
