package com.ticket.githubsearchuser.data.remote

import com.ticket.githubsearchuser.base.NetworkResult
import com.ticket.githubsearchuser.data.remote.entity.GithubUserSearchResponse
import com.ticket.githubsearchuser.utils.toNetworkResult
import javax.inject.Inject

class GithubRemoteDataSourceImpl @Inject constructor(
    private val githubSearchService: GithubSearchService
) : GithubRemoteDataSource {
    override suspend fun searchUser(query: String, page: Int): NetworkResult<GithubUserSearchResponse> {
        return githubSearchService.searchUser(query, page).toNetworkResult()
    }
}