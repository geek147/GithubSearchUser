package com.ticket.githubsearchuser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ticket.githubsearchuser.base.Result
import com.ticket.githubsearchuser.domain.entity.User
import com.ticket.githubsearchuser.domain.usecase.SearchUser
import com.ticket.githubsearchuser.ui.main.MainContract
import com.ticket.githubsearchuser.ui.main.MainViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testScope = TestCoroutineScope()

    private var searchUser = mockk<SearchUser>()

    private val observedStateList = mutableListOf<MainContract.State>()
    private val observedEffectList = mutableListOf<MainContract.Effect>()
    private val observerState = mockk<Observer<MainContract.State>>()
    private val slotState = slot<MainContract.State>()

    private val viewModel = MainViewModel(searchUser)

    private val testDispatcher = TestCoroutineDispatcher()

    private val resultUserList by lazy {
        Result.Success(
            listOf(
                User(
                    id = 123,
                    username = "ilham",
                    avatarUrl = ""
                )
            )
        )
    }

    private val resultLoadMore by lazy {
        Result.Success(
            listOf(
                User(
                    id = 321,
                    username = "halim",
                    avatarUrl = ""
                )
            )
        )
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        viewModel.state.observeForever(observerState)

        every {
            observerState.onChanged(capture(slotState))
        } answers {
            observedStateList.add(slotState.captured)
        }

        testScope.launch {
            viewModel.effect.collect { event ->
                event?.peekContent()?.let {
                    observedEffectList.add(it)
                }
            }
        }
    }

    @After
    fun tearDown() {
        observedStateList.clear()
        observedEffectList.clear()

        viewModel.state.removeObserver(observerState)
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `onSearchUser should set display state to loading`() {

        coEvery {
            searchUser(any())
        } returns resultUserList

        viewModel.onIntentReceived(
            MainContract.Intent.SearchUser("i")
        )

        assertEquals(observedStateList.first().viewState, MainContract.ViewState.Loading)
    }

    @Test
    fun `onSearchUser success should set display state to success`() {
        val userName = "ilham"
        val id = 123

        coEvery {
            searchUser(any())
        } returns resultUserList

        viewModel.onIntentReceived(
            MainContract.Intent.SearchUser("i")
        )

        assertEquals(observedStateList.last().viewState, MainContract.ViewState.SuccessNewSearch)
        assertEquals(observedStateList.last().listUser[0].username, userName)
        assertEquals(observedStateList.last().listUser[0].id, id)
    }

    @Test
    fun `onSearchUser error should show error page`() {
        val errorMessage = "Terjadi kesalahan"
        coEvery {
            searchUser(any())
        } returns Result.Error(errorMessage, 0)

        viewModel.onIntentReceived(
            MainContract.Intent.SearchUser("i")
        )

        assertEquals(observedStateList.last().viewState, MainContract.ViewState.Error)
        assertEquals(observedEffectList.last(), MainContract.Effect.ShowToast(errorMessage))
    }

    @Test
    fun `onLoadMore success should set display state to success and Show new`() {
        val userName = "halim"
        val id = 321

        coEvery {
            searchUser(any())
        } returns resultLoadMore

        viewModel.onIntentReceived(
            MainContract.Intent.LoadNext(2)
        )

        assertEquals(observedStateList.last().viewState, MainContract.ViewState.SuccessLoadMore)
        assertEquals(observedStateList.last().listUser[0].username, userName)
        assertEquals(observedStateList.last().listUser[0].id, id)
    }

    @Test
    fun `onLoadMore error should show error toast`() {
        val errorMessage = "Terjadi kesalahan"
        coEvery {
            searchUser(any())
        } returns Result.Error(errorMessage, 0)

        viewModel.onIntentReceived(
            MainContract.Intent.LoadNext(2)
        )

        assertEquals(observedStateList.last().viewState, MainContract.ViewState.Idle)
        assertEquals(observedEffectList.last(), MainContract.Effect.ShowToast(errorMessage))
    }
}
