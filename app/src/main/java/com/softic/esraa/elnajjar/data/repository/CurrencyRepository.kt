package com.softic.esraa.elnajjar.data.repository

import android.content.Context
import com.softic.esraa.elnajjar.data.retrofit.BaseDataSource
import com.softic.esraa.elnajjar.data.retrofit.CurrencyApiInterface
import com.softic.esraa.elnajjar.utils.Constants.Companion.ACCESS_KEY
import com.softic.esraa.elnajjar.utils.PreferencesUtility
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    context: Context,
    private val currencyApiInterface: CurrencyApiInterface): BaseDataSource(){

    private val accessKey = PreferencesUtility.getString(context, ACCESS_KEY,"")

    suspend fun fetchAllSymbolsApi() = getResult { currencyApiInterface.fetchAllSymbols(accessKey)}


    suspend fun getConvertedCurrencyResultForCurrentAmount( base:String, symbols: String) =
        getResult { currencyApiInterface.getValueForCurrenciesBasedOnOneEURAPILatest(
            accessKey, base, symbols, 1) }

}