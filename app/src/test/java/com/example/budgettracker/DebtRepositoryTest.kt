package com.example.budgettracker

import com.example.budgettracker.data.model.DebtPaymentType
import com.example.budgettracker.data.model.DebtStrategy
import com.example.budgettracker.data.model.DebtType
import com.example.budgettracker.data.repository.DebtRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class DebtRepositoryTest {

    private lateinit var repository: DebtRepository

    @Before
    fun setup() {
        repository = DebtRepository(FakeDebtDao(), FakeDebtPaymentDao())
    }

    @Test
    fun addDebt_createsBossWithProjection() = runTest {
        repository.addDebt(
            name = "Credit Card",
            debtType = DebtType.CREDIT_CARD,
            startingBalance = 10_000.0,
            interestRate = 22.0,
            minimumPayment = 500.0,
            paymentDueDay = 25,
            isUnderDebtReview = false,
            interestStillApplies = true,
            strategy = DebtStrategy.HYBRID,
            notes = ""
        )

        val boss = repository.getDebtBosses().first()
        assertEquals("Credit Card", boss.name)
        assertEquals(10_000.0, boss.currentBalance, 0.01)
        assertEquals(20, boss.projectedPayoffMonths)
        assertNotNull(boss.warning)
    }

    @Test
    fun recordPayment_reducesBossHpAndTracksDamage() = runTest {
        val debtId = repository.addDebt(
            name = "Loan",
            debtType = DebtType.PERSONAL_LOAN,
            startingBalance = 5_000.0,
            interestRate = 0.0,
            minimumPayment = 500.0,
            paymentDueDay = 1,
            isUnderDebtReview = false,
            interestStillApplies = false,
            strategy = DebtStrategy.SNOWBALL,
            notes = ""
        )

        repository.recordPayment(
            debtId = debtId,
            amount = 800.0,
            date = "2026-05-01",
            paymentType = DebtPaymentType.EXTRA,
            notes = "Bonus damage"
        )

        val boss = repository.getDebtBosses().first()
        assertEquals(4_200.0, boss.currentBalance, 0.01)
        assertEquals(800.0, boss.damageDealt, 0.01)
        assertEquals(16.0, boss.progressPercentage, 0.01)
    }

    @Test
    fun recordInterest_increasesBossHp() = runTest {
        val debtId = repository.addDebt(
            name = "Store Account",
            debtType = DebtType.STORE_ACCOUNT,
            startingBalance = 2_000.0,
            interestRate = 10.0,
            minimumPayment = 200.0,
            paymentDueDay = 1,
            isUnderDebtReview = false,
            interestStillApplies = true,
            strategy = DebtStrategy.AVALANCHE,
            notes = ""
        )

        repository.recordPayment(
            debtId = debtId,
            amount = 100.0,
            date = "2026-05-01",
            paymentType = DebtPaymentType.INTEREST,
            notes = ""
        )

        assertEquals(2_100.0, repository.getDebtBosses().first().currentBalance, 0.01)
    }
}
