package com.example.budgettracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.data.entity.MonthlyBudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyBudgetDao {
    @Query("SELECT * FROM monthly_budget WHERE monthId = :monthId")
    suspend fun getMonth(monthId: String): MonthlyBudgetEntity?

    @Query("SELECT * FROM monthly_budget ORDER BY monthId DESC")
    fun observeAllMonths(): Flow<List<MonthlyBudgetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonth(month: MonthlyBudgetEntity)
}