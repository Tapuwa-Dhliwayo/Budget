package com.example.budgettracker.ui.analytics

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.R
import com.example.budgettracker.adapters.CategorySpendingAdapter
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.ui.common.configureToolbar
import com.example.budgettracker.utils.CurrencyUtils
import kotlinx.coroutines.launch

class AnalyticsFragment : Fragment(R.layout.fragment_analytics) {

    private val viewModel: AnalyticsViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        AnalyticsViewModelFactory(
            AnalyticsRepository(
                db.expenseDao(),
                db.categoryDao(),
                db.monthlyBudgetDao()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CategorySpendingAdapter()
        val recycler: RecyclerView = view.findViewById(R.id.recycler_categories)
        val emptyView: TextView = view.findViewById(R.id.text_empty_analytics)
        val totalSpent: TextView = view.findViewById(R.id.text_total_spent)
        val budgetHealth: TextView = view.findViewById(R.id.text_budget_health)
        val topInsight: TextView = view.findViewById(R.id.text_top_insight)

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                adapter.submitList(state.categories)

                val utilization = if (state.totalBudget > 0) {
                    (state.totalSpent / state.totalBudget) * 100
                } else 0.0

                totalSpent.text = "Damage logged this month: ${CurrencyUtils.format(state.totalSpent)}"
                budgetHealth.text = when {
                    state.totalBudget <= 0 -> "Set monthly shield strength to enable pressure intel."
                    state.overBudgetCount > 0 -> "${state.overBudgetCount} pressure zones breached · ${"%.1f".format(utilization)}% used"
                    else -> "Shield stable · ${"%.1f".format(utilization)}% of monthly shield used"
                }
                budgetHealth.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        if (state.overBudgetCount > 0) R.color.ra_danger else R.color.ra_success
                    )
                )

                topInsight.text = if (state.topCategoryName != null) {
                    "Top pressure zone: ${state.topCategoryName} (${CurrencyUtils.format(state.topCategoryAmount)})"
                } else {
                    "Add spending logs to reveal your biggest pressure zone."
                }

                emptyView.visibility = if (state.categories.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        viewModel.load()
    }

    override fun onResume() {
        super.onResume()
        configureToolbar(
            title = "Damage Report",
            subtitle = "Category pressure scan and shield breaches",
            menuRes = null
        )
    }
}
