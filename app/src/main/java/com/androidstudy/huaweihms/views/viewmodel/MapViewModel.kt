package com.androidstudy.huaweihms.views.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.androidstudy.huaweihms.data.model.Map
import com.androidstudy.huaweihms.data.remote.LocationRequest
import com.androidstudy.huaweihms.repository.MapRepository
import timber.log.Timber

class MapViewModel(
    private val mapRepository: MapRepository
) : ViewModel() {

    suspend fun getReverseGeoCode(locationRequest: LocationRequest) {
        Timber.d("heeerreeee 1")
        mapRepository.getReverseGeoCode(locationRequest)
    }

    fun getLocationDescriptions(): LiveData<List<Map>> {
        return mapRepository.getLocationDescriptions()
    }
}