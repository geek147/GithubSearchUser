package com.gg.githubsearchuser.data.remote

import com.gg.githubsearchuser.base.NetworkResult
import com.gg.githubsearchuser.data.remote.entity.GithubUserSearchResponse

interface GithubRemoteDataSource {
    suspend fun searchUser(query: String, page: Int): NetworkResult<GithubUserSearchResponse>
}
