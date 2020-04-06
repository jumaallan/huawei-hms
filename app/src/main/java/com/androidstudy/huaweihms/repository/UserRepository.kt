package com.androidstudy.huaweihms.repository

import com.androidstudy.huaweihms.data.dao.UserDao
import com.androidstudy.huaweihms.data.model.User

class UserRepository(
    private val userDao: UserDao
) {

    suspend fun saveUser(user: User) {
        userDao.insert(user)
    }

    suspend fun getUserById(userId: Int): User {
        return userDao.getUserById(userId)
    }
}