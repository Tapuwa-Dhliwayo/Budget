package com.example.budgettracker

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.budgettracker.data.dao.MonthlyBudgetDao
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.entity.MonthlyBudgetEntity
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MonthlyBudgetDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: MonthlyBudgetDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()

        dao = db.monthlyBudgetDao()
    }

    @After
    fun teardown() {
        if (this::db.isInitialized) {
            db.close()
        }
    }


    @Test
    fun insertAndRetriveMonth() = runTest {
        val month = MonthlyBudgetEntity(
            monthId = "2026-01",
            startDate = "2026-01-01",
            startingFunds = 2000.0
        )

        dao.insertMonth(month)

        val loaded = dao.getMonth("2026-01")

        assertEquals(2000.0, loaded?.startingFunds)

    }
}