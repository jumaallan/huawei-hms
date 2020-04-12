package com.androidstudy.huaweihms.data.network

import com.androidstudy.huaweihms.data.remote.LocationRequest
import com.androidstudy.huaweihms.data.remote.LocationResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface MapAPI {

    @POST("mapApi/v1/siteService/reverseGeocode?key={api_key}")
    suspend fun loginUser(
        @Path("api_key") apiKey: String,
        @Body locationRequest: LocationRequest
    ): LocationResponse?
}