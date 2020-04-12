package com.androidstudy.huaweihms.data.remote

data class Sites(
    val formatAddress: String,
    val address: Address,
    val viewport: Viewport,
    val name: String,
    val siteId: String,
    val location: Location
)