package com.example.budgettracker.ui.dashboard

import android.os.Bundle
import android.view.View
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
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val viewModel: DashboardViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        DashboardViewModelFactory(
            BudgetRepository(db.monthlyBudgetDao()),
            AnalyticsRepository(db.expenseDao(), db.categoryDao(), db.monthlyBudgetDao()),
            GamificationRepository(db.gamificationDao())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val progress: CircularProgressIndicator =
            view.findViewById(R.id.progress_budget)
        val summary: TextView =
            view.findViewById(R.id.text_budget_summary)
        val categoriesLayout: LinearLayout =
            view.findViewById(R.id.layout_top_categories)
        val emptyCategories: TextView =
            view.findViewById(R.id.text_empty_categories)
        val topCategoriesHeader: TextView =
            view.findViewById(R.id.text_top_categories)

        topCategoriesHeader.setOnClickListener {
            findNavController().navigate(
                R.id.action_dashboard_to_analytics
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.isLoading) return@collect

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

                summary.text =
                    "Spent ${state.totalSpent} / ${state.totalBudget}\nRemaining ${state.remaining}"

                categoriesLayout.removeAllViews()

                if (state.topCategories.isEmpty()) {
                    emptyCategories.visibility = View.VISIBLE
                } else {
                    emptyCategories.visibility = View.GONE
                    state.topCategories.forEach {
                        val tv = TextView(requireContext())
                        tv.text = "${it.categoryIcon} ${it.categoryName}: ${it.totalSpent}"
                        categoriesLayout.addView(tv)
                    }
                }
            }
        }

        viewModel.loadDashboard()
    }

}
