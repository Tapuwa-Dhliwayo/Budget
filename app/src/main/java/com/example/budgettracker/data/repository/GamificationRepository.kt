package com.example.budgettracker.data.repository

import com.example.budgettracker.data.dao.GamificationDao
import com.example.budgettracker.data.entity.GamificationEntity
import com.example.budgettracker.data.model.Badge
import com.example.budgettracker.data.model.GamificationStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class GamificationRepository(private val dao: GamificationDao) {
    
    fun observeGamificationStatus(): Flow<GamificationStatus?> {
        return dao.observeGamification().map { entity ->
            entity?.let { convertToStatus(it) }
        }
    }
    
    suspend fun getGamificationStatus(): GamificationStatus {
        val entity = dao.getGamification() ?: createInitialGamification()
        return convertToStatus(entity)
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
        
        // Check for new badges
        val currentBadges = gamification.badgesEarned.split(",").filter { it.isNotBlank() }.toMutableSet()
        
        if (newTotalExpenses == 1) currentBadges.add(Badge.FIRST_ENTRY.id)
        if (newStreak >= 7) currentBadges.add(Badge.WEEK_WARRIOR.id)
        if (newStreak >= 30) currentBadges.add(Badge.STREAK_MASTER.id)
        
        val updated = gamification.copy(
            currentStreak = newStreak,
            longestStreak = newLongestStreak,
            lastLoggedDate = expenseDate,
            badgesEarned = currentBadges.joinToString(","),
            totalExpensesLogged = newTotalExpenses
        )
        
        dao.updateGamification(updated)
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
    
    private fun convertToStatus(entity: GamificationEntity): GamificationStatus {
        val badgeIds = entity.badgesEarned.split(",").filter { it.isNotBlank() }
        val badges = Badge.values().filter { it.id in badgeIds }
        
        return GamificationStatus(
            currentStreak = entity.currentStreak,
            longestStreak = entity.longestStreak,
            badgesEarned = badges,
            totalExpenses = entity.totalExpensesLogged
        )
    }
}
