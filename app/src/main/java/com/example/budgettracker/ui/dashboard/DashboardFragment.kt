package com.example.budgettracker.ui.dashboard

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.entity.CategoryEntity
import com.example.budgettracker.data.model.CategorySpending
import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.data.repository.BudgetRepository
import com.example.budgettracker.data.repository.CategoryRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.example.budgettracker.data.repository.UserProfileRepository
import com.example.budgettracker.data.repository.WeeklyAllowanceRepository
import com.example.budgettracker.data.model.WeeklyAllowanceStatus
import com.example.budgettracker.data.model.WeeklyAllowanceSummary
import com.example.budgettracker.utils.CurrencyUtils
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val viewModel: DashboardViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        DashboardViewModelFactory(
            BudgetRepository(db.monthlyBudgetDao()),
            AnalyticsRepository(db.expenseDao(), db.categoryDao(), db.monthlyBudgetDao()),
            GamificationRepository(db.gamificationDao()),
            UserProfileRepository(db.userProfileDao()),
            WeeklyAllowanceRepository(
                db.weeklyAllowanceDao(),
                db.weeklyReviewDao(),
                db.weeklyCategoryAllowanceDao(),
                db.weeklyRecoveryActionDao(),
                db.expenseDao(),
                db.categoryDao()
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dashboard components
        val monthText: TextView = view.findViewById(R.id.text_month)
        val progress: CircularProgressIndicator = view.findViewById(R.id.progress_budget)
        val summary: TextView = view.findViewById(R.id.text_budget_summary)
        val categoriesLayout: LinearLayout = view.findViewById(R.id.layout_top_categories)
        val emptyCategories: TextView = view.findViewById(R.id.text_empty_categories)
        val topCategoriesHeader: TextView = view.findViewById(R.id.text_top_categories)
        val editBudgetBtn: Button = view.findViewById(R.id.btn_edit_budget)
        val weeklySummary: TextView = view.findViewById(R.id.text_weekly_allowance_summary)
        val weeklyShieldStatus: TextView = view.findViewById(R.id.text_weekly_shield_status)
        val weeklyShieldProgress: CircularProgressIndicator = view.findViewById(R.id.progress_weekly_shield)
        val weeklyShieldPercent: TextView = view.findViewById(R.id.text_weekly_shield_percent)
        val weeklyShieldSpent: TextView = view.findViewById(R.id.text_weekly_shield_spent)
        val weeklyShieldDaily: TextView = view.findViewById(R.id.text_weekly_shield_daily)
        val weeklyShieldWindow: TextView = view.findViewById(R.id.text_weekly_shield_window)
        val weeklyGuidance: TextView = view.findViewById(R.id.text_weekly_allowance_guidance)
        val editWeeklyAllowanceBtn: Button = view.findViewById(R.id.btn_edit_weekly_allowance)
        val weeklyPressure: TextView = view.findViewById(R.id.text_weekly_category_pressure)
        val weeklyActions: LinearLayout = view.findViewById(R.id.text_weekly_recovery_actions)
        val weeklyReview: TextView = view.findViewById(R.id.text_weekly_review)
        val weeklyHistory: TextView = view.findViewById(R.id.text_weekly_history)
        val reviewWeeklyAllowanceBtn: Button = view.findViewById(R.id.btn_weekly_review)
        val weeklySummaryBtn: Button = view.findViewById(R.id.btn_weekly_summary)
        val weeklyCategoryLimitsBtn: Button = view.findViewById(R.id.btn_weekly_category_limits)

        // Chart components
        val trendContainer: LinearLayout = view.findViewById(R.id.layout_daily_trend)
        val categoryPieContainer: LinearLayout = view.findViewById(R.id.layout_category_pie)
        val comparisonText: TextView = view.findViewById(R.id.text_month_comparison)

        topCategoriesHeader.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_analytics)
        }

        editBudgetBtn.setOnClickListener {
            showEditBudgetDialog()
        }

        editWeeklyAllowanceBtn.setOnClickListener {
            showWeeklyAllowanceDialog()
        }

        reviewWeeklyAllowanceBtn.setOnClickListener {
            showWeeklyReviewDialog()
        }

        weeklySummaryBtn.setOnClickListener {
            findNavController().navigate(R.id.weeklyReviewSummaryFragment)
        }

        weeklyCategoryLimitsBtn.setOnClickListener {
            showWeeklyCategoryLimitsDialog()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.isLoading) return@collect

                monthText.text = state.monthId

                val progressValue = state.percentageUsed.coerceIn(0.0, 100.0)
                progress.setProgress(progressValue.toInt(), true)

                progress.setIndicatorColor(
                    if (state.isOverBudget)
                        requireContext().getColor(R.color.ra_danger)
                    else
                        requireContext().getColor(R.color.ra_primary)
                )

                summary.text =
                    "Shield used ${com.example.budgettracker.utils.CurrencyUtils.format(state.totalSpent)} / ${com.example.budgettracker.utils.CurrencyUtils.format(state.totalBudget)}\nSafe spend remaining ${com.example.budgettracker.utils.CurrencyUtils.format(state.remaining)}"

                categoriesLayout.removeAllViews()
                if (state.topCategories.isEmpty()) {
                    emptyCategories.visibility = View.VISIBLE
                } else {
                    emptyCategories.visibility = View.GONE
                    renderPressureZones(categoriesLayout, state.topCategories)
                }

                renderRecoveryRun(trendContainer, state.dailySpendingTrend)

                renderDamageReport(categoryPieContainer, state.categoryBreakdown)

                comparisonText.text = state.previousMonthComparison

                state.weeklyAllowance?.let { allowance ->
                    weeklySummary.text = formatWeeklyAllowanceSummary(allowance)
                    val shieldPercent = weeklyShieldPercent(allowance)
                    weeklyShieldProgress.setProgress(shieldPercent, true)
                    weeklyShieldProgress.setIndicatorColor(
                        requireContext().getColor(colorForWeeklyStatus(allowance.status))
                    )
                    weeklyShieldPercent.text = "$shieldPercent%"
                    weeklyShieldPercent.setTextColor(
                        requireContext().getColor(colorForWeeklyStatus(allowance.status))
                    )
                    weeklyShieldStatus.text = formatWeeklyShieldStatus(allowance)
                    weeklyShieldStatus.setTextColor(
                        requireContext().getColor(colorForWeeklyStatus(allowance.status))
                    )
                    weeklyShieldSpent.text = formatWeeklyShieldSpent(allowance)
                    weeklyShieldDaily.text = formatWeeklyShieldDaily(allowance)
                    weeklyShieldWindow.text = formatWeeklyShieldWindow(allowance)
                    weeklyGuidance.text = allowance.guidance
                    editWeeklyAllowanceBtn.text =
                        if (allowance.allowanceSet) "Update Shield" else "Set Shield"
                    weeklyGuidance.setTextColor(
                        requireContext().getColor(colorForWeeklyStatus(allowance.status))
                    )
                    weeklyPressure.text = formatCategoryPressure(allowance)
                    renderRecoveryActions(weeklyActions, allowance)
                    weeklyReview.text = formatWeeklyReview(allowance)
                    reviewWeeklyAllowanceBtn.text =
                        if (allowance.review == null) "Complete Debrief" else "Update Debrief"
                }

                weeklyHistory.text = formatWeeklyHistory(state.weeklyAllowanceHistory)
            }
        }

        viewModel.loadDashboard()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showEditBudgetDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_budget, null)
        val budgetInput = dialogView.findViewById<EditText>(R.id.edit_budget_amount)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Tune Monthly Loadout")
            .setMessage("Tune the starting funds that power this month’s recovery run.")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val budget = budgetInput.text.toString().toDoubleOrNull()
                if (budget != null && budget > 0) {
                    viewModel.updateBudget(
                        viewModel.uiState.value.monthId,
                        budget
                    )
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showWeeklyReviewDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_weekly_review, null)
        val wentWellInput = dialogView.findViewById<EditText>(R.id.edit_went_well)
        val challengeInput = dialogView.findViewById<EditText>(R.id.edit_challenge)
        val adjustmentInput = dialogView.findViewById<EditText>(R.id.edit_next_adjustment)

        viewModel.uiState.value.weeklyAllowance?.review?.let { review ->
            wentWellInput.setText(review.wentWell)
            challengeInput.setText(review.challenge)
            adjustmentInput.setText(review.nextWeekAdjustment)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Weekly Debrief")
            .setMessage("Use this to learn from the week without judging yourself.")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                viewModel.saveWeeklyReview(
                    wentWell = wentWellInput.text.toString(),
                    challenge = challengeInput.text.toString(),
                    nextWeekAdjustment = adjustmentInput.text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showWeeklyAllowanceDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_weekly_allowance, null)
        val allowanceInput = dialogView.findViewById<EditText>(R.id.edit_weekly_allowance)
        viewModel.uiState.value.weeklyAllowance
            ?.takeIf { it.allowanceSet }
            ?.let { allowanceInput.setText(it.allowanceAmount.toString()) }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Set Safe Spend Shield")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val allowance = allowanceInput.text.toString().toDoubleOrNull()
                if (allowance != null && allowance >= 0.0) {
                    viewModel.updateWeeklyAllowance(allowance)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showWeeklyCategoryLimitsDialog() {
        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val categories = CategoryRepository(db.categoryDao()).getAllCategories()
            val currentPressures = viewModel.uiState.value.weeklyAllowance
                ?.categoryPressures
                ?.associateBy { it.categoryId }
                ?: emptyMap()

            val container = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 12, 24, 0)
            }
            val inputs = mutableListOf<Pair<CategoryEntity, EditText>>()
            categories.forEach { category ->
                val label = TextView(requireContext()).apply {
                    text = "${category.icon} ${category.name}"
                    setTextColor(requireContext().getColor(R.color.ra_text))
                    textSize = 14f
                }
                val input = EditText(requireContext()).apply {
                    inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                    hint = "Weekly damage cap"
                    setText(currentPressures[category.id]?.weeklyLimit?.toString().orEmpty())
                }
                container.addView(label)
                container.addView(input)
                inputs.add(category to input)
            }

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Weekly Damage Caps")
                .setMessage("Use these caps to spot category pressure earlier.")
                .setView(container)
                .setPositiveButton("Save") { _, _ ->
                    inputs.forEach { (category, input) ->
                        val value = input.text.toString().toDoubleOrNull()
                        if (value != null && value >= 0.0) {
                            viewModel.updateWeeklyCategoryLimit(category.id, value)
                        }
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun formatWeeklyAllowanceSummary(allowance: WeeklyAllowanceSummary): String {
        if (!allowance.allowanceSet) {
            return CurrencyUtils.format(allowance.spent)
        }

        return if (allowance.remaining >= 0.0) {
            CurrencyUtils.format(allowance.remaining)
        } else {
            "-${CurrencyUtils.format(-allowance.remaining)}"
        }
    }

    private fun weeklyShieldPercent(allowance: WeeklyAllowanceSummary): Int {
        if (!allowance.allowanceSet || allowance.allowanceAmount <= 0.0) return 0
        val remainingRatio = allowance.remaining.coerceAtLeast(0.0) / allowance.allowanceAmount
        return (remainingRatio * 100.0).toInt().coerceIn(0, 100)
    }

    private fun formatWeeklyShieldStatus(allowance: WeeklyAllowanceSummary): String {
        return if (!allowance.allowanceSet) {
            "SHIELD NOT SET"
        } else {
            allowance.status.label.uppercase()
        }
    }

    private fun formatWeeklyShieldSpent(allowance: WeeklyAllowanceSummary): String {
        return if (!allowance.allowanceSet) {
            "Set a shield to unlock weekly spend protection."
        } else {
            "${CurrencyUtils.format(allowance.spent)} damage absorbed of ${CurrencyUtils.format(allowance.allowanceAmount)}"
        }
    }

    private fun formatWeeklyShieldDaily(allowance: WeeklyAllowanceSummary): String {
        return if (!allowance.allowanceSet) {
            "Logged this week: ${CurrencyUtils.format(allowance.spent)}"
        } else {
            "Safe daily capacity: ${CurrencyUtils.format(allowance.safeDailySpend)}"
        }
    }

    private fun formatWeeklyShieldWindow(allowance: WeeklyAllowanceSummary): String {
        return "Run window: ${allowance.weekStartDate} to ${allowance.weekEndDate} · ${allowance.daysRemaining} days left"
    }

    private fun colorForWeeklyStatus(status: WeeklyAllowanceStatus): Int {
        return when (status) {
            WeeklyAllowanceStatus.STABLE -> R.color.ra_success
            WeeklyAllowanceStatus.WATCHFUL -> R.color.ra_warning
            WeeklyAllowanceStatus.PRESSURED,
            WeeklyAllowanceStatus.CRITICAL,
            WeeklyAllowanceStatus.OVER_PLAN -> R.color.ra_danger
            WeeklyAllowanceStatus.NOT_SET -> R.color.ra_text_muted
        }
    }

    private fun renderRecoveryRun(
        container: LinearLayout,
        trend: List<Pair<String, Double>>
    ) {
        container.removeAllViews()
        val visibleTrend = trend.takeLast(7)
        if (visibleTrend.isEmpty()) {
            container.addView(arcadePanel("No run data yet. Log a spend to light up the recovery graph."))
            return
        }

        val maxAmount = visibleTrend.maxOf { it.second }.coerceAtLeast(1.0)
        val graph = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.BOTTOM
            setPadding(dp(12), dp(12), dp(12), dp(10))
            setBackgroundResource(R.drawable.ra_inner_panel_bg)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        visibleTrend.forEach { (date, amount) ->
            val percent = (amount / maxAmount).coerceIn(0.0, 1.0)
            val barHeight = (dp(88) * percent).toInt().coerceAtLeast(dp(8))
            val color = when {
                percent >= 0.8 -> R.color.ra_danger
                percent >= 0.55 -> R.color.ra_warning
                else -> R.color.ra_primary
            }

            val column = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                gravity = android.view.Gravity.BOTTOM or android.view.Gravity.CENTER_HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(0, dp(138), 1f).apply {
                    setMargins(dp(3), 0, dp(3), 0)
                }
            }
            val track = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                gravity = android.view.Gravity.BOTTOM
                setBackgroundColor(requireContext().getColor(R.color.ra_surface_elevated))
                layoutParams = LinearLayout.LayoutParams(dp(18), dp(92))
            }
            val bar = View(requireContext()).apply {
                setBackgroundColor(requireContext().getColor(color))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    barHeight
                )
            }
            track.addView(bar)
            column.addView(track)
            column.addView(TextView(requireContext()).apply {
                text = date.takeLast(5)
                setTextColor(requireContext().getColor(R.color.ra_text_subtle))
                textSize = 10f
                gravity = android.view.Gravity.CENTER
                setPadding(0, dp(6), 0, 0)
            })
            column.addView(TextView(requireContext()).apply {
                text = CurrencyUtils.formatCompact(amount)
                setTextColor(requireContext().getColor(R.color.ra_text_muted))
                textSize = 10f
                gravity = android.view.Gravity.CENTER
            })
            graph.addView(column)
        }

        container.addView(graph)
    }

    private fun renderDamageReport(
        container: LinearLayout,
        categories: List<CategorySpending>
    ) {
        container.removeAllViews()
        val activeCategories = categories.filter { it.totalSpent > 0 }.take(5)
        if (activeCategories.isEmpty()) {
            container.addView(arcadePanel("No category damage yet. The report activates after spending logs are added."))
            return
        }

        activeCategories.forEach { category ->
            val percent = category.percentageUsed.toInt().coerceIn(0, 100)
            val color = colorForCategoryDamage(category)
            val card = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(14), dp(12), dp(14), dp(12))
                setBackgroundResource(R.drawable.ra_inner_panel_bg)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, dp(10)) }
            }

            val row = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_VERTICAL
            }
            row.addView(TextView(requireContext()).apply {
                text = "${category.categoryIcon} ${category.categoryName}"
                setTextColor(requireContext().getColor(R.color.ra_text))
                textSize = 15f
                setTypeface(typeface, Typeface.BOLD)
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            })
            row.addView(TextView(requireContext()).apply {
                text = damageStatusLabel(category)
                setTextColor(requireContext().getColor(color))
                textSize = 10f
                setTypeface(typeface, Typeface.BOLD)
            })
            card.addView(row)
            card.addView(ProgressBar(requireContext(), null, android.R.attr.progressBarStyleHorizontal).apply {
                max = 100
                progress = percent
                progressTintList = ColorStateList.valueOf(requireContext().getColor(color))
                progressBackgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.ra_surface_elevated))
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp(8)
                ).apply { setMargins(0, dp(10), 0, dp(8)) }
            })
            card.addView(TextView(requireContext()).apply {
                text = "${CurrencyUtils.format(category.totalSpent)} damage · ${percent}% shield use · ${remainingCategoryText(category)}"
                setTextColor(requireContext().getColor(R.color.ra_text_muted))
                textSize = 12f
            })
            container.addView(card)
        }
    }

    private fun renderPressureZones(
        container: LinearLayout,
        categories: List<CategorySpending>
    ) {
        container.removeAllViews()
        categories.take(3).forEachIndexed { index, category ->
            val color = colorForCategoryDamage(category)
            val card = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_VERTICAL
                setPadding(dp(14), dp(12), dp(14), dp(12))
                setBackgroundResource(R.drawable.ra_inner_panel_bg)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, dp(10)) }
            }
            card.addView(TextView(requireContext()).apply {
                text = "#${index + 1}"
                setTextColor(requireContext().getColor(color))
                textSize = 18f
                setTypeface(typeface, Typeface.BOLD)
                gravity = android.view.Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(dp(46), LinearLayout.LayoutParams.WRAP_CONTENT)
            })
            card.addView(TextView(requireContext()).apply {
                text = "${category.categoryIcon} ${category.categoryName}\n${pressureZoneMessage(category)}"
                setTextColor(requireContext().getColor(R.color.ra_text_muted))
                textSize = 13f
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            })
            card.addView(TextView(requireContext()).apply {
                text = CurrencyUtils.formatCompact(category.totalSpent)
                setTextColor(requireContext().getColor(color))
                textSize = 15f
                setTypeface(typeface, Typeface.BOLD)
            })
            container.addView(card)
        }
    }

    private fun colorForCategoryDamage(category: CategorySpending): Int {
        return when {
            category.isOverBudget -> R.color.ra_danger
            category.percentageUsed >= 80.0 -> R.color.ra_warning
            else -> R.color.ra_success
        }
    }

    private fun damageStatusLabel(category: CategorySpending): String {
        return when {
            category.isOverBudget -> "CRITICAL DAMAGE"
            category.percentageUsed >= 80.0 -> "WATCH CLOSELY"
            else -> "SHIELD STABLE"
        }
    }

    private fun remainingCategoryText(category: CategorySpending): String {
        return if (category.remaining >= 0.0) {
            "${CurrencyUtils.format(category.remaining)} shield left"
        } else {
            "${CurrencyUtils.format(-category.remaining)} breach"
        }
    }

    private fun pressureZoneMessage(category: CategorySpending): String {
        return when {
            category.isOverBudget -> "Critical breach. A recovery action here gives the fastest shield repair."
            category.percentageUsed >= 80.0 -> "Watch closely. Small choices here can keep the run stable."
            else -> "Stable, but still one of the biggest drains this month."
        }
    }

    private fun arcadePanel(message: String): TextView {
        return TextView(requireContext()).apply {
            text = message
            setTextColor(requireContext().getColor(R.color.ra_text_muted))
            textSize = 14f
            setPadding(dp(14), dp(12), dp(14), dp(12))
            setBackgroundResource(R.drawable.ra_inner_panel_bg)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    private fun formatCategoryPressure(allowance: WeeklyAllowanceSummary): String {
        val mainPressure = allowance.categoryPressures.firstOrNull()
        return if (mainPressure == null) {
            "Damage report: no weekly spending logged yet."
        } else {
            "Main pressure zone: ${mainPressure.categoryIcon} ${mainPressure.categoryName} " +
                    "at ${CurrencyUtils.format(mainPressure.amount)} " +
                    "(${String.format("%.0f", mainPressure.percentageOfWeekSpend)}% of weekly spend)."
        }
    }

    private fun renderRecoveryActions(container: LinearLayout, allowance: WeeklyAllowanceSummary) {
        container.removeAllViews()
        if (allowance.recoveryActions.isEmpty()) {
            container.addView(TextView(requireContext()).apply {
                text = "Recovery actions will unlock as the week develops."
                setTextColor(requireContext().getColor(R.color.ra_text_muted))
                textSize = 14f
            })
            return
        }

        allowance.recoveryActions.forEach { action ->
            container.addView(CheckBox(requireContext()).apply {
                text = action.actionText
                isChecked = action.isCompleted
                setTextColor(requireContext().getColor(R.color.ra_text_muted))
                textSize = 14f
                setOnCheckedChangeListener { _, checked ->
                    viewModel.setRecoveryActionCompleted(action.actionText, checked)
                }
            })
        }
    }

    private fun formatWeeklyReview(allowance: WeeklyAllowanceSummary): String {
        val review = allowance.review ?: return "Debrief: not completed yet."
        return "Debrief: ${review.nextWeekAdjustment.ifBlank { "Saved for this week." }}"
    }

    private fun formatWeeklyHistory(history: List<WeeklyAllowanceSummary>): String {
        if (history.isEmpty()) return "No shield history yet."
        return history.joinToString(separator = "\n") { week ->
            val result = if (week.remaining >= 0.0) {
                "${CurrencyUtils.format(week.remaining)} left"
            } else {
                "${CurrencyUtils.format(-week.remaining)} over"
            }
            "${week.weekStartDate}: ${week.status.label}, $result"
        }
    }

    override fun onResume() {
        super.onResume()

        val toolbar = requireActivity()
            .findViewById<MaterialToolbar>(R.id.topAppBar)

        toolbar.title = "Recovery HUD"
        toolbar.subtitle = viewModel.uiState.value.userName?.let { "Hello, $it!" }
    }
}
