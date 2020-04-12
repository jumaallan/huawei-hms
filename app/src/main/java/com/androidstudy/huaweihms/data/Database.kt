package com.androidstudy.huaweihms.data

import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androidstudy.huaweihms.data.dao.MapDao
import com.androidstudy.huaweihms.data.model.Map

@androidx.room.Database(
    entities = [
        Map::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class Database : RoomDatabase() {

    abstract fun mapDao(): MapDao
}
