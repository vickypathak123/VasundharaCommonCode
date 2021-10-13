package com.vasu.appcenter.akshay.api

import android.content.Context
import android.util.Base64
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vasu.appcenter.R
import com.vasu.appcenter.akshay.adshelper.getStringRes
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


object APIBuilder {

    val Context.getUpdateBaseUrl: String get() = this.getStringRes(R.string.base_url_update).getBaseUrl
    val Context.getAuthorizationToken: String get() = this.getStringRes(R.string.AUTHORIZATION_URL).getBaseUrl

    val Context.getUpdateClient: APIInterface get() = getUpdateClientBuilder.create(APIInterface::class.java)

    private val okHttpClient: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                    .callTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .build()
        }

    private val Interceptor.provideOkHttpClient: OkHttpClient
        get() {
            val b = OkHttpClient.Builder()
            b.addInterceptor(this@provideOkHttpClient)
            return b.build()
        }

    private val provideLoggingInterceptor: HttpLoggingInterceptor
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return interceptor
        }

    private val Context.headerAuthorizationInterceptor: Interceptor
        get() = Interceptor { chain ->
            var request: Request = chain.request()
//            For latest logging-interceptor lib
            val headers: Headers = request.headers.newBuilder().add("Authorization", this@headerAuthorizationInterceptor.getAuthorizationToken).build()
            request = request.newBuilder().headers(headers).build()

            chain.proceed(request)
        }

    private val Context.getUpdateClientBuilder: Retrofit
        get() {
            return Retrofit.Builder()
                    .baseUrl(this.getUpdateBaseUrl)
                    .client(provideLoggingInterceptor.provideOkHttpClient)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .build() //Doesn't require the adapter
        }

    private val String.getBaseUrl: String
        get() {
            val dataDec: ByteArray = Base64.decode(this, Base64.DEFAULT)
            var decodedString = ""
            try {
                decodedString = String(dataDec, Charset.forName("UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            } finally {
                return decodedString
            }
        }

}