package com.example.budgettracker.ui.dashboard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.data.repository.BudgetRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.example.budgettracker.data.repository.UserProfileRepository
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
            UserProfileRepository(db.userProfileDao())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Header components
        val headerTitle: TextView = view.findViewById(R.id.header_title)
        val headerProfileBtn: ImageButton = view.findViewById(R.id.header_profile_btn)
        val headerUserName: TextView = view.findViewById(R.id.header_user_name)

        // Dashboard components
        val monthText: TextView = view.findViewById(R.id.text_month)
        val progress: CircularProgressIndicator = view.findViewById(R.id.progress_budget)
        val summary: TextView = view.findViewById(R.id.text_budget_summary)
        val categoriesLayout: LinearLayout = view.findViewById(R.id.layout_top_categories)
        val emptyCategories: TextView = view.findViewById(R.id.text_empty_categories)
        val topCategoriesHeader: TextView = view.findViewById(R.id.text_top_categories)
        val editBudgetBtn: Button = view.findViewById(R.id.btn_edit_budget)

        // Chart components
        val trendContainer: LinearLayout = view.findViewById(R.id.layout_daily_trend)
        val categoryPieContainer: LinearLayout = view.findViewById(R.id.layout_category_pie)
        val comparisonText: TextView = view.findViewById(R.id.text_month_comparison)

        // Set header title
        headerTitle.text = "Dashboard"

        // Navigate to profile
        headerProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_profile)
        }

        topCategoriesHeader.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_analytics)
        }

        // ✅ Edit budget button
        editBudgetBtn.setOnClickListener {
            showEditBudgetDialog()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.isLoading) return@collect

                // ✅ Update header with user name
                headerUserName.text = "Hello, ${state.userName}!"

                // Month
                monthText.text = state.monthId

                // Progress (clamp 0–100)
                val progressValue = state.percentageUsed.coerceIn(0.0, 100.0)
                progress.setProgress(progressValue.toInt(), true)

                // Over-budget visual
                progress.setIndicatorColor(
                    if (state.isOverBudget)
                        requireContext().getColor(R.color.red_700)
                    else
                        requireContext().getColor(R.color.teal_700)
                )

                summary.text = "Spent ${state.totalSpent} / ${state.totalBudget}\nRemaining ${state.remaining}"

                // Top categories
                categoriesLayout.removeAllViews()
                if (state.topCategories.isEmpty()) {
                    emptyCategories.visibility = View.VISIBLE
                } else {
                    emptyCategories.visibility = View.GONE
                    state.topCategories.forEach {
                        val tv = TextView(requireContext())
                        tv.text = "${it.categoryIcon} ${it.categoryName}: ${it.totalSpent}"
                        tv.textSize = 16f
                        tv.setPadding(0, 8, 0, 8)
                        categoriesLayout.addView(tv)
                    }
                }

                // Daily spending trend
                trendContainer.removeAllViews()
                if (state.dailySpendingTrend.isNotEmpty()) {
                    state.dailySpendingTrend.forEach { (date, amount) ->
                        val tv = TextView(requireContext())
                        tv.text = "$date: R${String.format("%.2f", amount)}"
                        tv.textSize = 14f
                        tv.setPadding(0, 4, 0, 4)
                        trendContainer.addView(tv)
                    }
                }

                // Category pie chart (simplified text version)
                categoryPieContainer.removeAllViews()
                state.categoryBreakdown.take(5).forEach { category ->
                    if (category.totalSpent > 0) {
                        val tv = TextView(requireContext())
                        tv.text = "${category.categoryIcon} ${category.categoryName}: ${String.format("%.1f", category.percentageUsed)}%"
                        tv.textSize = 14f
                        tv.setPadding(0, 4, 0, 4)
                        categoryPieContainer.addView(tv)
                    }
                }

                //Month comparison
                comparisonText.text = state.previousMonthComparison
            }
        }

        viewModel.loadDashboard()
    }

    private fun showEditBudgetDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_budget, null)
        val budgetInput = dialogView.findViewById<EditText>(R.id.edit_budget_amount)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Monthly Budget")
            .setMessage("Enter the total starting funds for this month")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val budgetText = budgetInput.text.toString()
                val budget = budgetText.toDoubleOrNull()
                if (budget != null && budget > 0) {
                    val monthId = viewModel.uiState.value.monthId
                    viewModel.updateBudget(monthId, budget)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
