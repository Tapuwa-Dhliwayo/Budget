package com.example.budgettracker.utils

import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

object DateUtils {

    var budgetStartDay: Int = 1

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val displayFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    private val monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM")

    fun getCurrentDate(): String {
        return LocalDate.now().format(dateFormatter)
    }

    fun getCurrentMonthId(): String {
        return getMonthIdFor(LocalDate.now(), budgetStartDay)
    }

    fun getMonthIdFor(date: LocalDate, startDay: Int): String {
        val clampedStartDay = startDay.coerceIn(1, 28)
        val anchor = if (date.dayOfMonth >= clampedStartDay) date else date.minusMonths(1)
        return YearMonth.from(anchor).format(monthFormatter)
    }

    fun formatDateForDisplay(isoDate: String): String {
        return try {
            val date = LocalDate.parse(isoDate, dateFormatter)
            date.format(displayFormatter)
        } catch (e: Exception) {
            isoDate
        }
    }

    fun getMonthName(monthId: String): String {
        return try {
            val yearMonth = YearMonth.parse(monthId, monthFormatter)
            val month = yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
            val year = yearMonth.year
            "$month $year"
        } catch (e: Exception) {
            monthId
        }
    }

    fun getMonthStartDate(monthId: String): String {
        val yearMonth = YearMonth.parse(monthId, monthFormatter)
        return yearMonth.atDay(budgetStartDay.coerceIn(1, 28)).toString()
    }

    fun getMonthEndDate(monthId: String): String {
        val yearMonth = YearMonth.parse(monthId, monthFormatter)
        val start = yearMonth.atDay(budgetStartDay.coerceIn(1, 28))
        return start.plusMonths(1).minusDays(1).toString()
    }

    fun isToday(isoDate: String): Boolean {
        return isoDate == getCurrentDate()
    }

    fun isYesterday(isoDate: String): Boolean {
        val yesterday = LocalDate.now().minusDays(1).format(dateFormatter)
        return isoDate == yesterday
    }

    /**
     * ➕ ADDED
     * Converts epoch millis (from MaterialDatePicker)
     * into ISO date string (yyyy-MM-dd)
     *
     * SAFE:
     * - Uses java.time
     * - Respects system timezone
     * - Matches existing date format
     */
    fun millisToIsoDate(millis: Long): String {
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(dateFormatter)
    }
}

object CurrencyUtils {

    /**
     * Format amount with currency symbol (ZAR - South African Rand)
     */
    fun format(amount: Double): String {
        return String.format("R%.2f", amount)
    }

    /**
     * Format amount without decimal if it's a whole number
     */
    fun formatCompact(amount: Double): String {
        return if (amount % 1.0 == 0.0) {
            String.format("R%.0f", amount)
        } else {
            String.format("R%.2f", amount)
        }
    }

    /**
     * Parse currency string to double
     */
    fun parse(amountString: String): Double? {
        return try {
            amountString.replace("R", "").replace(",", "").trim().toDoubleOrNull()
        } catch (e: Exception) {
            null
        }
    }
}

object PercentageUtils {

    fun format(percentage: Double): String {
        return String.format("%.1f%%", percentage)
    }

    fun formatWhole(percentage: Double): String {
        return String.format("%.0f%%", percentage)
    }
}
