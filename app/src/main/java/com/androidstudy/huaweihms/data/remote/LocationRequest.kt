package com.androidstudy.huaweihms.data.remote

data class LocationRequest(
    val location: LocationDataRequest,
    val language: String,
    val politicalView: String,
    val returnPoi: Boolean
)