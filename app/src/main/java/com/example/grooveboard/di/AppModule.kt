package com.example.grooveboard.di

import com.example.grooveboard.data.repository.AuthRepositoryImpl
import com.example.grooveboard.data.repository.TicketRepositoryImpl
import com.example.grooveboard.domain.repository.AuthRepository
import com.example.grooveboard.domain.repository.TicketRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTicketRepository(
        ticketRepositoryImpl: TicketRepositoryImpl
    ): TicketRepository
}
