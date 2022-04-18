package com.softic.esraa.elnajjar.data.repository

import android.content.Context
import com.softic.esraa.elnajjar.data.retrofit.CurrencyApiInterface
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val currencyApiInterface: CurrencyApiInterface,
    private val applicationContext: Context
) {


}