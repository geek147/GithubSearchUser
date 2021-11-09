package com.gg.githubsearchuser.data.remote

import com.gg.githubsearchuser.BuildConfig
import com.gg.githubsearchuser.data.remote.entity.GithubDetailUserResponse
import com.gg.githubsearchuser.data.remote.entity.GithubUserSearchResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubSearchService {
    @GET("search/users")
    suspend fun searchUser(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
    ): Response<GithubUserSearchResponse>

    @GET("users/{user_name}")
    @Headers("Authorization: token ${BuildConfig.API_KEY}")
    suspend fun getUserDetail(
        @Path("user_name") userName: String,
    ): Response<GithubDetailUserResponse>

    companion object {
        operator fun invoke(retrofit: Retrofit): GithubSearchService = retrofit.create(GithubSearchService::class.java)
    }
}
