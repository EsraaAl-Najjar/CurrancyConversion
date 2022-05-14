package com.softic.esraa.elnajjar.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.softic.esraa.elnajjar.R
import com.softic.esraa.elnajjar.data.model.ObjectResponse
import com.softic.esraa.elnajjar.data.model.Resource
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import java.math.*
import kotlin.math.*
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class Utility {
    companion object{

        fun roundOfBigDecimal(result: BigDecimal) : Double{
           return (result).setScale(4, RoundingMode.CEILING).toDouble();
        }
        fun showShortToast(context: Context ,message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        }

        fun getUnsafeOkHttpClient(): OkHttpClient {

            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            // Stetho For Debug Option With Api Failure
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)

            builder.addNetworkInterceptor(StethoInterceptor())
            builder.hostnameVerifier { _, _ -> true }
            builder.addInterceptor { chain ->
                val newBuilder = chain.request().newBuilder()

                chain.proceed(newBuilder.build())
            }

            return builder.build()
        }

        fun  performGetOperation( networkCall: suspend () -> Resource<ObjectResponse> ): LiveData<Resource<ObjectResponse>> =
            liveData(Dispatchers.IO) {
                emit(Resource.loading())

                val responseStatus = networkCall.invoke()
                when (responseStatus.status) {
                    Resource.Status.SUCCESS -> {
                        emit(Resource.success(responseStatus.data!!,responseStatus.error))
                    }
                    Resource.Status.ERROR -> {
                        emit(Resource.error(responseStatus.data, responseStatus.error!!.info,responseStatus.responseErrorBody!! ))
                    }
                    Resource.Status.LOADING -> {
                        emit(Resource.loading())
                    }
                }

            }

        fun getProgressDialog(context: Context): Dialog {

            val mDialog = Dialog(context)
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mDialog.setCancelable(false)
            mDialog.setContentView(R.layout.layout_dialog_loading_indicator)

            mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mDialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

            return mDialog
        }


    }
}