package com.gg.githubsearchuser.data.remote

import com.gg.githubsearchuser.base.NetworkResult
import com.gg.githubsearchuser.data.remote.entity.GithubDetailUserResponse
import com.gg.githubsearchuser.data.remote.entity.GithubUserSearchResponse

interface GithubRemoteDataSource {
    suspend fun searchUser(query: String, page: Int, per_page: Int): NetworkResult<GithubUserSearchResponse>
    suspend fun getUserDetail(
        userName: String,
    ): NetworkResult<GithubDetailUserResponse>
}
