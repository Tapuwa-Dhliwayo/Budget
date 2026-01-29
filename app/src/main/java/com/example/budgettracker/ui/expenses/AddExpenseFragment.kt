package com.example.budgettracker.ui.expenses

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.entity.CategoryEntity
import com.example.budgettracker.data.repository.CategoryRepository
import com.example.budgettracker.data.repository.ExpenseRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.example.budgettracker.utils.DateUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AddExpenseFragment : Fragment(R.layout.fragment_add_expense) {

    private val viewModel: ExpenseViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val expenseRepository = ExpenseRepository(
            database.expenseDao(),
            database.categoryDao()
        )
        val gamificationRepository = GamificationRepository(database.gamificationDao())
        ExpenseViewModelFactory(expenseRepository, gamificationRepository)
    }

    private lateinit var categories: List<CategoryEntity>
    private var selectedCategoryId: Long = 0L
    private var selectedDate: String = DateUtils.getCurrentDate()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val amountInput: TextInputEditText = view.findViewById(R.id.input_amount)
        val descriptionInput: TextInputEditText = view.findViewById(R.id.input_description)
        val categorySpinner: Spinner = view.findViewById(R.id.spinner_category)
        val dateButton: MaterialButton = view.findViewById(R.id.button_date)
        val saveButton: MaterialButton = view.findViewById(R.id.button_save)
        val cancelButton: MaterialButton = view.findViewById(R.id.button_cancel)

        // Load categories
        loadCategories(categorySpinner)

        // Date selection
        dateButton.text = formatDateForButton(selectedDate)
        dateButton.setOnClickListener {
            // TODO: Show DatePickerDialog
            // For now, just use current date
        }

        // Save button
        saveButton.setOnClickListener {
            val amountStr = amountInput.text.toString()
            val description = descriptionInput.text.toString()

            when {
                amountStr.isBlank() -> {
                    Toast.makeText(requireContext(), "Enter amount", Toast.LENGTH_SHORT).show()
                }
                description.isBlank() -> {
                    Toast.makeText(requireContext(), "Enter description", Toast.LENGTH_SHORT).show()
                }
                selectedCategoryId == 0L -> {
                    Toast.makeText(requireContext(), "Select category", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val amount = amountStr.toDoubleOrNull()
                    if (amount != null && amount > 0) {
                        viewModel.addExpense(
                            amount = amount,
                            description = description,
                            date = selectedDate,
                            categoryId = selectedCategoryId
                        )
                        findNavController().navigateUp()
                    } else {
                        Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Cancel button
        cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun loadCategories(spinner: Spinner) {
        val database = AppDatabase.getInstance(requireContext())
        val repository = CategoryRepository(database.categoryDao())

        viewLifecycleOwner.lifecycleScope.launch {
            categories = repository.getAllCategories()

            val categoryNames = categories.map { "${it.icon} ${it.name}" }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedCategoryId = categories[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedCategoryId = 0L
                }
            }

            if (categories.isNotEmpty()) {
                selectedCategoryId = categories[0].id
            }
        }
    }

    private fun formatDateForButton(date: String): String {
        return when {
            DateUtils.isToday(date) -> "Today"
            DateUtils.isYesterday(date) -> "Yesterday"
            else -> DateUtils.formatDateForDisplay(date)
        }
    }
}