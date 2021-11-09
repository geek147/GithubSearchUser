package com.gg.githubsearchuser.ui.main

import com.gg.githubsearchuser.domain.entity.User

class MainContract {
    sealed class Intent {
        data class SearchUser(val query: String) : Intent()
        data class LoadNextSearchUser(val query: String, val page: Int) : Intent()
    }

    data class State(
        val viewState: ViewState = ViewState.Idle,
        val listUser: List<User> = listOf(),
        val showLoading: Boolean = false,
    )

    sealed class ViewState {
        object Idle : ViewState()
        object SuccessFirstInit : ViewState()
        object EmptyListFirstInit : ViewState()
        object EmptyListLoadMore : ViewState()
        object SuccessLoadMore : ViewState()
        object ErrorFirstInit : ViewState()
        object ErrorLoadMore : ViewState()
    }

    sealed class Effect {
        data class ShowToast(val message: String) : Effect()
    }
}
