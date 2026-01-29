package com.example.budgettracker.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.repository.BudgetRepository

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val viewModel: DashboardViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val repository = BudgetRepository(database.monthlyBudgetDao())
        DashboardViewModelFactory(repository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Temporary hardcoded values - testing purposes
        viewModel.loadMonth(
            monthId = "2026-01",
            startingFunds = 4000.0
        )
    }

}