package com.androidstudy.huaweihms.views.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.androidstudy.huaweihms.data.model.Map
import com.androidstudy.huaweihms.repository.MapRepository

class MapViewModel(
    private val mapRepository: MapRepository
) : ViewModel() {

    suspend fun getLocationDescriptions(): LiveData<List<Map>> {
        return mapRepository.getLocationDescriptions()
    }
}