package com.softic.esraa.elnajjar.data.retrofit

import android.util.Log
import com.softic.esraa.elnajjar.data.model.ObjectResponse
import com.softic.esraa.elnajjar.data.model.Resource
import okhttp3.ResponseBody
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun  getResult(call: suspend () -> Response<ObjectResponse>): Resource<ObjectResponse> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body, response.body()!!.error)
            }
            error(" ${response.code()} ${response.message()} ${response.errorBody()}", response.errorBody()!!)
        } catch (e: Exception) {
            error(e.message ?: e.toString())
        }
    }

    private fun  error(message: String, responseErrorBody: ResponseBody): Resource<ObjectResponse> {
        Log.e("remoteDataSource", message)
        return Resource.error(null, "Network call has failed for a following reason: $message " ,responseErrorBody)
    }
}