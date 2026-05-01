package com.example.budgettracker.data.repository

import com.example.budgettracker.data.dao.DebtDao
import com.example.budgettracker.data.dao.DebtPaymentDao
import com.example.budgettracker.data.entity.DebtEntity
import com.example.budgettracker.data.entity.DebtPaymentEntity
import com.example.budgettracker.data.model.DebtBattleSummary
import com.example.budgettracker.data.model.DebtBoss
import com.example.budgettracker.data.model.DebtPaymentType
import com.example.budgettracker.data.model.DebtStrategy
import com.example.budgettracker.data.model.DebtType
import com.example.budgettracker.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.YearMonth

class DebtRepository(
    private val debtDao: DebtDao,
    private val debtPaymentDao: DebtPaymentDao
) {
    fun observeDebtBosses(): Flow<List<DebtBoss>> {
        return debtDao.observeDebts().map { debts -> debts.map { it.toBoss() } }
    }

    suspend fun getDebtBosses(): List<DebtBoss> {
        return debtDao.getAllDebts().map { it.toBoss() }
    }

    suspend fun addDebt(
        name: String,
        debtType: DebtType,
        startingBalance: Double,
        interestRate: Double,
        minimumPayment: Double,
        paymentDueDay: Int,
        isUnderDebtReview: Boolean,
        interestStillApplies: Boolean,
        strategy: DebtStrategy,
        notes: String
    ): Long {
        require(name.isNotBlank()) { "Debt name is required" }
        require(startingBalance >= 0.0) { "Starting balance cannot be negative" }
        require(interestRate >= 0.0) { "Interest rate cannot be negative" }
        require(minimumPayment >= 0.0) { "Minimum payment cannot be negative" }

        return debtDao.insertDebt(
            DebtEntity(
                name = name.trim(),
                debtType = debtType.name,
                startingBalance = startingBalance,
                currentBalance = startingBalance,
                interestRate = interestRate,
                minimumPayment = minimumPayment,
                paymentDueDay = paymentDueDay.coerceIn(1, 31),
                isUnderDebtReview = isUnderDebtReview,
                interestStillApplies = interestStillApplies,
                strategy = strategy.name,
                notes = notes.trim(),
                createdDate = LocalDate.now().toString()
            )
        )
    }

    suspend fun recordPayment(
        debtId: Long,
        amount: Double,
        date: String,
        paymentType: DebtPaymentType,
        notes: String = ""
    ) {
        require(amount > 0.0) { "Payment amount must be greater than zero" }
        val debt = debtDao.getDebtById(debtId) ?: error("Debt not found")
        val balanceChange = when (paymentType) {
            DebtPaymentType.INTEREST,
            DebtPaymentType.FEE -> amount
            DebtPaymentType.ADJUSTMENT -> 0.0
            DebtPaymentType.MINIMUM,
            DebtPaymentType.EXTRA,
            DebtPaymentType.SETTLEMENT -> -amount
        }
        val updatedBalance = (debt.currentBalance + balanceChange).coerceAtLeast(0.0)
        val closedDate = if (updatedBalance == 0.0 && debt.closedDate == null) date else debt.closedDate

        debtPaymentDao.insertPayment(
            DebtPaymentEntity(
                debtId = debtId,
                amount = amount,
                date = date,
                paymentType = paymentType.name,
                notes = notes.trim()
            )
        )
        debtDao.updateDebt(
            debt.copy(
                currentBalance = updatedBalance,
                closedDate = closedDate
            )
        )
    }

    suspend fun getMonthlyBattleSummary(monthId: String = DateUtils.getCurrentMonthId()): DebtBattleSummary {
        val debts = debtDao.getAllDebts()
        val yearMonth = YearMonth.parse(monthId)
        val payments = debtPaymentDao.getPaymentsForPeriod(
            yearMonth.atDay(1).toString(),
            yearMonth.atEndOfMonth().toString()
        )
        val minimum = payments
            .filter { it.paymentType == DebtPaymentType.MINIMUM.name }
            .sumOf { it.amount }
        val extra = payments
            .filter { it.paymentType == DebtPaymentType.EXTRA.name || it.paymentType == DebtPaymentType.SETTLEMENT.name }
            .sumOf { it.amount }
        val interestAndFees = payments
            .filter { it.paymentType == DebtPaymentType.INTEREST.name || it.paymentType == DebtPaymentType.FEE.name }
            .sumOf { it.amount }
        val strongestAttack = payments
            .filter { it.paymentType == DebtPaymentType.EXTRA.name || it.paymentType == DebtPaymentType.SETTLEMENT.name }
            .maxByOrNull { it.amount }
            ?.let { payment ->
                debts.firstOrNull { it.id == payment.debtId }?.name?.let { "${it}: ${payment.amount}" }
            }

        return DebtBattleSummary(
            totalStartingBalance = debts.sumOf { it.startingBalance },
            totalCurrentBalance = debts.sumOf { it.currentBalance },
            totalDamageDealt = debts.sumOf { (it.startingBalance - it.currentBalance).coerceAtLeast(0.0) },
            minimumPayments = minimum,
            extraPayments = extra,
            interestAndFees = interestAndFees,
            netDamage = minimum + extra - interestAndFees,
            strongestAttack = strongestAttack
        )
    }

    private fun DebtEntity.toBoss(): DebtBoss {
        val damageDealt = (startingBalance - currentBalance).coerceAtLeast(0.0)
        val progress = if (startingBalance > 0.0) {
            (damageDealt / startingBalance) * 100.0
        } else {
            100.0
        }
        val months = if (minimumPayment > 0.0 && currentBalance > 0.0) {
            kotlin.math.ceil(currentBalance / minimumPayment).toInt()
        } else null

        return DebtBoss(
            debtId = id,
            name = name,
            debtType = parseDebtType(debtType),
            startingBalance = startingBalance,
            currentBalance = currentBalance,
            interestRate = interestRate,
            minimumPayment = minimumPayment,
            paymentDueDay = paymentDueDay,
            isUnderDebtReview = isUnderDebtReview,
            interestStillApplies = interestStillApplies,
            strategy = parseStrategy(strategy),
            notes = notes,
            createdDate = createdDate,
            closedDate = closedDate,
            progressPercentage = progress.coerceIn(0.0, 100.0),
            damageDealt = damageDealt,
            projectedPayoffMonths = months,
            projectedPayoffLabel = projectionLabel(months),
            warning = warningFor(this)
        )
    }

    private fun projectionLabel(months: Int?): String {
        return when {
            months == null -> "Add a monthly payment to estimate defeat timing."
            months <= 1 -> "At this payment rate, this debt may be defeated within a month."
            else -> "At this payment rate, this debt may be defeated in about $months months."
        }
    }

    private fun warningFor(debt: DebtEntity): String? {
        return when {
            debt.isUnderDebtReview && debt.interestStillApplies ->
                "This debt is under debt review. Check whether interest or fees are still being added so the plan stays accurate."
            debt.minimumPayment <= 0.0 && debt.currentBalance > 0.0 ->
                "Add a minimum payment so Mini Budget can estimate how long this boss may take to defeat."
            debt.interestRate >= 20.0 ->
                "Interest is adding pressure to this debt. Consider making this boss a higher priority if possible."
            else -> null
        }
    }

    private fun parseDebtType(value: String): DebtType {
        return DebtType.values().firstOrNull { it.name == value } ?: DebtType.OTHER
    }

    private fun parseStrategy(value: String): DebtStrategy {
        return DebtStrategy.values().firstOrNull { it.name == value } ?: DebtStrategy.HYBRID
    }
}
