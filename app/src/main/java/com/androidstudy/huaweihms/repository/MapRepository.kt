package com.androidstudy.huaweihms.repository

import androidx.lifecycle.LiveData
import com.androidstudy.huaweihms.data.dao.MapDao
import com.androidstudy.huaweihms.data.model.Map

class MapRepository(
    private val mapDao: MapDao
) {

    suspend fun saveMap(map: Map) {
        mapDao.insert(map)
    }

    suspend fun getLocationDescriptions(): LiveData<List<Map>> {
        return mapDao.getLocationDescriptions()
    }
}