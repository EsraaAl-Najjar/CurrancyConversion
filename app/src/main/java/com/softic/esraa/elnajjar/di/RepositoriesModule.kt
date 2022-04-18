package com.softic.esraa.elnajjar.di

import android.content.Context
import com.softic.esraa.elnajjar.data.repository.CurrencyRepository
import com.softic.esraa.elnajjar.data.retrofit.CurrencyApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoriesModule {

    @Singleton
    @Provides
    fun provideCurrencyRepository(
        currencyApiInterface: CurrencyApiInterface,
        @ApplicationContext applicationContext: Context
    ) =
        CurrencyRepository(currencyApiInterface, applicationContext)

}