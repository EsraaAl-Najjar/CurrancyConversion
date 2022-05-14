package com.softic.esraa.elnajjar.data.retrofit

import com.softic.esraa.elnajjar.data.model.ObjectResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiInterface {

    @GET("symbols")
    suspend fun fetchAllSymbols(@Query("access_key") accessKey: String): Response<ObjectResponse>

    @GET("latest")
    suspend fun  getValueForCurrenciesBasedOnOneEURAPILatest(@Query("access_key") accessKey: String,
                                                             @Query("base") from:String,
                                                             @Query("symbols") to: String,
                                                             @Query("format") format: Int
                                                          ): Response<ObjectResponse>

}