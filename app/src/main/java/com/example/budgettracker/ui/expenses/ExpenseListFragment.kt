package com.example.budgettracker.ui.expenses

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.R
import com.example.budgettracker.adapters.ExpenseAdapter
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.repository.ExpenseRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ExpenseListFragment : Fragment(R.layout.fragment_expense_list) {

    private val viewModel: ExpenseViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val expenseRepository = ExpenseRepository(
            database.expenseDao(),
            database.categoryDao()
        )
        val gamificationRepository = GamificationRepository(database.gamificationDao())
        ExpenseViewModelFactory(expenseRepository, gamificationRepository)
    }

    private lateinit var adapter: ExpenseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_expenses)
        val emptyText: TextView = view.findViewById(R.id.text_empty)
        val fab: FloatingActionButton = view.findViewById(R.id.fab_add_expense)

        // Setup RecyclerView
        adapter = ExpenseAdapter(
            onExpenseClick = { expense ->
                // Could navigate to edit screen
            },
            onExpenseLongClick = { expense ->
                showDeleteDialog(expense)
                true
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // FAB click
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_expenseList_to_addExpense)
        }

        // Observe expenses
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.expenses)

                    emptyText.visibility = if (state.expenses.isEmpty()) View.VISIBLE else View.GONE
                    recyclerView.visibility = if (state.expenses.isEmpty()) View.GONE else View.VISIBLE

                    state.error?.let { error ->
                        Snackbar.make(view, error, Snackbar.LENGTH_LONG).show()
                        viewModel.clearError()
                    }
                }
            }
        }
    }

    private fun showDeleteDialog(expense: com.example.budgettracker.data.model.ExpenseWithCategory) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Expense")
            .setMessage("Delete ${expense.description}?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteExpense(expense)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}