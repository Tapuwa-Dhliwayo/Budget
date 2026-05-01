package com.example.budgettracker

import com.example.budgettracker.data.model.Badge
import com.example.budgettracker.data.model.RecoveryXpEventType
import com.example.budgettracker.data.repository.GamificationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GamificationRepositoryTest {

    private lateinit var repository: GamificationRepository
    private lateinit var eventDao: FakeGamificationEventDao

    @Before
    fun setup() {
        eventDao = FakeGamificationEventDao()
        repository = GamificationRepository(FakeGamificationDao(), eventDao)
    }

    @Test
    fun updateStreakForNewExpense_awardsLoggingXpAndFirstBadge() = runTest {
        repository.updateStreakForNewExpense("2026-05-01")

        val status = repository.getGamificationStatus()

        assertEquals(1, status.currentStreak)
        assertEquals(RecoveryXpEventType.LOG_EXPENSE.xp, status.totalXp)
        assertTrue(status.badgesEarned.any { it == Badge.FIRST_ENTRY })
        assertEquals(1, status.recentEvents.size)
        assertEquals(RecoveryXpEventType.LOG_EXPENSE, status.recentEvents.first().eventType)
    }

    @Test
    fun recordRecoveryEvent_awardsEventXpAndMappedBadge() = runTest {
        repository.recordRecoveryEvent(
            eventType = RecoveryXpEventType.EXTRA_DEBT_PAYMENT,
            occurredDate = "2026-05-02",
            relatedId = 10L
        )

        val status = repository.getGamificationStatus()

        assertEquals(RecoveryXpEventType.EXTRA_DEBT_PAYMENT.xp, status.totalXp)
        assertTrue(status.badgesEarned.any { it == Badge.FIRST_DEBT_ATTACK })
        assertTrue(status.badgesEarned.any { it == Badge.EXTRA_PAYMENT_STRIKE })
        assertEquals(10L, status.recentEvents.first().relatedId)
    }

    @Test
    fun totalXp_movesThroughRecoveryTitles() = runTest {
        repeat(8) {
            repository.recordRecoveryEvent(RecoveryXpEventType.DEBT_DEFEATED, "2026-05-03")
        }

        val status = repository.getGamificationStatus()

        assertEquals(1200, status.totalXp)
        assertEquals(7, status.level)
        assertEquals("Recovery Strategist", status.title)
    }
}
