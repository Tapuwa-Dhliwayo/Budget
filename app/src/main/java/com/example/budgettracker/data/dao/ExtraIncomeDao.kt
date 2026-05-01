package com.example.budgettracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.budgettracker.data.entity.ExtraIncomeEntity

@Dao
interface ExtraIncomeDao {
    @Query("SELECT * FROM extra_income WHERE dateReceived BETWEEN :startDate AND :endDate ORDER BY dateReceived DESC, id DESC")
    suspend fun getIncomeForPeriod(startDate: String, endDate: String): List<ExtraIncomeEntity>

    @Query("SELECT * FROM extra_income ORDER BY dateReceived DESC, id DESC LIMIT :limit")
    suspend fun getRecentIncome(limit: Int): List<ExtraIncomeEntity>

    @Query("SELECT * FROM extra_income WHERE id = :id LIMIT 1")
    suspend fun getIncomeById(id: Long): ExtraIncomeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: ExtraIncomeEntity): Long

    @Update
    suspend fun updateIncome(income: ExtraIncomeEntity)
}
