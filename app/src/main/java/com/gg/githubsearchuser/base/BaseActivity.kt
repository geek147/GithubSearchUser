package com.gg.githubsearchuser.base

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseActivity<Intent, State, Effect, VM : BaseViewModel<Intent, State, Effect>> :
    AppCompatActivity() {

    lateinit var viewModel: VM

    abstract val layoutResourceId: Int

    abstract fun provideViewModel(): Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(provideViewModel())

        viewModel.state.observe(this) {
            invalidate(it)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.effect.collect { effect ->
                    effect?.getContentIfNotHandled()?.let {
                        renderEffect(it)
                    }
                }
            }
        }
    }

    abstract fun invalidate(state: State)

    abstract fun renderEffect(effect: Effect)

    protected fun dispatch(intent: Intent) {
        viewModel.onIntentReceived(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
