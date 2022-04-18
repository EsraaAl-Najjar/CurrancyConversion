package com.softic.esraa.elnajjar.utils

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient

class Utility {
    companion object{

        fun getUnsafeOkHttpClient(context: Context): OkHttpClient {

            val builder = OkHttpClient.Builder()
            // Stetho For Debug Option With Api Failure
            builder.addNetworkInterceptor(StethoInterceptor())

            builder.hostnameVerifier { _, _ -> true }
            builder.addInterceptor { chain ->

                val newBuilder = chain.request().newBuilder()

                chain.proceed(newBuilder.build())
            }

            return builder.build()
        }
    }
}