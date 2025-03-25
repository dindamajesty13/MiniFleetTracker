package com.majesty.minifleettracker.di

import com.majesty.minifleettracker.presentation.login.di.AuthRepository
import com.majesty.minifleettracker.presentation.main.di.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMainRepository() : MainRepository = MainRepository()

    @Provides
    @Singleton
    fun provideAuthRepository() : AuthRepository = AuthRepository()
}