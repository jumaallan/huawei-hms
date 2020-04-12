package com.androidstudy.huaweihms.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.androidstudy.huaweihms.data.model.Map

@Dao
interface MapDao : BaseDao<Map> {

    @Query("SELECT * FROM Map")
    fun getLocationDescriptions(): LiveData<List<Map>>

    @Query("DELETE FROM Map")
    fun nukeTable()
}