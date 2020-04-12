package com.androidstudy.huaweihms.data.remote

data class LocationResponse(
    val returnCode: Int,
    val sites: List<Sites>,
    val returnDesc: String
)