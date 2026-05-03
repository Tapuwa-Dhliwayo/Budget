package com.example.budgettracker.utils

import android.content.Context

object UserPreferences {
    private const val PREFS = "budget_user_prefs"
    private const val KEY_THEME = "theme_mode"
    private const val KEY_BUDGET_START_DAY = "budget_start_day"

    const val THEME_ARCADE = "arcade"
    const val THEME_SOFT = "soft"

    fun getTheme(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_THEME, THEME_ARCADE) ?: THEME_ARCADE

    fun setTheme(context: Context, theme: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY_THEME, theme).apply()
    }

    fun getBudgetStartDay(context: Context): Int =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getInt(KEY_BUDGET_START_DAY, 1)

    fun setBudgetStartDay(context: Context, day: Int) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putInt(KEY_BUDGET_START_DAY, day.coerceIn(1, 28)).apply()
    }
}
