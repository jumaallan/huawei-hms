package com.androidstudy.huaweihms.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Update

interface BaseDao<T> {

    @Insert(onConflict = REPLACE)
    suspend fun insert(item: T)

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg items: T)

    @Insert(onConflict = REPLACE)
    suspend fun insert(items: List<T>)

    @Update(onConflict = REPLACE)
    suspend fun update(item: T)

    @Delete
    suspend fun delete(item: T)
}
