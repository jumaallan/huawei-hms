package com.androidstudy.huaweihms.repository

import androidx.lifecycle.LiveData
import com.androidstudy.huaweihms.BuildConfig
import com.androidstudy.huaweihms.data.dao.MapDao
import com.androidstudy.huaweihms.data.model.Map
import com.androidstudy.huaweihms.data.network.MapAPI
import com.androidstudy.huaweihms.data.remote.LocationRequest
import timber.log.Timber

class MapRepository(
    private val mapDao: MapDao,
    private val mapAPI: MapAPI
) {

    suspend fun getReverseGeoCode(locationRequest: LocationRequest) {
        try {
            val response =
                mapAPI.getReverseGeoCode(
                    BuildConfig.API_KEY,
                    locationRequest
                )

            if (response.isSuccessful) {
                response.body()?.sites?.forEach {
                    saveMap(
                        Map(
                            0,
                            it.formatAddress,
                            it.address.country,
                            it.address.countryCode,
                            it.address.locality,
                            it.address.adminArea,
                            it.address.subAdminArea,
                            it.siteId,
                            locationRequest.location.lng,
                            it.location.lng,
                            locationRequest.location.lat,
                            it.location.lat
                        )
                    )
                }
            } else {
                Timber.d("Failed to get reverse geo coding data")
            }
        } catch (t: Throwable) {
            Timber.d(t.localizedMessage)
        }
    }

    private suspend fun saveMap(map: Map) {
        mapDao.insert(map)
    }

    suspend fun getLocationDescriptions(): LiveData<List<Map>> {
        return mapDao.getLocationDescriptions()
    }
}