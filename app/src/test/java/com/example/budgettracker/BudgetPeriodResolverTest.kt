package com.example.budgettracker

import com.example.budgettracker.utils.BudgetPeriodResolver
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class BudgetPeriodResolverTest {
    @Test
    fun requiredMonthIdCases() {
        assertEquals("2026-04", BudgetPeriodResolver.resolveMonthId(LocalDate.parse("2026-05-01"), 28))
        assertEquals("2026-05", BudgetPeriodResolver.resolveMonthId(LocalDate.parse("2026-05-28"), 28))
        assertEquals("2026-05", BudgetPeriodResolver.resolveMonthId(LocalDate.parse("2026-05-01"), 1))
        assertEquals("2025-12", BudgetPeriodResolver.resolveMonthId(LocalDate.parse("2026-01-01"), 28))
    }
}
