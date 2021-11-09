package com.gg.githubsearchuser.domain.entity

data class DetailUser(
    val name: String? = null,
    val id: Int? = null,
    val loginName: String? = null,
    val company: String? = null,
    val followerCount: Int? = null,
    val followingCount: Int? = null,
    val location: String? = null,
    val email: String? = null,
    val avatarUrl: String? = null,
)
