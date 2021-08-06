package com.ticket.githubsearchuser.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.ticket.githubsearchuser.R
import com.ticket.githubsearchuser.databinding.ErrorViewBinding

class ErrorStateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = ErrorViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun showError(
        title: String = resources.getString(R.string.error_state_title),
        message: String = resources.getString(R.string.error_state_message),
    ) {
        visibility = View.VISIBLE
        binding.textErrorTitle.text = title
        binding.textErrorMessage.text = message
    }
}
