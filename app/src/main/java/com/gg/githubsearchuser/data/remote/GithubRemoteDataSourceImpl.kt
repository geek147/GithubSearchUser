package com.gg.githubsearchuser.data.remote

import com.gg.githubsearchuser.base.NetworkResult
import com.gg.githubsearchuser.data.remote.entity.GithubDetailUserResponse
import com.gg.githubsearchuser.data.remote.entity.GithubUserSearchResponse
import com.gg.githubsearchuser.utils.toNetworkResult
import javax.inject.Inject

class GithubRemoteDataSourceImpl @Inject constructor(
    private val githubSearchService: GithubSearchService
) : GithubRemoteDataSource {
    override suspend fun searchUser(query: String, page: Int, per_page: Int): NetworkResult<GithubUserSearchResponse> {
        return githubSearchService.searchUser(query, page, per_page).toNetworkResult()
    }

    override suspend fun getUserDetail(userName: String): NetworkResult<GithubDetailUserResponse> {
        return githubSearchService.getUserDetail(userName).toNetworkResult()
    }
}
