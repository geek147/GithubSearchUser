package com.gg.githubsearchuser.data.remote

import com.gg.githubsearchuser.base.NetworkResult
import com.gg.githubsearchuser.data.remote.entity.GithubUserSearchResponse
import com.gg.githubsearchuser.utils.toNetworkResult
import javax.inject.Inject

class GithubRemoteDataSourceImpl @Inject constructor(
    private val githubSearchService: GithubSearchService
) : GithubRemoteDataSource {
    override suspend fun searchUser(query: String, page: Int): NetworkResult<GithubUserSearchResponse> {
        return githubSearchService.searchUser(query, page).toNetworkResult()
    }
}
