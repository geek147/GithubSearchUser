package com.ticket.githubsearchuser.base

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class ErrorEntity(
    @Json(name = "documentation_url")
    val documentationUrl: String?,
    @Json(name = "message")
    val message: String
) {
    companion object {
        @JvmStatic
        fun empty(): ErrorEntity {
            return ErrorEntity(
                message = "Unknown error, please try again later.",
                documentationUrl = ""
            )
        }
    }
}
