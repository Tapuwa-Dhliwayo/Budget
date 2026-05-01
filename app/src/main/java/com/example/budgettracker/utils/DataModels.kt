package com.example.budgettracker.data.model

/**
 * Represents category spending with budget comparison
 */
data class CategorySpending(
    val categoryId: Long,
    val categoryName: String,
    val categoryColor: Int,
    val categoryIcon: String,
    val totalSpent: Double,
    val budgetLimit: Double,
    val percentageUsed: Double,
    val remaining: Double,
    val isOverBudget: Boolean
)

/**
 * Monthly overview statistics
 */
data class MonthlyOverview(
    val monthId: String,
    val totalBudget: Double,
    val totalSpent: Double,
    val remaining: Double,
    val percentageUsed: Double,
    val isOverBudget: Boolean
)

/**
 * Expense with category details for display
 */
data class ExpenseWithCategory(
    val expenseId: Long,
    val amount: Double,
    val description: String,
    val date: String,
    val photoPath: String?,
    val isRecurring: Boolean,
    val categoryName: String,
    val categoryColor: Int,
    val categoryIcon: String
)

/**
 * Category summary for text-based analytics
 */
data class CategorySummary(
    val categoryName: String,
    val categoryIcon: String,
    val amount: Double,
    val percentage: Double,
    val color: Int
)

/**
 * Gamification status
 */
data class GamificationStatus(
    val currentStreak: Int,
    val longestStreak: Int,
    val badgesEarned: List<Badge>,
    val totalExpenses: Int,
    val totalXp: Int = 0,
    val level: Int = 1,
    val title: String = "Facing Reality",
    val currentLevelXp: Int = 0,
    val nextLevelXp: Int = 100,
    val recentEvents: List<RecoveryXpEvent> = emptyList()
)

/**
 * Badge definition
 */
enum class Badge(val id: String, val title: String, val description: String) {
    FIRST_ENTRY("first_entry", "First Step", "Logged your first expense"),
    WEEK_WARRIOR("week_warrior", "Week Warrior", "7-day logging streak"),
    BUDGET_HERO("budget_hero", "Budget Hero", "Stayed under budget for a month"),
    STREAK_MASTER("streak_master", "Streak Master", "30-day logging streak"),
    FIRST_DEBT_ATTACK("first_debt_attack", "First Debt Attack", "Logged a debt payment"),
    EXTRA_PAYMENT_STRIKE("extra_payment_strike", "Extra Payment Strike", "Used extra money against debt"),
    NET_WORTH_TRACKER("net_worth_tracker", "Net Worth Tracker", "Created a net worth snapshot"),
    EXTRA_INCOME_MOVER("extra_income_mover", "Extra Income Mover", "Logged extra income"),
    GOAL_STARTED("goal_started", "Goal Started", "Created a financial goal"),
    GOAL_FINISHED("goal_finished", "Goal Finished", "Completed a financial goal"),
    RECOVERY_REVIEW("recovery_review", "Recovery Review", "Completed a weekly review")
}

enum class RecoveryXpEventType(val label: String, val xp: Int) {
    LOG_EXPENSE("Logged expense", 5),
    SET_WEEKLY_ALLOWANCE("Set weekly allowance", 20),
    COMPLETE_WEEKLY_REVIEW("Completed weekly review", 35),
    RECOVERY_ACTION_COMPLETED("Completed recovery action", 20),
    DEBT_PAYMENT("Debt payment", 30),
    EXTRA_DEBT_PAYMENT("Extra debt payment", 50),
    DEBT_DEFEATED("Debt defeated", 150),
    NET_WORTH_SNAPSHOT("Net worth snapshot", 40),
    NET_WORTH_IMPROVED("Net worth improved", 75),
    LOG_EXTRA_INCOME("Logged extra income", 25),
    ALLOCATE_EXTRA_TO_RECOVERY("Allocated extra income to recovery", 45),
    CREATE_GOAL("Created goal", 25),
    GOAL_CONTRIBUTION("Goal contribution", 30),
    COMPLETE_GOAL("Completed goal", 120),
    BACKUP_CREATED("Created backup", 40)
}

data class RecoveryXpEvent(
    val eventId: Long,
    val eventType: RecoveryXpEventType,
    val xpEarned: Int,
    val occurredDate: String,
    val message: String,
    val relatedId: Long?
)

