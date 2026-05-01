package com.example.budgettracker

import com.example.budgettracker.data.model.AssetType
import com.example.budgettracker.data.model.DebtStrategy
import com.example.budgettracker.data.model.DebtType
import com.example.budgettracker.data.repository.DebtRepository
import com.example.budgettracker.data.repository.NetWorthRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NetWorthRepositoryTest {

    private lateinit var assetDao: FakeAssetDao
    private lateinit var debtDao: FakeDebtDao
    private lateinit var snapshotDao: FakeNetWorthSnapshotDao
    private lateinit var netWorthRepository: NetWorthRepository
    private lateinit var debtRepository: DebtRepository

    @Before
    fun setup() {
        assetDao = FakeAssetDao()
        debtDao = FakeDebtDao()
        snapshotDao = FakeNetWorthSnapshotDao()
        netWorthRepository = NetWorthRepository(assetDao, debtDao, snapshotDao)
        debtRepository = DebtRepository(debtDao, FakeDebtPaymentDao())
    }

    @Test
    fun getSummary_calculatesAssetsMinusDebts() = runTest {
        netWorthRepository.addAsset(
            name = "Emergency fund",
            assetType = AssetType.EMERGENCY_FUND,
            currentValue = 3_000.0,
            notes = ""
        )
        debtRepository.addDebt(
            name = "Credit card",
            debtType = DebtType.CREDIT_CARD,
            startingBalance = 5_500.0,
            interestRate = 0.0,
            minimumPayment = 500.0,
            paymentDueDay = 25,
            isUnderDebtReview = false,
            interestStillApplies = false,
            strategy = DebtStrategy.HYBRID,
            notes = ""
        )

        val summary = netWorthRepository.getSummary()

        assertEquals(3_000.0, summary.totalAssets, 0.01)
        assertEquals(5_500.0, summary.totalDebts, 0.01)
        assertEquals(-2_500.0, summary.currentNetWorth, 0.01)
        assertTrue(summary.guidance.contains("first snapshot"))
    }

    @Test
    fun createSnapshot_preservesHistoricalTotalsAndMovement() = runTest {
        val assetId = netWorthRepository.addAsset(
            name = "Savings",
            assetType = AssetType.BANK_ACCOUNT,
            currentValue = 1_000.0,
            notes = ""
        )
        debtRepository.addDebt(
            name = "Loan",
            debtType = DebtType.PERSONAL_LOAN,
            startingBalance = 2_000.0,
            interestRate = 0.0,
            minimumPayment = 200.0,
            paymentDueDay = 1,
            isUnderDebtReview = false,
            interestStillApplies = false,
            strategy = DebtStrategy.SNOWBALL,
            notes = ""
        )

        netWorthRepository.createSnapshot(snapshotDate = "2026-04-30")
        netWorthRepository.updateAssetValue(assetId, 1_600.0, "")
        netWorthRepository.createSnapshot(snapshotDate = "2026-05-31")

        val summary = netWorthRepository.getSummary()

        assertEquals(2, summary.recentSnapshots.size)
        assertEquals(-400.0, summary.latestSnapshot?.netWorth ?: 0.0, 0.01)
        assertEquals(600.0, summary.movementSincePrevious ?: 0.0, 0.01)
        assertEquals(600.0, summary.assetMovementSincePrevious ?: 0.0, 0.01)
    }
}
