package com.vasu.appcenter.retrofit

import android.app.Activity
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vasu.appcenter.retrofit.model.ForceUpdateModel
import com.vasu.appcenter.utilities.getUpdateBaseUrl
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.concurrent.TimeUnit


class APIService {


    private lateinit var apiInterface: APIInterface

    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(50, TimeUnit.SECONDS)
        .writeTimeout(50, TimeUnit.SECONDS)
        .build()


    fun getUpdateClient(mContext: Activity): APIInterface {
        return getClient(mContext.getUpdateBaseUrl())
    }

    private fun getClient(baseUrl: String): APIInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(provideOkHttpClient(provideLoggingInterceptor()))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        apiInterface = retrofit.create(APIInterface::class.java)
        return apiInterface
    }

    private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        val b = OkHttpClient.Builder()
        b.addInterceptor(interceptor)
        return b.build()
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    interface APIInterface {

        // To check force update status
        @FormUrlEncoded
        @POST("ApkVersion")
        fun getUpdateStatusAsync(@Field("packageName") packageName: String, @Field("versionCode") versionCode: Double): Deferred<Response<ForceUpdateModel>>

    }
}
