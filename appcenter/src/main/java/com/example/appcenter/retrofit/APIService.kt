package com.example.appcenter.retrofit

import android.content.Context
import com.example.appcenter.retrofit.model.ModelAppCenter
import com.example.appcenter.utilities.getBaseUrlApps
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit


class APIService {


    private lateinit var apiInterface: APIInterface

    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(50, TimeUnit.SECONDS)
        .writeTimeout(50, TimeUnit.SECONDS)
        .build()

    var okHttpClientForMoreAppsView: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(8, TimeUnit.SECONDS)
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(50, TimeUnit.SECONDS)
        .writeTimeout(50, TimeUnit.SECONDS)
        .build()

    fun getClient(mContext: Context): APIInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(mContext.getBaseUrlApps())
            .client(provideOkHttpClient(provideLoggingInterceptor()))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        apiInterface = retrofit.create(APIInterface::class.java)

        return apiInterface
    }

    fun getClientForMoreAppsView(mContext: Context): APIInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(mContext.getBaseUrlApps())
            .client(provideOkHttpClient(provideLoggingInterceptor()))
            .client(okHttpClientForMoreAppsView)
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

        // This method return list of invitation categories
        @GET("{packageName}")
        fun getAppCenterAsync(@Path("packageName") packageName: String): Deferred<Response<ModelAppCenter>>


    }
}
