package com.softic.esraa.elnajjar.di

import com.google.gson.GsonBuilder
import com.softic.esraa.elnajjar.BuildConfig
import com.softic.esraa.elnajjar.data.retrofit.CurrencyApiInterface
import com.softic.esraa.elnajjar.utils.Utility
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(Utility.getUnsafeOkHttpClient())
            .build()

    @Provides
    fun provideCurrencyApiInterface(retrofit: Retrofit): CurrencyApiInterface =
        retrofit.create(CurrencyApiInterface::class.java)


}