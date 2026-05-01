package com.example.budgettracker.data.repository

import com.example.budgettracker.data.dao.GamificationDao
import com.example.budgettracker.data.dao.GamificationEventDao
import com.example.budgettracker.data.entity.GamificationEntity
import com.example.budgettracker.data.entity.GamificationEventEntity
import com.example.budgettracker.data.model.Badge
import com.example.budgettracker.data.model.GamificationStatus
import com.example.budgettracker.data.model.RecoveryXpEvent
import com.example.budgettracker.data.model.RecoveryXpEventType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class GamificationRepository(
    private val dao: GamificationDao,
    private val eventDao: GamificationEventDao? = null
) {
    
    fun observeGamificationStatus(): Flow<GamificationStatus?> {
        return dao.observeGamification().map { entity ->
            entity?.let { convertToStatus(it) }
        }
    }
    
    suspend fun getGamificationStatus(): GamificationStatus {
        val entity = dao.getGamification() ?: createInitialGamification()
        return convertToStatus(entity, eventDao?.getRecentEvents(8).orEmpty())
    }
    
    /**
     * Update streak when a new expense is logged
     */
    suspend fun updateStreakForNewExpense(expenseDate: String) {
        val gamification = dao.getGamification() ?: createInitialGamification()
        
        val today = LocalDate.parse(expenseDate)
        val lastLogged = gamification.lastLoggedDate?.let { LocalDate.parse(it) }
        
        val newStreak = when {
            lastLogged == null -> 1 // First expense ever
            lastLogged == today -> gamification.currentStreak // Same day, no change
            ChronoUnit.DAYS.between(lastLogged, today) == 1L -> gamification.currentStreak + 1 // Consecutive day
            else -> 1 // Streak broken, restart
        }
        
        val newLongestStreak = maxOf(newStreak, gamification.longestStreak)
        val newTotalExpenses = gamification.totalExpensesLogged + 1
        
        val currentBadges = gamification.badgesEarned.split(",").filter { it.isNotBlank() }.toMutableSet()
        
        if (newTotalExpenses == 1) currentBadges.add(Badge.FIRST_ENTRY.id)
        if (newStreak >= 7) currentBadges.add(Badge.WEEK_WARRIOR.id)
        if (newStreak >= 30) currentBadges.add(Badge.STREAK_MASTER.id)
        val xpEvent = RecoveryXpEventType.LOG_EXPENSE
        
        val updated = gamification.copy(
            currentStreak = newStreak,
            longestStreak = newLongestStreak,
            lastLoggedDate = expenseDate,
            badgesEarned = currentBadges.joinToString(","),
            totalExpensesLogged = newTotalExpenses,
            totalXp = gamification.totalXp + xpEvent.xp
        )
        
        dao.updateGamification(updated)
        insertEvent(
            eventType = xpEvent,
            occurredDate = expenseDate,
            message = "You logged honestly. Recovery XP earned.",
            relatedId = null
        )
    }

    suspend fun recordRecoveryEvent(
        eventType: RecoveryXpEventType,
        occurredDate: String = LocalDate.now().toString(),
        relatedId: Long? = null,
        message: String = defaultMessageFor(eventType)
    ) {
        val gamification = dao.getGamification() ?: createInitialGamification()
        val badges = gamification.badgesEarned.split(",").filter { it.isNotBlank() }.toMutableSet()
        badges.addAll(badgesFor(eventType).map { it.id })
        dao.updateGamification(
            gamification.copy(
                badgesEarned = badges.joinToString(","),
                totalXp = gamification.totalXp + eventType.xp
            )
        )
        insertEvent(eventType, occurredDate, message, relatedId)
    }
    
    /**
     * Award budget hero badge when user stays under budget
     */
    suspend fun awardBudgetHeroBadge() {
        val gamification = dao.getGamification() ?: createInitialGamification()
        val badges = gamification.badgesEarned.split(",").filter { it.isNotBlank() }.toMutableSet()
        
        if (badges.add(Badge.BUDGET_HERO.id)) {
            val updated = gamification.copy(badgesEarned = badges.joinToString(","))
            dao.updateGamification(updated)
        }
    }
    
    private suspend fun createInitialGamification(): GamificationEntity {
        val initial = GamificationEntity()
        dao.insertGamification(initial)
        return initial
    }
    
    private fun convertToStatus(
        entity: GamificationEntity,
        recentEvents: List<GamificationEventEntity> = emptyList()
    ): GamificationStatus {
        val badgeIds = entity.badgesEarned.split(",").filter { it.isNotBlank() }
        val badges = Badge.values().filter { it.id in badgeIds }
        val level = levelFor(entity.totalXp)
        val bounds = levelBounds(level)
        
        return GamificationStatus(
            currentStreak = entity.currentStreak,
            longestStreak = entity.longestStreak,
            badgesEarned = badges,
            totalExpenses = entity.totalExpensesLogged,
            totalXp = entity.totalXp,
            level = level,
            title = titleFor(level),
            currentLevelXp = bounds.first,
            nextLevelXp = bounds.second,
            recentEvents = recentEvents.map { it.toModel() }
        )
    }

    private suspend fun insertEvent(
        eventType: RecoveryXpEventType,
        occurredDate: String,
        message: String,
        relatedId: Long?
    ) {
        eventDao?.insertEvent(
            GamificationEventEntity(
                eventType = eventType.name,
                xpEarned = eventType.xp,
                occurredDate = occurredDate,
                message = message,
                relatedId = relatedId
            )
        )
    }

    private fun badgesFor(eventType: RecoveryXpEventType): List<Badge> {
        return when (eventType) {
            RecoveryXpEventType.DEBT_PAYMENT -> listOf(Badge.FIRST_DEBT_ATTACK)
            RecoveryXpEventType.EXTRA_DEBT_PAYMENT -> listOf(Badge.FIRST_DEBT_ATTACK, Badge.EXTRA_PAYMENT_STRIKE)
            RecoveryXpEventType.DEBT_DEFEATED -> listOf(Badge.FIRST_DEBT_ATTACK)
            RecoveryXpEventType.NET_WORTH_SNAPSHOT,
            RecoveryXpEventType.NET_WORTH_IMPROVED -> listOf(Badge.NET_WORTH_TRACKER)
            RecoveryXpEventType.LOG_EXTRA_INCOME,
            RecoveryXpEventType.ALLOCATE_EXTRA_TO_RECOVERY -> listOf(Badge.EXTRA_INCOME_MOVER)
            RecoveryXpEventType.CREATE_GOAL -> listOf(Badge.GOAL_STARTED)
            RecoveryXpEventType.COMPLETE_GOAL -> listOf(Badge.GOAL_STARTED, Badge.GOAL_FINISHED)
            RecoveryXpEventType.COMPLETE_WEEKLY_REVIEW -> listOf(Badge.RECOVERY_REVIEW)
            else -> emptyList()
        }
    }

    private fun defaultMessageFor(eventType: RecoveryXpEventType): String {
        return when (eventType) {
            RecoveryXpEventType.LOG_EXPENSE -> "You logged honestly. Recovery XP earned."
            RecoveryXpEventType.SET_WEEKLY_ALLOWANCE -> "You planned the week before it planned you."
            RecoveryXpEventType.COMPLETE_WEEKLY_REVIEW -> "You reviewed the week honestly. Recovery beats perfection."
            RecoveryXpEventType.RECOVERY_ACTION_COMPLETED -> "You completed a recovery action."
            RecoveryXpEventType.DEBT_PAYMENT -> "You dealt damage to debt."
            RecoveryXpEventType.EXTRA_DEBT_PAYMENT -> "Extra debt damage logged."
            RecoveryXpEventType.DEBT_DEFEATED -> "Debt defeated. One less weight on your future."
            RecoveryXpEventType.NET_WORTH_SNAPSHOT -> "You saved a net worth checkpoint."
            RecoveryXpEventType.NET_WORTH_IMPROVED -> "Your net worth moved in the right direction."
            RecoveryXpEventType.LOG_EXTRA_INCOME -> "You recorded extra effort before it disappeared."
            RecoveryXpEventType.ALLOCATE_EXTRA_TO_RECOVERY -> "Extra income was given a recovery job."
            RecoveryXpEventType.CREATE_GOAL -> "You turned a future need into a measurable plan."
            RecoveryXpEventType.GOAL_CONTRIBUTION -> "Goal progress recorded."
            RecoveryXpEventType.COMPLETE_GOAL -> "Goal completed. That is real movement."
            RecoveryXpEventType.BACKUP_CREATED -> "Backup created. Your data stays yours."
        }
    }

    private fun levelFor(totalXp: Int): Int {
        return when {
            totalXp >= 2200 -> 9
            totalXp >= 1500 -> 8
            totalXp >= 1000 -> 7
            totalXp >= 650 -> 6
            totalXp >= 400 -> 5
            totalXp >= 250 -> 4
            totalXp >= 120 -> 3
            totalXp >= 40 -> 2
            else -> 1
        }
    }

    private fun levelBounds(level: Int): Pair<Int, Int> {
        return when (level) {
            1 -> 0 to 40
            2 -> 40 to 120
            3 -> 120 to 250
            4 -> 250 to 400
            5 -> 400 to 650
            6 -> 650 to 1000
            7 -> 1000 to 1500
            8 -> 1500 to 2200
            else -> 2200 to 2200
        }
    }

    private fun titleFor(level: Int): String {
        return when (level) {
            1 -> "Facing Reality"
            2 -> "Budget Rookie"
            3 -> "Expense Watcher"
            4 -> "Allowance Defender"
            5 -> "Debt Fighter"
            6 -> "Habit Builder"
            7 -> "Recovery Strategist"
            8 -> "Net Worth Climber"
            else -> "Financial Guardian"
        }
    }

    private fun GamificationEventEntity.toModel(): RecoveryXpEvent {
        return RecoveryXpEvent(
            eventId = id,
            eventType = RecoveryXpEventType.values().firstOrNull { it.name == eventType }
                ?: RecoveryXpEventType.LOG_EXPENSE,
            xpEarned = xpEarned,
            occurredDate = occurredDate,
            message = message,
            relatedId = relatedId
        )
    }
}
