package com.example.budgettracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.budgettracker.data.dao.*
import com.example.budgettracker.data.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        MonthlyBudgetEntity::class,
        CategoryEntity::class,
        ExpenseEntity::class,
        GamificationEntity::class,
        UserProfileEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun monthlyBudgetDao(): MonthlyBudgetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun gamificationDao(): GamificationDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create user_profile table
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS user_profile (
                        id INTEGER PRIMARY KEY NOT NULL,
                        firstName TEXT NOT NULL,
                        lastName TEXT NOT NULL,
                        createdDate TEXT NOT NULL
                    )
                    """.trimIndent()
                )

                // Insert default user
                db.execSQL(
                    """
                    INSERT INTO user_profile (id, firstName, lastName, createdDate) 
                    VALUES (1, 'User', '', date('now'))
                    """.trimIndent()
                )
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_tracker.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Prepopulate with default categories
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    prepopulateDatabase(database)
                                }
                            }
                        }
                    })
                    .build()

                INSTANCE = instance
                instance
            }
        }

        private suspend fun prepopulateDatabase(database: AppDatabase) {
            val categoryDao = database.categoryDao()

            // Default categories with colors (using Android color resources)
            val defaultCategories = listOf(
                CategoryEntity(name = "Groceries", color = 0xFF4CAF50.toInt(), icon = "🛒", budgetLimit = 600.0),
                CategoryEntity(name = "Entertainment", color = 0xFFE91E63.toInt(), icon = "🎬", budgetLimit = 300.0),
                CategoryEntity(name = "Transport", color = 0xFF2196F3.toInt(), icon = "🚗", budgetLimit = 400.0),
                CategoryEntity(name = "Bills", color = 0xFFFF9800.toInt(), icon = "📄", budgetLimit = 800.0),
                CategoryEntity(name = "Healthcare", color = 0xFFF44336.toInt(), icon = "🏥", budgetLimit = 200.0),
                CategoryEntity(name = "Dining", color = 0xFF9C27B0.toInt(), icon = "🍽️", budgetLimit = 400.0),
                CategoryEntity(name = "Shopping", color = 0xFF00BCD4.toInt(), icon = "🛍️", budgetLimit = 500.0),
                CategoryEntity(name = "Other", color = 0xFF9E9E9E.toInt(), icon = "📦", budgetLimit = 300.0)
            )

            defaultCategories.forEach { category ->
                categoryDao.insertCategory(category)
            }

            // Initialize gamification
            database.gamificationDao().insertGamification(
                GamificationEntity(
                    id = 1,
                    currentStreak = 0,
                    longestStreak = 0,
                    lastLoggedDate = null,
                    badgesEarned = "",
                    totalExpensesLogged = 0
                )
            )

            database.userProfileDao().insertUser(
                UserProfileEntity(
                    id = 1,
                    firstName = "User",
                    lastName = "",
                    createdDate = java.time.LocalDate.now().toString()
                )
            )
        }
    }
}
