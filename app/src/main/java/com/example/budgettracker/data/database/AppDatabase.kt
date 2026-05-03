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
        UserProfileEntity::class,
        WeeklyAllowanceEntity::class,
        WeeklyReviewEntity::class,
        WeeklyCategoryAllowanceEntity::class,
        WeeklyRecoveryActionEntity::class,
        DebtEntity::class,
        DebtPaymentEntity::class,
        AssetEntity::class,
        NetWorthSnapshotEntity::class,
        ExtraIncomeEntity::class,
        GoalEntity::class,
        GoalContributionEntity::class,
        GamificationEventEntity::class
    ],
    version = 12,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun monthlyBudgetDao(): MonthlyBudgetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun gamificationDao(): GamificationDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun weeklyAllowanceDao(): WeeklyAllowanceDao
    abstract fun weeklyReviewDao(): WeeklyReviewDao
    abstract fun weeklyCategoryAllowanceDao(): WeeklyCategoryAllowanceDao
    abstract fun weeklyRecoveryActionDao(): WeeklyRecoveryActionDao
    abstract fun debtDao(): DebtDao
    abstract fun debtPaymentDao(): DebtPaymentDao
    abstract fun assetDao(): AssetDao
    abstract fun netWorthSnapshotDao(): NetWorthSnapshotDao
    abstract fun extraIncomeDao(): ExtraIncomeDao
    abstract fun goalDao(): GoalDao
    abstract fun goalContributionDao(): GoalContributionDao
    abstract fun gamificationEventDao(): GamificationEventDao

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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE expenses ADD COLUMN isRecurring INTEGER NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS weekly_allowance (
                        weekStartDate TEXT PRIMARY KEY NOT NULL,
                        weekEndDate TEXT NOT NULL,
                        allowanceAmount REAL NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS weekly_review (
                        weekStartDate TEXT PRIMARY KEY NOT NULL,
                        weekEndDate TEXT NOT NULL,
                        wentWell TEXT NOT NULL,
                        challenge TEXT NOT NULL,
                        nextWeekAdjustment TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS weekly_category_allowance (
                        weekStartDate TEXT NOT NULL,
                        weekEndDate TEXT NOT NULL,
                        categoryId INTEGER NOT NULL,
                        limitAmount REAL NOT NULL,
                        updatedAt INTEGER NOT NULL,
                        PRIMARY KEY(weekStartDate, categoryId)
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS weekly_recovery_action (
                        weekStartDate TEXT NOT NULL,
                        weekEndDate TEXT NOT NULL,
                        actionText TEXT NOT NULL,
                        isCompleted INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL,
                        PRIMARY KEY(weekStartDate, actionText)
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS debts (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        debtType TEXT NOT NULL,
                        startingBalance REAL NOT NULL,
                        currentBalance REAL NOT NULL,
                        interestRate REAL NOT NULL,
                        minimumPayment REAL NOT NULL,
                        paymentDueDay INTEGER NOT NULL,
                        isUnderDebtReview INTEGER NOT NULL,
                        interestStillApplies INTEGER NOT NULL,
                        strategy TEXT NOT NULL,
                        notes TEXT NOT NULL,
                        createdDate TEXT NOT NULL,
                        closedDate TEXT
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS debt_payments (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        debtId INTEGER NOT NULL,
                        amount REAL NOT NULL,
                        date TEXT NOT NULL,
                        paymentType TEXT NOT NULL,
                        notes TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        FOREIGN KEY(debtId) REFERENCES debts(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_debt_payments_debtId ON debt_payments(debtId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_debt_payments_date ON debt_payments(date)")
            }
        }

        val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS assets (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        assetType TEXT NOT NULL,
                        currentValue REAL NOT NULL,
                        notes TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL,
                        isActive INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS net_worth_snapshots (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        snapshotDate TEXT NOT NULL,
                        totalAssets REAL NOT NULL,
                        totalDebts REAL NOT NULL,
                        netWorth REAL NOT NULL,
                        notes TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_assets_isActive ON assets(isActive)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_net_worth_snapshots_snapshotDate ON net_worth_snapshots(snapshotDate)")
            }
        }

        val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS extra_income (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        source TEXT NOT NULL,
                        incomeType TEXT NOT NULL,
                        amount REAL NOT NULL,
                        dateReceived TEXT NOT NULL,
                        allocationType TEXT NOT NULL,
                        linkedDebtId INTEGER,
                        linkedGoalId INTEGER,
                        notes TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_extra_income_dateReceived ON extra_income(dateReceived)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_extra_income_allocationType ON extra_income(allocationType)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_extra_income_linkedDebtId ON extra_income(linkedDebtId)")
            }
        }

        val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS goals (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        goalType TEXT NOT NULL,
                        targetAmount REAL NOT NULL,
                        currentAmount REAL NOT NULL,
                        targetDate TEXT,
                        monthlyContributionTarget REAL NOT NULL,
                        priority TEXT NOT NULL,
                        healthClassification TEXT NOT NULL,
                        status TEXT NOT NULL,
                        notes TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL,
                        completedAt TEXT
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS goal_contributions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        goalId INTEGER NOT NULL,
                        amount REAL NOT NULL,
                        contributionDate TEXT NOT NULL,
                        sourceType TEXT NOT NULL,
                        linkedExtraIncomeId INTEGER,
                        notes TEXT NOT NULL,
                        createdAt INTEGER NOT NULL,
                        updatedAt INTEGER NOT NULL,
                        FOREIGN KEY(goalId) REFERENCES goals(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_goals_status ON goals(status)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_goals_targetDate ON goals(targetDate)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_goals_goalType ON goals(goalType)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_goal_contributions_goalId ON goal_contributions(goalId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_goal_contributions_contributionDate ON goal_contributions(contributionDate)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_goal_contributions_linkedExtraIncomeId ON goal_contributions(linkedExtraIncomeId)")
            }
        }

        val MIGRATION_10_11 = object : Migration(10, 11) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE gamification ADD COLUMN totalXp INTEGER NOT NULL DEFAULT 0")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS gamification_events (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        eventType TEXT NOT NULL,
                        xpEarned INTEGER NOT NULL,
                        occurredDate TEXT NOT NULL,
                        message TEXT NOT NULL,
                        relatedId INTEGER,
                        createdAt INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_gamification_events_eventType ON gamification_events(eventType)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_gamification_events_occurredDate ON gamification_events(occurredDate)")
            }
        }


        val MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE user_profile ADD COLUMN budgetStartDay INTEGER NOT NULL DEFAULT 1")
                db.execSQL("ALTER TABLE user_profile ADD COLUMN themeKey TEXT NOT NULL DEFAULT 'recovery_arcade'")
            }
        }

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_tracker.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7, MIGRATION_7_8, MIGRATION_8_9, MIGRATION_9_10, MIGRATION_10_11, MIGRATION_11_12)
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
