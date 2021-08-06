package com.ticket.githubsearchuser.di

import com.ticket.githubsearchuser.domain.dispatchers.CoroutineDispatchers
import com.ticket.githubsearchuser.domain.dispatchers.CoroutineDispatchersImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    @Binds
    abstract fun bindCoroutineDispatchers(coroutineDispatchersImpl: CoroutineDispatchersImpl): CoroutineDispatchers
}