data class BackupStatus(
    val lastBackupDate: String?,
    val lastBackupPath: String?,
    val daysSinceBackup: Long?,
    val backupNeeded: Boolean,
    val guidance: String
)

enum class WeeklyAllowanceStatus(val label: String) {
    NOT_SET("Not set"),
    STABLE("Stable"),
    WATCHFUL("Watchful"),
    PRESSURED("Pressured"),
    CRITICAL("Critical"),
    OVER_PLAN("Over plan")
}

data class WeeklyAllowanceSummary(
    val weekStartDate: String,
    val weekEndDate: String,
    val allowanceSet: Boolean,
    val allowanceAmount: Double,
    val spent: Double,
    val remaining: Double,
    val safeDailySpend: Double,
    val daysRemaining: Int,
    val status: WeeklyAllowanceStatus,
    val guidance: String,
    val categoryPressures: List<WeeklyCategoryPressure> = emptyList(),
    val recoveryActions: List<WeeklyRecoveryAction> = emptyList(),
    val review: WeeklyReview? = null
)

data class WeeklyCategoryPressure(
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val amount: Double,
    val percentageOfWeekSpend: Double,
    val weeklyLimit: Double? = null,
    val limitRemaining: Double? = null,
    val isOverLimit: Boolean = false
)

data class WeeklyRecoveryAction(
    val actionText: String,
    val isCompleted: Boolean
)

data class WeeklyReview(
    val weekStartDate: String,
    val weekEndDate: String,
    val wentWell: String,
    val challenge: String,
    val nextWeekAdjustment: String
)

enum class DebtType(val label: String) {
    CREDIT_CARD("Credit card"),
    PERSONAL_LOAN("Personal loan"),
    STORE_ACCOUNT("Store account"),
    VEHICLE_FINANCE("Vehicle finance"),
    MORTGAGE("Mortgage"),
    FAMILY_FRIEND("Family or friend loan"),
    DEBT_REVIEW("Debt review account"),
    OTHER("Other")
}

enum class DebtStrategy(val label: String) {
    SNOWBALL("Snowball"),
    AVALANCHE("Avalanche"),
    HYBRID("Hybrid")
}

enum class DebtPaymentType(val label: String) {
    MINIMUM("Minimum payment"),
    EXTRA("Extra payment"),
    SETTLEMENT("Settlement payment"),
    ADJUSTMENT("Adjustment"),
    INTEREST("Interest charge"),
    FEE("Fee")
}

data class DebtBoss(
    val debtId: Long,
    val name: String,
    val debtType: DebtType,
    val startingBalance: Double,
    val currentBalance: Double,
    val interestRate: Double,
    val minimumPayment: Double,
    val paymentDueDay: Int,
    val isUnderDebtReview: Boolean,
    val interestStillApplies: Boolean,
    val strategy: DebtStrategy,
    val notes: String,
    val createdDate: String,
    val closedDate: String?,
    val progressPercentage: Double,
    val damageDealt: Double,
    val projectedPayoffMonths: Int?,
    val projectedPayoffLabel: String,
    val warning: String?
)

data class DebtBattleSummary(
    val totalStartingBalance: Double,
    val totalCurrentBalance: Double,
    val totalDamageDealt: Double,
    val minimumPayments: Double,
    val extraPayments: Double,
    val interestAndFees: Double,
    val netDamage: Double,
    val strongestAttack: String?
)

enum class AssetType(val label: String) {
    BANK_ACCOUNT("Bank account"),
    EMERGENCY_FUND("Emergency fund"),
    CASH("Cash"),
    INVESTMENT("Investment"),
    RETIREMENT("Retirement fund"),
    PROPERTY("Property"),
    VEHICLE("Vehicle"),
    BUSINESS("Business asset"),
    OTHER("Other")
}

data class FinancialAsset(
    val assetId: Long,
    val name: String,
    val assetType: AssetType,
    val currentValue: Double,
    val notes: String,
    val updatedAt: Long
)

data class NetWorthSnapshot(
    val snapshotId: Long,
    val snapshotDate: String,
    val totalAssets: Double,
    val totalDebts: Double,
    val netWorth: Double,
    val notes: String
)

data class NetWorthSummary(
    val assets: List<FinancialAsset>,
    val recentSnapshots: List<NetWorthSnapshot>,
    val totalAssets: Double,
    val totalDebts: Double,
    val currentNetWorth: Double,
    val latestSnapshot: NetWorthSnapshot?,
    val previousSnapshot: NetWorthSnapshot?,
    val movementSincePrevious: Double?,
    val assetMovementSincePrevious: Double?,
    val debtMovementSincePrevious: Double?,
    val guidance: String
)

