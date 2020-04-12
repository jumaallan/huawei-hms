package com.androidstudy.huaweihms.views.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidstudy.huaweihms.data.model.Map
import com.androidstudy.huaweihms.repository.MapRepository
import kotlinx.coroutines.launch

class MapViewModel(
    private val mapRepository: MapRepository
) : ViewModel() {

    suspend fun getLocationDescriptions(): LiveData<List<Map>> {
        return mapRepository.getLocationDescriptions()
    }

    fun saveMap(map: Map) {
        viewModelScope.launch {
            mapRepository.saveMap(map)
        }
    }
}