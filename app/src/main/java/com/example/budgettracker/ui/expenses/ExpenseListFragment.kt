package com.example.budgettracker.ui.expenses

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
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
import com.example.budgettracker.data.repository.UserProfileRepository
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
        super.onViewCreated(view, savedInstanceState)

        // Header components
        val headerTitle: TextView = view.findViewById(R.id.header_title)
        val headerProfileBtn: ImageButton = view.findViewById(R.id.header_profile_btn)
        val headerUserName: TextView = view.findViewById(R.id.header_user_name)

        // Expense list components
        val container: LinearLayout = view.findViewById(R.id.layout_expenses)
        val emptyText: TextView = view.findViewById(R.id.text_empty_expenses)
        val fabAddExpense: FloatingActionButton = view.findViewById(R.id.fab_add_expense)

        // Set header title
        headerTitle.text = "Expenses"

        //Navigate to profile
        headerProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_expenses_to_profile)
        }

        //Load user name for header
        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val userRepo = UserProfileRepository(db.userProfileDao())
            val user = userRepo.getOrCreateUser()
            headerUserName.text = "Hello, ${user.firstName}!"
        }

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