enum class ExtraIncomeType(val label: String) {
    OVERTIME("Overtime"),
    FREELANCE("Freelance work"),
    BONUS("Bonus"),
    SIDE_PROJECT("Side project"),
    ONCE_OFF("Once-off income"),
    REIMBURSEMENT("Reimbursement"),
    CASH_GIFT("Cash gift"),
    SALE_OF_ITEM("Sale of item"),
    REFUND("Refund"),
    OTHER("Other")
}

enum class ExtraIncomeAllocationType(val label: String, val isRecovery: Boolean) {
    UNALLOCATED("Not allocated yet", false),
    DEBT_PAYMENT("Debt payment", true),
    EMERGENCY_FUND("Emergency fund", true),
    GOAL_CONTRIBUTION("Goal contribution", true),
    LIVING_EXPENSES("Living expenses", false),
    PERSONAL_REWARD("Personal reward", false),
    BUFFER("Buffer", true),
    OTHER("Other", false)
}

data class ExtraIncomeEntry(
    val incomeId: Long,
    val source: String,
    val incomeType: ExtraIncomeType,
    val amount: Double,
    val dateReceived: String,
    val allocationType: ExtraIncomeAllocationType,
    val linkedDebtId: Long?,
    val linkedDebtName: String?,
    val notes: String
)

data class ExtraIncomeImpactSummary(
    val monthId: String,
    val totalIncome: Double,
    val recoveryAmount: Double,
    val debtRecoveryAmount: Double,
    val savingsAndGoalsAmount: Double,
    val spendingPersonalAmount: Double,
    val unallocatedAmount: Double,
    val recoveryPercentage: Double,
    val mainImpact: String,
    val guidance: String,
    val recentEntries: List<ExtraIncomeEntry>
)

enum class GoalType(val label: String) {
    EMERGENCY_FUND("Emergency fund"),
    DEBT_PAYOFF("Debt payoff"),
    ESSENTIAL_ITEM("Essential item"),
    RENT_DEPOSIT("Rent deposit"),
    HOLIDAY_TRAVEL("Holiday or travel"),
    SCHOOL_FEES("School fees"),
    HOME_IMPROVEMENT("Home improvement"),
    INVESTMENT("Investment"),
    CUSTOM("Custom goal")
}

enum class GoalStatus(val label: String) {
    ACTIVE("Active"),
    PAUSED("Paused"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    DEFERRED("Deferred")
}

enum class GoalHealthClassification(val label: String) {
    HEALTHY("Healthy"),
    NEUTRAL("Neutral"),
    RISKY("Risky")
}

enum class GoalFeasibilityStatus(val label: String) {
    FEASIBLE("Feasible"),
    TIGHT("Tight"),
    RISKY("Risky"),
    NOT_FEASIBLE("Not feasible with current plan")
}

enum class GoalPriority(val label: String) {
    CRITICAL("Critical"),
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low")
}

enum class GoalContributionSourceType(val label: String) {
    REGULAR_SAVING("Regular saving"),
    EXTRA_INCOME("Extra income"),
    TRANSFER("Transfer"),
    ADJUSTMENT("Adjustment"),
    OTHER("Other")
}

data class GoalPlan(
    val goalId: Long,
    val name: String,
    val goalType: GoalType,
    val targetAmount: Double,
    val currentAmount: Double,
    val remainingAmount: Double,
    val progressPercentage: Double,
    val targetDate: String?,
    val monthsRemaining: Int?,
    val requiredMonthlyContribution: Double?,
    val monthlyContributionTarget: Double,
    val priority: GoalPriority,
    val healthClassification: GoalHealthClassification,
    val feasibilityStatus: GoalFeasibilityStatus,
    val status: GoalStatus,
    val guidance: String,
    val notes: String
)

data class GoalContribution(
    val contributionId: Long,
    val goalId: Long,
    val amount: Double,
    val contributionDate: String,
    val sourceType: GoalContributionSourceType,
    val linkedExtraIncomeId: Long?,
    val notes: String
)

data class GoalSummary(
    val activeGoals: List<GoalPlan>,
    val completedGoals: List<GoalPlan>,
    val totalTargetAmount: Double,
    val totalCurrentAmount: Double,
    val totalRemainingAmount: Double,
    val guidance: String
)
