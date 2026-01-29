package com.example.budgettracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.budgettracker.data.dao.MonthlyBudgetDao
import com.example.budgettracker.data.entity.MonthlyBudgetEntity

@Database(
    entities = [MonthlyBudgetEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun monthlyBudgetDao(): MonthlyBudgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_tracker.db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}