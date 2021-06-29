package com.vasu.appcenter.rateandfeedback.network

import android.content.Context
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vasu.appcenter.utilities.getReviewBaseUrl
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.*
import java.util.concurrent.TimeUnit


class FeedbackAPIService {


    private lateinit var apiInterface: APIInterface

    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .callTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()


    fun getFeedbackClient(mContext: Context): APIInterface {
        val retrofit = Retrofit.Builder()
            .baseUrl(mContext.getReviewBaseUrl())
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

        @Multipart
        @POST("app_review")
        fun getFeedbackAsync(
            @Part("package_name") package_name: RequestBody,
            @Part("review") review: RequestBody,
            @Part("ratings") ratings: RequestBody,
            @Part file: ArrayList<MultipartBody.Part>,
            @Part("contact_information") contact_information: RequestBody,
            @Part("version_code") version_code: RequestBody,
            @Part("version_name") version_name: RequestBody
        ): Deferred<Response<ModelResponseFeedback>>
    }
}
