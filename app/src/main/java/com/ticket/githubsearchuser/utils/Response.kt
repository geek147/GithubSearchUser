package com.ticket.githubsearchuser.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.ticket.githubsearchuser.base.ErrorEntity
import com.ticket.githubsearchuser.base.NetworkResult
import com.ticket.githubsearchuser.base.Result
import retrofit2.Response
import java.io.Reader

fun <T : Any> Response<T>.toNetworkResult(): NetworkResult<T> {
    val entity = body()

    return if (isSuccessful && entity != null) {
        NetworkResult.Success(entity)
    } else {
        val stream = errorBody()?.charStream()
        val errorResponse = makeErrorResponse(stream)

        if (errorResponse != null) {

            NetworkResult.Error(errorResponse.message, code())
        } else {
            NetworkResult.Error("Unknown error, please try again later.", code())
        }
    }
}

private fun makeErrorResponse(charStream: Reader?): ErrorEntity? {
    if (charStream == null) return ErrorEntity.empty()

    val moshi = Moshi.Builder().build()
    val jsonAdapter: JsonAdapter<ErrorEntity> =
        moshi.adapter(ErrorEntity::class.java)

    return jsonAdapter.fromJson(charStream.toString())
}

fun NetworkResult.Error.createResultError(): Result.Error{
    return Result.Error(errorMessage, httpCode)
}
