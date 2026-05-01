package com.example.budgettracker

import com.example.budgettracker.data.model.DebtStrategy
import com.example.budgettracker.data.model.DebtType
import com.example.budgettracker.data.model.ExtraIncomeAllocationType
import com.example.budgettracker.data.model.ExtraIncomeType
import com.example.budgettracker.data.repository.DebtRepository
import com.example.budgettracker.data.repository.ExtraIncomeRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExtraIncomeRepositoryTest {

    private lateinit var extraIncomeDao: FakeExtraIncomeDao
    private lateinit var debtDao: FakeDebtDao
    private lateinit var repository: ExtraIncomeRepository
    private lateinit var debtRepository: DebtRepository

    @Before
    fun setup() {
        extraIncomeDao = FakeExtraIncomeDao()
        debtDao = FakeDebtDao()
        repository = ExtraIncomeRepository(extraIncomeDao, debtDao)
        debtRepository = DebtRepository(debtDao, FakeDebtPaymentDao())
    }

    @Test
    fun getMonthlyImpact_summarizesAllocationBuckets() = runTest {
        repository.addIncome(
            source = "Overtime",
            incomeType = ExtraIncomeType.OVERTIME,
            amount = 1_500.0,
            dateReceived = "2026-05-05",
            allocationType = ExtraIncomeAllocationType.DEBT_PAYMENT,
            linkedDebtId = null,
            notes = ""
        )
        repository.addIncome(
            source = "Refund",
            incomeType = ExtraIncomeType.REFUND,
            amount = 500.0,
            dateReceived = "2026-05-08",
            allocationType = ExtraIncomeAllocationType.PERSONAL_REWARD,
            linkedDebtId = null,
            notes = ""
        )
        repository.addIncome(
            source = "Bonus",
            incomeType = ExtraIncomeType.BONUS,
            amount = 1_000.0,
            dateReceived = "2026-05-10",
            allocationType = ExtraIncomeAllocationType.EMERGENCY_FUND,
            linkedDebtId = null,
            notes = ""
        )

        val summary = repository.getMonthlyImpact("2026-05")

        assertEquals(3_000.0, summary.totalIncome, 0.01)
        assertEquals(2_500.0, summary.recoveryAmount, 0.01)
        assertEquals(1_500.0, summary.debtRecoveryAmount, 0.01)
        assertEquals(1_000.0, summary.savingsAndGoalsAmount, 0.01)
        assertEquals(500.0, summary.spendingPersonalAmount, 0.01)
        assertEquals(83.33, summary.recoveryPercentage, 0.1)
    }

    @Test
    fun getMonthlyImpact_showsLinkedDebtAsMainImpact() = runTest {
        val debtId = debtRepository.addDebt(
            name = "Credit Card",
            debtType = DebtType.CREDIT_CARD,
            startingBalance = 6_000.0,
            interestRate = 20.0,
            minimumPayment = 500.0,
            paymentDueDay = 25,
            isUnderDebtReview = false,
            interestStillApplies = true,
            strategy = DebtStrategy.HYBRID,
            notes = ""
        )
        repository.addIncome(
            source = "Freelance",
            incomeType = ExtraIncomeType.FREELANCE,
            amount = 2_000.0,
            dateReceived = "2026-05-12",
            allocationType = ExtraIncomeAllocationType.DEBT_PAYMENT,
            linkedDebtId = debtId,
            notes = ""
        )

        val summary = repository.getMonthlyImpact("2026-05")

        assertEquals("Credit Card", summary.recentEntries.first().linkedDebtName)
        assertTrue(summary.mainImpact.contains("Credit Card"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun addIncome_rejectsLinkedDebtForNonDebtAllocation() = runTest {
        repository.addIncome(
            source = "Bonus",
            incomeType = ExtraIncomeType.BONUS,
            amount = 1_000.0,
            dateReceived = "2026-05-01",
            allocationType = ExtraIncomeAllocationType.BUFFER,
            linkedDebtId = 1L,
            notes = ""
        )
    }
}
