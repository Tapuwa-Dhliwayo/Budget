package com.example.budgettracker.data.repository

import com.example.budgettracker.data.dao.UserProfileDao
import com.example.budgettracker.data.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class UserProfileRepository(
    private val dao: UserProfileDao
) {
    suspend fun getUser(): UserProfileEntity? {
        return dao.getUser()
    }

    fun observeUser(): Flow<UserProfileEntity?> {
        return dao.observeUser()
    }

    suspend fun createDefaultUser(): UserProfileEntity {
        val defaultUser = UserProfileEntity(
            id = 1,
            firstName = "User",
            lastName = "",
            createdDate = LocalDate.now().toString()
        )
        dao.insertUser(defaultUser)
        return defaultUser
    }

    suspend fun updateUser(firstName: String, lastName: String) {
        val user = getUser() ?: createDefaultUser()
        val updatedUser = user.copy(
            firstName = firstName,
            lastName = lastName
        )
        dao.updateUser(updatedUser)
    }

    suspend fun getOrCreateUser(): UserProfileEntity {
        return getUser() ?: createDefaultUser()
    }
}
