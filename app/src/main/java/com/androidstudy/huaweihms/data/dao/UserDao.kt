package com.androidstudy.huaweihms.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.androidstudy.huaweihms.data.model.User

@Dao
interface UserDao : BaseDao<User> {

    @Query("SELECT * FROM User WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User
}