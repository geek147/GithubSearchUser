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
            MainContract.ViewState.Error -> {
                binding.errorView.visibility = View.VISIBLE
                binding.errorView.run {
                    showError()
                }
                binding.pgProgressList.visibility = View.GONE
                adapter.setList(state.listUser)
                binding.recyclerView.visibility = View.GONE
            }
            MainContract.ViewState.Loading -> {
                binding.errorView.visibility = View.GONE
                binding.pgProgressList.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.VISIBLE
            }
            MainContract.ViewState.SuccessNewSearch -> {
                binding.errorView.visibility = View.GONE
                binding.pgProgressList.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.setList(state.listUser)
                hideSoftKeyboard(binding.editTextQuery)
            }
            MainContract.ViewState.SuccessLoadMore -> {
                binding.errorView.visibility = View.GONE
                binding.pgProgressList.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.addData(state.listUser)
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
                view: RecyclerView?
            ) {
                currentPage = page + 1
                dispatch(MainContract.Intent.LoadNext(currentPage))
            }
        }
        binding.recyclerView.addOnScrollListener(scrollListener)
    }

    private fun setupSearchText() {
        binding.editTextQuery.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    adapter.setList(emptyList())
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
