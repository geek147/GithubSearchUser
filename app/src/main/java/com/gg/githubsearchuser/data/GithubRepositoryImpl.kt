package com.gg.githubsearchuser.data

import com.gg.githubsearchuser.base.NetworkResult
import com.gg.githubsearchuser.base.Result
import com.gg.githubsearchuser.data.remote.GithubRemoteDataSource
import com.gg.githubsearchuser.domain.entity.DetailUser
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
        page: Int,
        per_page: Int
    ): Result<List<User>> {
        return when (val result = remoteDataSource.searchUser(query, page, per_page)) {
            is NetworkResult.Success -> {
                val resultValue = result.value
                if (resultValue?.items?.isNotEmpty() == true) {
                    val newList = mutableListOf<User>()
                    resultValue.items.forEach {
                        val user = it!!.toUser()
                        when (val resultDetail = it.login?.let { it1 -> getUserDetail(it1) }) {
                            is Result.Success -> {
                                user.detailUser = if (resultDetail.value.isEmpty()) null else resultDetail.value.first()
                            }
                        }
                        newList.add(user)
                    }
                    return Result.Success(newList.toList())
                } else {
                    return Result.Success(emptyList())
                }
            }
            is NetworkResult.Error -> result.createResultError()
        }
    }

    override suspend fun getUserDetail(userName: String): Result<List<DetailUser>> {
        return when (val result = remoteDataSource.getUserDetail(userName)) {
            is NetworkResult.Success -> {
                val resultValue = result.value
                return Result.Success(listOf(resultValue!!.toDetailUser()))
            }
            is NetworkResult.Error -> result.createResultError()
        }
    }
}
