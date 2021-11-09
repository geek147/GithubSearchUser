package com.gg.githubsearchuser.ui.main

import com.gg.githubsearchuser.domain.entity.User

class MainContract {
    sealed class Intent {
        data class SearchUser(val query: String) : Intent()
        data class LoadNext(val page: Int) : Intent()
    }

    data class State(
        val viewState: ViewState = ViewState.Idle,
        val searchQuery: String = "",
        val listUser: List<User> = listOf()
    )

    sealed class ViewState {
        object Idle : ViewState()
        object Loading : ViewState()
        object SuccessNewSearch : ViewState()
        object SuccessLoadMore : ViewState()
        object Error : ViewState()
    }

    sealed class Effect {
        data class ShowToast(val message: String) : Effect()
    }
}
