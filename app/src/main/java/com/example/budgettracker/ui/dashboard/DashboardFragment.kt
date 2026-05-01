package com.example.budgettracker.ui.dashboard

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.entity.CategoryEntity
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
                        requireContext().getColor(R.color.red_700)
                    else
                        requireContext().getColor(R.color.accent_blue)
                )

                summary.text =
                    "Spent ${com.example.budgettracker.utils.CurrencyUtils.format(state.totalSpent)} / ${com.example.budgettracker.utils.CurrencyUtils.format(state.totalBudget)}\nRemaining ${com.example.budgettracker.utils.CurrencyUtils.format(state.remaining)}"

                categoriesLayout.removeAllViews()
                if (state.topCategories.isEmpty()) {
                    emptyCategories.visibility = View.VISIBLE
                } else {
                    emptyCategories.visibility = View.GONE
                    state.topCategories.forEach {
                        val tv = TextView(requireContext())
                        tv.text = "${it.categoryIcon} ${it.categoryName}: ${com.example.budgettracker.utils.CurrencyUtils.format(it.totalSpent)}"
                        tv.textSize = 16f
                        tv.setTextColor(requireContext().getColor(R.color.ink_secondary))
                        tv.setPadding(0, 8, 0, 8)
                        categoriesLayout.addView(tv)
                    }
                }

                trendContainer.removeAllViews()
                state.dailySpendingTrend.forEach { (date, amount) ->
                    val tv = TextView(requireContext())
                    tv.text = "$date: ${com.example.budgettracker.utils.CurrencyUtils.format(amount)}"
                    tv.textSize = 14f
                    tv.setTextColor(requireContext().getColor(R.color.ink_secondary))
                    tv.setPadding(0, 4, 0, 4)
                    trendContainer.addView(tv)
                }

                categoryPieContainer.removeAllViews()
                state.categoryBreakdown.take(5).forEach { category ->
                    if (category.totalSpent > 0) {
                        val tv = TextView(requireContext())
                        tv.text =
                            "${category.categoryIcon} ${category.categoryName}: ${
                                String.format("%.1f", category.percentageUsed)
                            }%"
                        tv.textSize = 14f
                        tv.setTextColor(requireContext().getColor(R.color.ink_secondary))
                        tv.setPadding(0, 4, 0, 4)
                        categoryPieContainer.addView(tv)
                    }
                }

                comparisonText.text = state.previousMonthComparison

                state.weeklyAllowance?.let { allowance ->
                    weeklySummary.text = formatWeeklyAllowanceSummary(allowance)
                    weeklyGuidance.text = allowance.guidance
                    editWeeklyAllowanceBtn.text =
                        if (allowance.allowanceSet) "Update Allowance" else "Set Allowance"
                    weeklyGuidance.setTextColor(
                        requireContext().getColor(colorForWeeklyStatus(allowance.status))
                    )
                    weeklyPressure.text = formatCategoryPressure(allowance)
                    renderRecoveryActions(weeklyActions, allowance)
                    weeklyReview.text = formatWeeklyReview(allowance)
                    reviewWeeklyAllowanceBtn.text =
                        if (allowance.review == null) "Complete Review" else "Update Review"
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
            .setTitle("Edit Monthly Budget")
            .setMessage("Enter the total starting funds for this month")
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
            .setTitle("Weekly Review")
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
            .setTitle("Set Weekly Allowance")
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
                    setTextColor(requireContext().getColor(R.color.ink_primary))
                    textSize = 14f
                }
                val input = EditText(requireContext()).apply {
                    inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                    hint = "Weekly limit"
                    setText(currentPressures[category.id]?.weeklyLimit?.toString().orEmpty())
                }
                container.addView(label)
                container.addView(input)
                inputs.add(category to input)
            }

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Weekly Category Limits")
                .setMessage("Use these limits to spot category pressure earlier.")
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
            return "No weekly allowance set\nSpent this week: ${CurrencyUtils.format(allowance.spent)}"
        }

        val remainingText = if (allowance.remaining >= 0.0) {
            "${CurrencyUtils.format(allowance.remaining)} remaining"
        } else {
            "${CurrencyUtils.format(-allowance.remaining)} over plan"
        }

        return "$remainingText\n" +
                "${CurrencyUtils.format(allowance.spent)} spent of ${CurrencyUtils.format(allowance.allowanceAmount)}\n" +
                "${allowance.daysRemaining} days left · Safe daily spend: ${CurrencyUtils.format(allowance.safeDailySpend)}\n" +
                "Status: ${allowance.status.label}"
    }

    private fun colorForWeeklyStatus(status: WeeklyAllowanceStatus): Int {
        return when (status) {
            WeeklyAllowanceStatus.STABLE -> R.color.budget_good
            WeeklyAllowanceStatus.WATCHFUL -> R.color.budget_warning
            WeeklyAllowanceStatus.PRESSURED,
            WeeklyAllowanceStatus.CRITICAL,
            WeeklyAllowanceStatus.OVER_PLAN -> R.color.red_700
            WeeklyAllowanceStatus.NOT_SET -> R.color.ink_secondary
        }
    }

    private fun formatCategoryPressure(allowance: WeeklyAllowanceSummary): String {
        val mainPressure = allowance.categoryPressures.firstOrNull()
        return if (mainPressure == null) {
            "Category pressure: no weekly spending logged yet."
        } else {
            "Main pressure: ${mainPressure.categoryIcon} ${mainPressure.categoryName} " +
                    "at ${CurrencyUtils.format(mainPressure.amount)} " +
                    "(${String.format("%.0f", mainPressure.percentageOfWeekSpend)}% of weekly spend)."
        }
    }

    private fun renderRecoveryActions(container: LinearLayout, allowance: WeeklyAllowanceSummary) {
        container.removeAllViews()
        if (allowance.recoveryActions.isEmpty()) {
            container.addView(TextView(requireContext()).apply {
                text = "Recovery actions will appear as the week develops."
                setTextColor(requireContext().getColor(R.color.ink_secondary))
                textSize = 14f
            })
            return
        }

        allowance.recoveryActions.forEach { action ->
            container.addView(CheckBox(requireContext()).apply {
                text = action.actionText
                isChecked = action.isCompleted
                setTextColor(requireContext().getColor(R.color.ink_secondary))
                textSize = 14f
                setOnCheckedChangeListener { _, checked ->
                    viewModel.setRecoveryActionCompleted(action.actionText, checked)
                }
            })
        }
    }

    private fun formatWeeklyReview(allowance: WeeklyAllowanceSummary): String {
        val review = allowance.review ?: return "Review: not completed yet."
        return "Review: ${review.nextWeekAdjustment.ifBlank { "Saved for this week." }}"
    }

    private fun formatWeeklyHistory(history: List<WeeklyAllowanceSummary>): String {
        if (history.isEmpty()) return "No allowance history yet."
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

        toolbar.title = "Dashboard"
        toolbar.subtitle = viewModel.uiState.value.userName?.let { "Hello, $it!" }
    }
}
