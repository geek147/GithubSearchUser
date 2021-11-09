package com.gg.githubsearchuser.base

sealed class Result<out T : Any> {

    data class Success<T : Any>(val value: T, val next: String = "") : Result<T>()

    data class Error(
        val errorMessage: String,
        val httpCode: Int = 0,
    ) : Result<Nothing>() {

        companion object {
            @JvmStatic
            fun noInternetConnection(): Error {
                return Error(
                    "Internet Connection Error",
                )
            }

            @JvmStatic
            fun emptyData(): Error {
                return Error(
                    "Oops, Page not found",
                )
            }

            @JvmStatic
            fun unknown(): Error {
                return Error("Unknown error, please try again later.", 0)
            }
        }
    }
}
