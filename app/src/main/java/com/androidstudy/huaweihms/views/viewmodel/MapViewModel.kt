package com.androidstudy.huaweihms.views.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidstudy.huaweihms.data.remote.LocationRequest
import com.androidstudy.huaweihms.repository.MapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(
    private val mapRepository: MapRepository
) : ViewModel() {

    var locationDescriptions = mapRepository.getLocationDescriptions()

    fun getReverseGeoCode(locationRequest: LocationRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            mapRepository.getReverseGeoCode(locationRequest)
        }
    }
}