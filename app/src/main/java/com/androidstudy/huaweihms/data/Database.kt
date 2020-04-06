package com.androidstudy.huaweihms.data

import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.androidstudy.huaweihms.data.dao.UserDao
import com.androidstudy.huaweihms.data.model.User

@androidx.room.Database(
    entities = [
        User::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class Database : RoomDatabase() {

    abstract fun userDao(): UserDao
}
