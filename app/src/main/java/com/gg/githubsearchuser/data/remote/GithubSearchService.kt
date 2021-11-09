package com.gg.githubsearchuser.data.remote

import com.gg.githubsearchuser.data.remote.entity.GithubUserSearchResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubSearchService {
    @GET("search/users")
    suspend fun searchUser(
        @Query("q") query: String,
        @Query("page") page: Int
    ): Response<GithubUserSearchResponse>

    companion object {
        operator fun invoke(retrofit: Retrofit): GithubSearchService = retrofit.create(GithubSearchService::class.java)
    }
}
