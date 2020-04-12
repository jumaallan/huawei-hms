package com.androidstudy.huaweihms.data.network

import com.androidstudy.huaweihms.data.remote.LocationRequest
import com.androidstudy.huaweihms.data.remote.LocationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface MapAPI {

    @POST("mapApi/v1/siteService/reverseGeocode")
    suspend fun getReverseGeoCode(
        @Query("key") apiKey: String,
        @Body locationRequest: LocationRequest
    ): Response<LocationResponse?>
}