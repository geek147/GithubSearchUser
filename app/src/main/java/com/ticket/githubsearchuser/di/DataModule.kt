package com.ticket.githubsearchuser.di

import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ticket.githubsearchuser.BuildConfig
import com.ticket.githubsearchuser.data.GithubRepositoryImpl
import com.ticket.githubsearchuser.data.remote.GithubSearchService
import com.ticket.githubsearchuser.data.remote.GithubRemoteDataSource
import com.ticket.githubsearchuser.data.remote.GithubRemoteDataSourceImpl
import com.ticket.githubsearchuser.domain.repository.GithubRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseUrl

@Module
@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    fun provideUserApiService(retrofit: Retrofit): GithubSearchService = GithubSearchService(retrofit)

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    fun provideRetrofit(
        @BaseUrl baseUrl: String,
        moshi: Moshi,
        client: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(baseUrl)
            .build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply { level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE },

            )
            .build()
    }

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    fun provideGalleryRepository(repository: GithubRepositoryImpl):
            GithubRepository = repository

    @Provides
    fun provideGalleryRemoteDataSource(remote: GithubRemoteDataSourceImpl):
            GithubRemoteDataSource = remote
}