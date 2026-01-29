package com.example.budgettracker.ui.expenses

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.model.ExpenseWithCategory
import com.example.budgettracker.data.repository.ExpenseRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ExpenseListFragment : Fragment(R.layout.fragment_expense_list) {

    private val viewModel: ExpenseViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        ExpenseViewModelFactory(
            ExpenseRepository(db.expenseDao(), db.categoryDao()),
            GamificationRepository(db.gamificationDao())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val container: LinearLayout =
            view.findViewById(R.id.layout_expenses)

        val emptyText: TextView =
            view.findViewById(R.id.text_empty_expenses)

        val fabAddExpense: FloatingActionButton =
            view.findViewById(R.id.fab_add_expense)

        fabAddExpense.setOnClickListener {
            findNavController().navigate(R.id.addExpenseFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                container.removeAllViews()

                if (state.expenses.isEmpty()) {
                    emptyText.visibility = View.VISIBLE
                    return@collect
                } else {
                    emptyText.visibility = View.GONE
                }

                state.expenses.forEach { expense ->
                    val card = layoutInflater.inflate(
                        R.layout.item_expense,
                        container,
                        false
                    )

                    card.findViewById<TextView>(R.id.text_description)
                        .text = expense.description

                    card.findViewById<TextView>(R.id.text_date)
                        .text = expense.date

                    card.findViewById<TextView>(R.id.text_amount)
                        .text = "-${expense.amount}"

                    // ✏️ Edit
                    card.setOnClickListener {
                        navigateToEdit(expense.expenseId)
                    }

                    // 🗑️ Delete
                    card.setOnLongClickListener {
                        confirmDelete(expense)
                        true
                    }

                    container.addView(card)
                }
            }
        }
    }

    private fun navigateToEdit(expenseId: Long) {
        val bundle = Bundle().apply {
            putLong("expenseId", expenseId)
        }
        findNavController().navigate(
            R.id.addExpenseFragment,
            bundle
        )
    }

    private fun confirmDelete(expense: ExpenseWithCategory) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete expense?")
            .setMessage("This cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.deleteExpense(expense)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}
