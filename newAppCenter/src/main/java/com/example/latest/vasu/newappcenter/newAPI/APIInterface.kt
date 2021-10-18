package com.example.latest.vasu.newappcenter.newAPI

import com.example.latest.vasu.newappcenter.model.MoreAppMainModel
import retrofit2.http.GET
import retrofit2.http.Path

interface APIInterface {

    @GET("{packageName}")
    suspend fun getMoreAppResponse(@Path("packageName") packageName: String): MoreAppMainModel
}