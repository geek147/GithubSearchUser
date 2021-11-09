package com.gg.githubsearchuser.data

import com.gg.githubsearchuser.base.NetworkResult
import com.gg.githubsearchuser.base.Result
import com.gg.githubsearchuser.data.remote.GithubRemoteDataSource
import com.gg.githubsearchuser.domain.entity.User
import com.gg.githubsearchuser.domain.repository.GithubRepository
import com.gg.githubsearchuser.utils.createResultError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class GithubRepositoryImpl @Inject constructor(
    private val remoteDataSource: GithubRemoteDataSource
) : GithubRepository {

    override suspend fun searchUser(
        query: String,
        page: Int
    ): Result<List<User>> {
        return when (val result = remoteDataSource.searchUser(query, page)) {
            is NetworkResult.Success -> {
                val resultValue = result.value
                return Result.Success(resultValue?.items!!.map { it!!.toUser() })
            }
            is NetworkResult.Error -> result.createResultError()
        }
    }
}
