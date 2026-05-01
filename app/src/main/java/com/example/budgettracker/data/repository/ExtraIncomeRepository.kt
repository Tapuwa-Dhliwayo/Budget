package com.example.budgettracker.data.repository

import com.example.budgettracker.data.dao.DebtDao
import com.example.budgettracker.data.dao.ExtraIncomeDao
import com.example.budgettracker.data.entity.DebtEntity
import com.example.budgettracker.data.entity.ExtraIncomeEntity
import com.example.budgettracker.data.model.ExtraIncomeAllocationType
import com.example.budgettracker.data.model.ExtraIncomeEntry
import com.example.budgettracker.data.model.ExtraIncomeImpactSummary
import com.example.budgettracker.data.model.ExtraIncomeType
import com.example.budgettracker.utils.DateUtils

class ExtraIncomeRepository(
    private val extraIncomeDao: ExtraIncomeDao,
    private val debtDao: DebtDao
) {
    suspend fun getMonthlyImpact(monthId: String = DateUtils.getCurrentMonthId()): ExtraIncomeImpactSummary {
        val start = DateUtils.getMonthStartDate(monthId)
        val end = DateUtils.getMonthEndDate(monthId)
        val entries = extraIncomeDao.getIncomeForPeriod(start, end)
        val debtsById = debtDao.getAllDebts().associateBy { it.id }
        val models = entries.map { it.toModel(debtsById[it.linkedDebtId]) }
        val total = models.sumOf { it.amount }
        val debtRecovery = models
            .filter { it.allocationType == ExtraIncomeAllocationType.DEBT_PAYMENT }
            .sumOf { it.amount }
        val savingsAndGoals = models
            .filter {
                it.allocationType == ExtraIncomeAllocationType.EMERGENCY_FUND ||
                    it.allocationType == ExtraIncomeAllocationType.GOAL_CONTRIBUTION ||
                    it.allocationType == ExtraIncomeAllocationType.BUFFER
            }
            .sumOf { it.amount }
        val spendingPersonal = models
            .filter {
                it.allocationType == ExtraIncomeAllocationType.LIVING_EXPENSES ||
                    it.allocationType == ExtraIncomeAllocationType.PERSONAL_REWARD ||
                    it.allocationType == ExtraIncomeAllocationType.OTHER
            }
            .sumOf { it.amount }
        val unallocated = models
            .filter { it.allocationType == ExtraIncomeAllocationType.UNALLOCATED }
            .sumOf { it.amount }
        val recovery = models
            .filter { it.allocationType.isRecovery }
            .sumOf { it.amount }
        val recoveryPercentage = if (total > 0.0) (recovery / total) * 100.0 else 0.0

        return ExtraIncomeImpactSummary(
            monthId = monthId,
            totalIncome = total,
            recoveryAmount = recovery,
            debtRecoveryAmount = debtRecovery,
            savingsAndGoalsAmount = savingsAndGoals,
            spendingPersonalAmount = spendingPersonal,
            unallocatedAmount = unallocated,
            recoveryPercentage = recoveryPercentage,
            mainImpact = mainImpact(models, debtRecovery, savingsAndGoals, spendingPersonal, unallocated),
            guidance = guidanceFor(total, recovery, unallocated),
            recentEntries = models
        )
    }

    suspend fun addIncome(
        source: String,
        incomeType: ExtraIncomeType,
        amount: Double,
        dateReceived: String,
        allocationType: ExtraIncomeAllocationType,
        linkedDebtId: Long?,
        notes: String
    ): Long {
        require(source.isNotBlank()) { "Source is required" }
        require(amount > 0.0) { "Amount must be greater than zero" }
        require(
            allocationType == ExtraIncomeAllocationType.DEBT_PAYMENT || linkedDebtId == null
        ) { "Linked debt can only be used with a debt payment allocation" }

        val now = System.currentTimeMillis()
        return extraIncomeDao.insertIncome(
            ExtraIncomeEntity(
                source = source.trim(),
                incomeType = incomeType.name,
                amount = amount,
                dateReceived = dateReceived,
                allocationType = allocationType.name,
                linkedDebtId = linkedDebtId,
                notes = notes.trim(),
                createdAt = now,
                updatedAt = now
            )
        )
    }

    suspend fun getDebtOptions(): List<Pair<Long, String>> {
        return debtDao.getAllDebts()
            .filter { it.closedDate == null }
            .map { it.id to it.name }
    }

    private fun mainImpact(
        entries: List<ExtraIncomeEntry>,
        debtRecovery: Double,
        savingsAndGoals: Double,
        spendingPersonal: Double,
        unallocated: Double
    ): String {
        if (entries.isEmpty()) {
            return "No extra income recorded this month yet."
        }
        val topDebt = entries
            .filter { it.allocationType == ExtraIncomeAllocationType.DEBT_PAYMENT && it.linkedDebtName != null }
            .groupBy { it.linkedDebtName.orEmpty() }
            .mapValues { item -> item.value.sumOf { it.amount } }
            .maxByOrNull { it.value }

        return when {
            topDebt != null -> "${topDebt.key} received the strongest extra-income push."
            debtRecovery >= savingsAndGoals && debtRecovery >= spendingPersonal && debtRecovery >= unallocated ->
                "Debt recovery received the biggest share of extra income."
            savingsAndGoals >= spendingPersonal && savingsAndGoals >= unallocated ->
                "Savings, goals, or buffer received the biggest share of extra income."
            unallocated > 0.0 ->
                "Some extra income is still waiting for a clear allocation."
            else ->
                "Extra income helped cover real life this month."
        }
    }

    private fun guidanceFor(total: Double, recovery: Double, unallocated: Double): String {
        return when {
            total == 0.0 ->
                "Log bonuses, overtime, side work, refunds, or once-off money here so the impact does not disappear."
            unallocated > 0.0 ->
                "You have extra income waiting to be assigned. Giving it a job will make the impact clearer."
            recovery > 0.0 ->
                "That extra work created real movement in your recovery plan."
            else ->
                "Extra income helped this month. The trade-off is visible, and that keeps the plan honest."
        }
    }

    private fun ExtraIncomeEntity.toModel(linkedDebt: DebtEntity?): ExtraIncomeEntry {
        return ExtraIncomeEntry(
            incomeId = id,
            source = source,
            incomeType = parseIncomeType(incomeType),
            amount = amount,
            dateReceived = dateReceived,
            allocationType = parseAllocationType(allocationType),
            linkedDebtId = linkedDebtId,
            linkedDebtName = linkedDebt?.name,
            notes = notes
        )
    }

    private fun parseIncomeType(value: String): ExtraIncomeType {
        return ExtraIncomeType.values().firstOrNull { it.name == value } ?: ExtraIncomeType.OTHER
    }

    private fun parseAllocationType(value: String): ExtraIncomeAllocationType {
        return ExtraIncomeAllocationType.values().firstOrNull { it.name == value }
            ?: ExtraIncomeAllocationType.OTHER
    }
}
