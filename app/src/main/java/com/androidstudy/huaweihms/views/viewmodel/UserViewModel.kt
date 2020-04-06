package com.androidstudy.huaweihms.views.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidstudy.huaweihms.data.model.User
import com.androidstudy.huaweihms.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    suspend fun getUserById(userId: Int): User {
        return userRepository.getUserById(userId)
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            userRepository.saveUser(user)
        }
    }
}