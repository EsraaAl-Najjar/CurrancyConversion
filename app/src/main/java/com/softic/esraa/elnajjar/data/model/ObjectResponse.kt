package com.softic.esraa.elnajjar.data.model

import com.google.gson.annotations.SerializedName

data class ObjectResponse(
    @SerializedName("success") var success: Boolean? = null,
    @SerializedName("symbols") var symbols: Map<String, String>? = null,
    @SerializedName("error") var error: Error? = Error(),

    @SerializedName("query") var query: Query? = Query(),
    @SerializedName("info") var info: Info? = Info(),
    @SerializedName("historical") var historical: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("result") var result: Double? = null,
    @SerializedName("rates") var rates: Map<String, Double>? = null)


data class Error(
    @SerializedName("code") var code: Int? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("info") var info: String? = null
)


