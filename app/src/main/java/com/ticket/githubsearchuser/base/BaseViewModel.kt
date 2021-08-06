package com.ticket.githubsearchuser.base

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

abstract class BaseViewModel<Intent, State, Effect>(initialState: State) : ViewModel() {

    protected var viewState = initialState

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    private val _effect = MutableStateFlow<Event<Effect>?>(null)
    val effect: StateFlow<Event<Effect>?>
        get() = _effect

    abstract fun onIntentReceived(intent: Intent)

    protected fun setState(state: State.() -> State) {
        viewState = state.invoke(viewState)
        _state.value = state.invoke(viewState)
    }

    protected fun setEffect(effect: Event<Effect>) {
        _effect.value = effect
    }

    protected fun setEffect(effect: Effect) {
        _effect.value = Event(effect)
    }

    @VisibleForTesting
    fun mockState(state: State.() -> State) {
        setState(state)
    }
}
