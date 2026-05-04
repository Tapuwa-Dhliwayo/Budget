package com.example.budgettracker.utils

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

object BudgetPeriodResolver {
    private val monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    fun resolveMonthId(date: LocalDate, startDay: Int): String {
        val clamped = startDay.coerceIn(1, 28)
        val anchor = if (date.dayOfMonth >= clamped) date else date.minusMonths(1)
        return YearMonth.from(anchor).format(monthFormatter)
    }

    fun resolveRange(monthId: String, startDay: Int): Pair<LocalDate, LocalDate> {
        val clamped = startDay.coerceIn(1, 28)
        val yearMonth = YearMonth.parse(monthId, monthFormatter)
        val start = yearMonth.atDay(clamped)
        return start to start.plusMonths(1).minusDays(1)
    }

    fun resolveCurrentMonthId(today: LocalDate, startDay: Int): String {
        return resolveMonthId(today, startDay)
    }
}
