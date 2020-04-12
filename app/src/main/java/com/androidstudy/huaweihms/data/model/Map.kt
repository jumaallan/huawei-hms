package com.androidstudy.huaweihms.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["lng", "lat"], unique = true)])
data class Map(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val formatAddress: String,
    val country: String,
    val countryCode: String,
    val locality: String,
    val adminArea: String,
    val subAdminArea: String,
    val siteId: String,
    val lng: Double,
    val longitude: Double,
    val lat: Double,
    val latitude: Double
)