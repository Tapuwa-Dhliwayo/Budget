package com.example.budgettracker

import com.example.budgettracker.data.dao.UserProfileDao
import com.example.budgettracker.data.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserProfileDao : UserProfileDao {

    private var user: UserProfileEntity? = null
    private val flowData = MutableStateFlow<UserProfileEntity?>(null)

    override suspend fun getUser(): UserProfileEntity? = user

    override fun observeUser(): Flow<UserProfileEntity?> = flowData

    override suspend fun insertUser(user: UserProfileEntity) {
        this.user = user
        flowData.value = user
    }

    override suspend fun updateUser(user: UserProfileEntity) {
        this.user = user
        flowData.value = user
    }

    fun clear() {
        user = null
        flowData.value = null
    }
}
