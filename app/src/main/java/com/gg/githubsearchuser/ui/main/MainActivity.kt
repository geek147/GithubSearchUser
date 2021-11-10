package com.gg.githubsearchuser.ui.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gg.githubsearchuser.R
import com.gg.githubsearchuser.base.BaseActivity
import com.gg.githubsearchuser.databinding.ActivityMainBinding
import com.gg.githubsearchuser.utils.EndlessRecyclerViewScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity :
    BaseActivity<MainContract.Intent,
        MainContract.State,
        MainContract.Effect,
        MainViewModel>() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MainAdapter

    override val layoutResourceId: Int = R.layout.activity_main
    private var currentPage = 1

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupSearchText()
        setupRecylerView()
    }

    override fun invalidate(state: MainContract.State) {
        when (state.viewState) {
            MainContract.ViewState.EmptyListFirstInit -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView(
                            title = resources.getString(R.string.empty_state_title),
                            message = resources.getString(R.string.empty_state_message)
                        )
                        binding.buttonRetry.setOnClickListener {
                            currentPage = 1
                            viewModel.onIntentReceived(MainContract.Intent.SearchUser(editTextQuery.text.toString()))
                        }
                    }
                    adapter.setList(emptyList())
                    recyclerView.visibility = View.GONE
                    swipeContainer.isRefreshing = false
                }
            }
            MainContract.ViewState.ErrorFirstInit -> {
                with(binding) {
                    errorView.visibility = View.VISIBLE
                    errorView.run {
                        setUpErrorView()
                        binding.buttonRetry.setOnClickListener {
                            currentPage = 1
                            viewModel.onIntentReceived(MainContract.Intent.SearchUser(editTextQuery.text.toString()))
                        }
                    }
                    adapter.setList(emptyList())
                    recyclerView.visibility = View.GONE
                    swipeContainer.isRefreshing = false
                }
            }
            MainContract.ViewState.ErrorLoadMore -> {
                with(binding) {
                    recyclerView.visibility = View.VISIBLE
                    swipeContainer.isRefreshing = false
                }
            }
            MainContract.ViewState.Idle -> {}
            MainContract.ViewState.SuccessFirstInit -> {
                with(binding) {
                    recyclerView.visibility = View.VISIBLE
                    adapter.setList(state.listUser)
                    errorView.visibility = View.GONE
                    hideSoftKeyboard(editTextQuery)
                    swipeContainer.isRefreshing = false
                }
            }
            MainContract.ViewState.SuccessLoadMore -> {
                with(binding) {
                    recyclerView.visibility = View.VISIBLE
                    adapter.addData(state.listUser)
                    errorView.visibility = View.GONE
                    swipeContainer.isRefreshing = false
                }
            }
            MainContract.ViewState.EmptyListLoadMore -> {
                Toast.makeText(this@MainActivity, "new list is empty", Toast.LENGTH_SHORT).show()
                binding.swipeContainer.isRefreshing = false
            }
        }
    }

    override fun renderEffect(effect: MainContract.Effect) {
        when (effect) {
            is MainContract.Effect.ShowToast -> {
                Toast.makeText(this@MainActivity, effect.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupRecylerView() {
        binding.recyclerView.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.itemAnimator = null
        adapter = MainAdapter(this@MainActivity)
        adapter.setHasStableIds(true)
        binding.recyclerView.adapter = adapter
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(
                page: Int,
                totalItemsCount: Int,
                view: RecyclerView?,
            ) {
                currentPage = page + 1
                dispatch(MainContract.Intent.LoadNextSearchUser(editTextQuery.text.toString(), currentPage))
            }
        }
        binding.recyclerView.addOnScrollListener(scrollListener)
        binding.swipeContainer.setOnRefreshListener { // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            currentPage += 1
            dispatch(MainContract.Intent.LoadNextSearchUser(editTextQuery.text.toString(), currentPage))
        }
        // Configure the refreshing colors
        // Configure the refreshing colors
        binding.swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    private fun setupSearchText() {
        binding.editTextQuery.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    adapter.setList(emptyList())
                    recyclerView.visibility = View.GONE
                } else {
                    dispatch(MainContract.Intent.SearchUser(s.toString()))
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun hideSoftKeyboard(view: View) {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun provideViewModel(): Class<MainViewModel> = MainViewModel::class.java
}
