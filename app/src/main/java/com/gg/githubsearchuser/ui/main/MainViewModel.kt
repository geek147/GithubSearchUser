package com.gg.githubsearchuser.ui.main

import androidx.lifecycle.viewModelScope
import com.gg.githubsearchuser.base.BaseViewModel
import com.gg.githubsearchuser.base.Result
import com.gg.githubsearchuser.domain.usecase.SearchUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchUser: SearchUser
) : BaseViewModel<MainContract.Intent, MainContract.State, MainContract.Effect>(MainContract.State()) {

    override fun onIntentReceived(intent: MainContract.Intent) {
        when (intent) {
            is MainContract.Intent.LoadNextSearchUser -> {
                getUserSearch(intent.query, intent.page, true)
            }
            is MainContract.Intent.SearchUser -> {
                getUserSearch(intent.query, 1, false)
            }
        }
    }

    private fun getUserSearch(query: String, page: Int, isLoadMore: Boolean) {
        setState {
            copy(
                showLoading = true
            )
        }

        val params = SearchUser.Params(query = query, page = page)

        viewModelScope.launch {
            when (
                val result = searchUser(params)
            ) {
                is Result.Success -> {
                    if (result.value.isEmpty()) {
                        if (isLoadMore) {
                            setState {
                                copy(
                                    viewState = MainContract.ViewState.EmptyListLoadMore,
                                    listUser = emptyList(),
                                    showLoading = false
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    viewState = MainContract.ViewState.EmptyListFirstInit,
                                    listUser = emptyList(),
                                    showLoading = false
                                )
                            }
                        }
                    } else {
                        if (isLoadMore) {
                            setState {
                                copy(
                                    viewState = MainContract.ViewState.SuccessLoadMore,
                                    listUser = result.value,
                                    showLoading = false
                                )
                            }
                        } else {
                            setState {
                                copy(
                                    viewState = MainContract.ViewState.SuccessFirstInit,
                                    listUser = result.value,
                                    showLoading = false
                                )
                            }
                        }
                    }
                }
                is Result.Error -> {
                    if (isLoadMore) {
                        setState {
                            copy(
                                viewState = MainContract.ViewState.ErrorLoadMore,
                                listUser = emptyList(),
                                showLoading = false,
                            )
                        }
                    } else {
                        setState {
                            copy(
                                viewState = MainContract.ViewState.ErrorFirstInit,
                                listUser = emptyList(),
                                showLoading = false,
                            )
                        }
                    }
                }
            }
        }
    }
}
