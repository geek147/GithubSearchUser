package com.ticket.githubsearchuser.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ticket.githubsearchuser.R
import com.ticket.githubsearchuser.databinding.ListUserBinding
import com.ticket.githubsearchuser.domain.entity.User

class MainAdapter(private val context: Context) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    private var listUser: MutableList<User> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding: ListUserBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.list_user, parent, false)
        return MainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (listUser.isNullOrEmpty()) {
            0
        } else {
            listUser.size
        }
    }

    override fun getItemId(position: Int): Long {
        val user: User = listUser[position]
        return user.id.toLong()
    }

    fun addData(list: List<User>) {
        this.listUser.addAll(list)
    }

    fun setList(list: List<User>) {
        this.listUser.clear()
        this.listUser.addAll(list)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        listUser[holder.bindingAdapterPosition].let {
            holder.bindData(it, context)
        }
    }

    class MainViewHolder(private val binding: ListUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(model: User, context: Context) {
            binding.user = model
        }
    }
}
