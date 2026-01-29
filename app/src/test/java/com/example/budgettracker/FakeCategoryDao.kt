package com.example.budgettracker

import com.example.budgettracker.data.dao.CategoryDao
import com.example.budgettracker.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeCategoryDao : CategoryDao {

    private val categories = mutableListOf<CategoryEntity>()
    private val flowData = MutableStateFlow<List<CategoryEntity>>(emptyList())
    private var nextId = 1L

    override fun observeAllCategories(): Flow<List<CategoryEntity>> = flowData

    override suspend fun getAllCategories(): List<CategoryEntity> {
        return categories
            .filter { it.isActive }
            .sortedBy { it.name }
    }

    override suspend fun getCategoryById(id: Long): CategoryEntity? {
        return categories.find { it.id == id }
    }

    override suspend fun insertCategory(category: CategoryEntity): Long {
        val id = nextId++

        // 🔥 CRITICAL FIX: force active state to mirror Room behavior
        val activeCategory = category.copy(
            id = id,
            isActive = true
        )

        categories.add(activeCategory)
        updateFlow()
        return id
    }

    override suspend fun updateCategory(category: CategoryEntity) {
        val index = categories.indexOfFirst { it.id == category.id }
        if (index != -1) {
            categories[index] = category
            updateFlow()
        }
    }

    override suspend fun softDeleteCategory(id: Long) {
        val index = categories.indexOfFirst { it.id == id }
        if (index != -1) {
            categories[index] = categories[index].copy(isActive = false)
            updateFlow()
        }
    }

    override suspend fun getActiveCategoryCount(): Int {
        return categories.count { it.isActive }
    }

    fun clear() {
        categories.clear()
        nextId = 1L
        updateFlow()
    }

    private fun updateFlow() {
        flowData.value = categories
            .filter { it.isActive }
            .sortedBy { it.name }
    }
}
