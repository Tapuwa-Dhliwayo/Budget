package com.example.budgettracker.utils

import android.content.Context

object AppPreferences {
    private const val PREFS = "mini_budget_preferences"
    private const val KEY_BUDGET_START_DAY = "budget_start_day"
    private const val KEY_THEME = "theme"

    fun getBudgetStartDay(context: Context): Int =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getInt(KEY_BUDGET_START_DAY, 1)
            .coerceIn(1, 28)

    fun setBudgetStartDay(context: Context, day: Int) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putInt(KEY_BUDGET_START_DAY, day.coerceIn(1, 28))
            .apply()
    }

    fun getTheme(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_THEME, "recovery_arcade") ?: "recovery_arcade"

    fun setTheme(context: Context, theme: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString(KEY_THEME, theme)
            .apply()
    }
}
