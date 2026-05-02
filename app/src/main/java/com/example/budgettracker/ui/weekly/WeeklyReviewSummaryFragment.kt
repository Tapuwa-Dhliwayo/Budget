package com.example.budgettracker.ui.weekly

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.content.res.ColorStateList
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.model.WeeklyAllowanceSummary
import com.example.budgettracker.data.repository.WeeklyAllowanceRepository
import com.example.budgettracker.ui.common.configureToolbar
import com.example.budgettracker.utils.CurrencyUtils
import kotlinx.coroutines.launch

class WeeklyReviewSummaryFragment : Fragment(R.layout.fragment_weekly_review_summary) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val overview: TextView = view.findViewById(R.id.text_weekly_review_overview)
        val notes: TextView = view.findViewById(R.id.text_weekly_review_notes)
        val categories: TextView = view.findViewById(R.id.text_weekly_review_categories)
        val actions: TextView = view.findViewById(R.id.text_weekly_review_actions)
        val trend: LinearLayout = view.findViewById(R.id.layout_weekly_trend)

        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val repository = WeeklyAllowanceRepository(
                db.weeklyAllowanceDao(),
                db.weeklyReviewDao(),
                db.weeklyCategoryAllowanceDao(),
                db.weeklyRecoveryActionDao(),
                db.expenseDao(),
                db.categoryDao()
            )
            val current = repository.getCurrentWeekSummary()
            val history = repository.getRecentWeekSummaries(limit = 8)

            overview.text = formatOverview(current)
            stylePanel(overview)
            notes.text = formatNotes(current)
            stylePanel(notes)
            categories.text = formatCategories(current)
            stylePanel(categories)
            actions.text = formatActions(current)
            stylePanel(actions)
            renderTrend(trend, history)
        }
    }

    override fun onResume() {
        super.onResume()
        configureToolbar(
            title = "Weekly Debrief",
            subtitle = "Shield pressure, recovery actions, and history",
            menuRes = null
        )
    }

    private fun formatOverview(summary: WeeklyAllowanceSummary): String {
        val result = if (summary.remaining >= 0.0) {
            "${CurrencyUtils.format(summary.remaining)} shield strength"
        } else {
            "${CurrencyUtils.format(-summary.remaining)} shield breach"
        }
        return "Week ${summary.weekStartDate} to ${summary.weekEndDate}\n" +
                "${CurrencyUtils.format(summary.spent)} damage of ${CurrencyUtils.format(summary.allowanceAmount)} shield\n" +
                "$result\nStatus: ${summary.status.label}"
    }

    private fun formatNotes(summary: WeeklyAllowanceSummary): String {
        val review = summary.review ?: return "Debrief notes: not completed yet."
        return "What went well: ${review.wentWell.ifBlank { "Not captured" }}\n" +
                "Challenge: ${review.challenge.ifBlank { "Not captured" }}\n" +
                "Next adjustment: ${review.nextWeekAdjustment.ifBlank { "Not captured" }}"
    }

    private fun formatCategories(summary: WeeklyAllowanceSummary): String {
        if (summary.categoryPressures.isEmpty()) return "Pressure zones: no weekly spending yet."
        return summary.categoryPressures.joinToString(separator = "\n") { pressure ->
            val limit = pressure.weeklyLimit?.let {
                " / ${CurrencyUtils.format(it)} cap"
            }.orEmpty()
            val status = if (pressure.isOverLimit) " over cap" else ""
            "${pressure.categoryIcon} ${pressure.categoryName}: ${CurrencyUtils.format(pressure.amount)}$limit$status"
        }
    }

    private fun formatActions(summary: WeeklyAllowanceSummary): String {
        if (summary.recoveryActions.isEmpty()) return "Recovery actions: none yet."
        val completed = summary.recoveryActions.count { it.isCompleted }
        return "Recovery actions: $completed of ${summary.recoveryActions.size} completed\n" +
                summary.recoveryActions.joinToString(separator = "\n") {
                    val marker = if (it.isCompleted) "[x]" else "[ ]"
                    "$marker ${it.actionText}"
                }
    }

    private fun renderTrend(container: LinearLayout, history: List<WeeklyAllowanceSummary>) {
        container.removeAllViews()
        if (history.isEmpty()) {
            container.addView(TextView(requireContext()).apply {
                text = "No weekly history yet."
                setTextColor(requireContext().getColor(R.color.ra_text_muted))
                setBackgroundResource(R.drawable.ra_inner_panel_bg)
                setPadding(dp(14), dp(10), dp(14), dp(10))
            })
            return
        }

        history.forEach { week ->
            val percent = if (week.allowanceAmount > 0.0) {
                ((week.spent / week.allowanceAmount) * 100).toInt().coerceIn(0, 150)
            } else 0
            container.addView(TextView(requireContext()).apply {
                text = "${week.weekStartDate}: ${CurrencyUtils.format(week.spent)} of ${CurrencyUtils.format(week.allowanceAmount)} (${week.status.label})"
                setTextColor(requireContext().getColor(R.color.ra_text_muted))
                textSize = 13f
                setBackgroundResource(R.drawable.ra_inner_panel_bg)
                setPadding(dp(12), dp(8), dp(12), dp(8))
            })
            container.addView(ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal).apply {
                max = 150
                progress = percent
                progressTintList = ColorStateList.valueOf(requireContext().getColor(colorForPercent(percent)))
                progressBackgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.ra_surface_elevated))
            })
        }
    }

    private fun stylePanel(textView: TextView) {
        textView.setBackgroundResource(R.drawable.ra_inner_panel_bg)
        textView.setPadding(dp(14), dp(10), dp(14), dp(10))
    }

    private fun colorForPercent(percent: Int): Int {
        return when {
            percent >= 100 -> R.color.ra_danger
            percent >= 80 -> R.color.ra_warning
            else -> R.color.ra_success
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}
