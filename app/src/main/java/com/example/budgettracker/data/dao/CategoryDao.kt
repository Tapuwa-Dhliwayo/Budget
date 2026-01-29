package com.example.budgettracker.data.dao

import androidx.room.*
import com.example.budgettracker.data.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    
    @Query("SELECT * FROM categories WHERE isActive = 1 ORDER BY name ASC")
    fun observeAllCategories(): Flow<List<CategoryEntity>>
    
    @Query("SELECT * FROM categories WHERE isActive = 1 ORDER BY name ASC")
    suspend fun getAllCategories(): List<CategoryEntity>
    
    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: Long): CategoryEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity): Long
    
    @Update
    suspend fun updateCategory(category: CategoryEntity)
    
    @Query("UPDATE categories SET isActive = 0 WHERE id = :id")
    suspend fun softDeleteCategory(id: Long)
    
    @Query("SELECT COUNT(*) FROM categories WHERE isActive = 1")
    suspend fun getActiveCategoryCount(): Int
}
