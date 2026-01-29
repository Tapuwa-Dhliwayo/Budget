package com.example.budgettracker.data.repository

import com.example.budgettracker.data.dao.CategoryDao
import com.example.budgettracker.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val dao: CategoryDao) {
    
    fun observeAllCategories(): Flow<List<CategoryEntity>> {
        return dao.observeAllCategories()
    }
    
    suspend fun getAllCategories(): List<CategoryEntity> {
        return dao.getAllCategories()
    }
    
    suspend fun getCategoryById(id: Long): CategoryEntity? {
        return dao.getCategoryById(id)
    }
    
    suspend fun insertCategory(category: CategoryEntity): Long {
        return dao.insertCategory(category)
    }
    
    suspend fun updateCategory(category: CategoryEntity) {
        dao.updateCategory(category)
    }
    
    suspend fun deleteCategory(id: Long) {
        dao.softDeleteCategory(id)
    }
    
    suspend fun hasCategories(): Boolean {
        return dao.getActiveCategoryCount() > 0
    }
}
