package com.example.budgettracker

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.budgettracker.data.dao.CategoryDao
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.entity.CategoryEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: CategoryDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.categoryDao()
    }

    @After
    fun teardown() {
        if (this::db.isInitialized) {
            db.close()
        }
    }

    @Test
    fun insertAndRetrieveCategory() = runTest {
        val category = CategoryEntity(
            name = "Groceries",
            color = 0xFF4CAF50.toInt(),
            icon = "🛒",
            budgetLimit = 600.0
        )

        val id = dao.insertCategory(category)
        val loaded = dao.getCategoryById(id)

        assertNotNull(loaded)
        assertEquals("Groceries", loaded?.name)
        assertEquals(600.0, loaded?.budgetLimit  ?: 0.0, 0.01)
    }

    @Test
    fun getAllCategories() = runTest {
        val categories = listOf(
            CategoryEntity(name = "Groceries", color = 0xFF4CAF50.toInt(), icon = "🛒", budgetLimit = 600.0),
            CategoryEntity(name = "Transport", color = 0xFF2196F3.toInt(), icon = "🚗", budgetLimit = 400.0)
        )

        categories.forEach { dao.insertCategory(it) }

        val loaded = dao.getAllCategories()
        assertEquals(2, loaded.size)
    }

    @Test
    fun softDeleteCategory() = runTest {
        val category = CategoryEntity(
            name = "Groceries",
            color = 0xFF4CAF50.toInt(),
            icon = "🛒",
            budgetLimit = 600.0
        )

        val id = dao.insertCategory(category)
        dao.softDeleteCategory(id)

        val activeCount = dao.getActiveCategoryCount()
        assertEquals(0, activeCount)
    }
}