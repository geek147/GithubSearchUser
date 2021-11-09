package com.gg.githubsearchuser.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

abstract class BaseUseCase<ResultType : Any, in Params> {
    protected abstract suspend fun build(params: Params?): ResultType

    open suspend operator fun invoke(params: Params? = null): ResultType {
        return build(params)
    }
}

abstract class BaseCaseWrapper<SuccessType : Any, in Params> :
    BaseUseCase<Result<SuccessType>, Params>() {

    suspend fun execute(params: Params? = null): Result<SuccessType> {
        return try {
            withContext(Dispatchers.IO) {
                build(params)
            }
        } catch (error: Throwable) {
            when (error) {
                is NoInternetConnectionException -> Result.Error.noInternetConnection()
                is NoSuchElementException -> Result.Error.emptyData()
                else -> Result.Error.unknown()
            }
        }
    }

    override suspend operator fun invoke(params: Params?): Result<SuccessType> {
        return execute(params)
    }
}

class NoInternetConnectionException : IOException()
