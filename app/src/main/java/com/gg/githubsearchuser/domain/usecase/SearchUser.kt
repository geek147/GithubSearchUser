package com.gg.githubsearchuser.domain.usecase

import com.gg.githubsearchuser.base.BaseCaseWrapper
import com.gg.githubsearchuser.base.Result
import com.gg.githubsearchuser.domain.entity.User
import com.gg.githubsearchuser.domain.repository.GithubRepository
import com.gg.githubsearchuser.utils.Constants
import javax.inject.Inject

class SearchUser @Inject constructor(
    private val githubRepository: GithubRepository
) : BaseCaseWrapper<List<User>, SearchUser.Params>() {
    override suspend fun build(params: Params?): Result<List<User>> {
        requireNotNull(params)

        return githubRepository.searchUser(params.query, params.page, Constants.PER_PAGE)
    }

    class Params(val query: String, val page: Int)
}
