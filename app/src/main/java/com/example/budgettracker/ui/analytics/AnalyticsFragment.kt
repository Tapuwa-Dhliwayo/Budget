package com.example.budgettracker.ui.analytics

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.repository.AnalyticsRepository
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
        val layout: LinearLayout = view.findViewById(R.id.layout_categories)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                layout.removeAllViews()

                state.categories.forEach {
                    val tv = TextView(requireContext())
                    tv.text =
                        "${it.categoryIcon} ${it.categoryName} — ${it.totalSpent}/${it.budgetLimit}"
                    if (it.isOverBudget) {
                        tv.setTextColor(
                            requireContext().getColor(R.color.red_700)
                        )
                    }
                    layout.addView(tv)
                }
            }
        }

        viewModel.load()
    }
}
