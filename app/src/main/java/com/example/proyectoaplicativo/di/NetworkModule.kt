package com.example.proyectoaplicativo.di

import com.example.proyectoaplicativo.data.remote.api.TicketApiService
import com.example.proyectoaplicativo.data.repository.TicketRepositoryImpl
import com.example.proyectoaplicativo.domain.repository.TicketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://bjo2tvjmu2.execute-api.us-east-1.amazonaws.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor (HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTicketApiService(retrofit: Retrofit): TicketApiService{
        return retrofit.create(TicketApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTicketRepository(
        ticketApi: TicketApiService
    ): TicketRepository{
        return TicketRepositoryImpl(ticketApi)
    }
}