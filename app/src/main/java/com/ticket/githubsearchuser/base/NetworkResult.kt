package com.ticket.githubsearchuser.base

sealed class NetworkResult<out T : Any> {
    data class Success<T : Any>(val value: T?) : NetworkResult<T>()
    data class Error(
        val errorMessage: String,
        val httpCode: Int,
    ) : NetworkResult<Nothing>()
}