package com.example.budgettracker.ui.dashboard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.R
import com.example.budgettracker.adapters.CategorySpendingAdapter
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.data.repository.BudgetRepository
import com.example.budgettracker.utils.CurrencyUtils
import com.example.budgettracker.utils.DateUtils
import com.example.budgettracker.utils.PercentageUtils
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val viewModel: DashboardViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val budgetRepository = BudgetRepository(database.monthlyBudgetDao())
        val analyticsRepository = AnalyticsRepository(
            database.expenseDao(),
            database.categoryDao(),
            database.monthlyBudgetDao()
        )
        DashboardViewModelFactory(budgetRepository, analyticsRepository)
    }

    private lateinit var adapter: CategorySpendingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        val monthText: TextView = view.findViewById(R.id.text_month)
        val totalBudgetText: TextView = view.findViewById(R.id.text_total_budget)
        val totalSpentText: TextView = view.findViewById(R.id.text_total_spent)
        val remainingText: TextView = view.findViewById(R.id.text_remaining)
        val overallProgress: LinearProgressIndicator = view.findViewById(R.id.progress_overall)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_category_spending)

        // Setup RecyclerView
        adapter = CategorySpendingAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe UI state
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (!state.isLoading) {
                        state.monthlyOverview?.let { overview ->
                            monthText.text = DateUtils.getMonthName(overview.monthId)
                            totalBudgetText.text = "Budget: ${CurrencyUtils.format(overview.totalBudget)}"
                            totalSpentText.text = "Spent: ${CurrencyUtils.format(overview.totalSpent)}"
                            remainingText.text = "Remaining: ${CurrencyUtils.format(overview.remaining)}"

                            val progress = overview.percentageUsed.coerceIn(0.0, 100.0).toInt()
                            overallProgress.progress = progress
                        }

                        adapter.submitList(state.categorySpending)
                    }
                }
            }
        }
    }
}