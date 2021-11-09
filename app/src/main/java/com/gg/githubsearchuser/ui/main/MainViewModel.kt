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
            is MainContract.Intent.LoadNext -> {
                loadMoreUser(intent.page)
            }
            is MainContract.Intent.SearchUser -> {
                getUserSearch(intent.query)
            }
        }
    }

    private fun getUserSearch(query: String) = viewModelScope.launch {
        setState { copy(viewState = MainContract.ViewState.Loading) }
        val params = SearchUser.Params(query = query, page = 1)

        when (
            val result = searchUser(params)
        ) {
            is Result.Success -> {
                setState {
                    copy(
                        viewState = MainContract.ViewState.SuccessNewSearch,
                        listUser = result.value,
                        searchQuery = query
                    )
                }
            }
            is Result.Error -> {
                setEffect(MainContract.Effect.ShowToast(result.errorMessage))
                setState {
                    copy(
                        viewState = MainContract.ViewState.Error,
                        listUser = emptyList()
                    )
                }
            }
        }
    }

    private fun loadMoreUser(page: Int) = viewModelScope.launch {
        val query = state.value?.searchQuery.orEmpty()
        val params = SearchUser.Params(query = query, page = page)

        when (
            val result = searchUser(params)
        ) {
            is Result.Success -> {
                setState {
                    copy(
                        viewState = MainContract.ViewState.SuccessLoadMore,
                        listUser = result.value
                    )
                }
            }
            is Result.Error -> {
                setEffect(MainContract.Effect.ShowToast(result.errorMessage))
                setState { copy(viewState = MainContract.ViewState.Idle) }
            }
        }
    }
}
