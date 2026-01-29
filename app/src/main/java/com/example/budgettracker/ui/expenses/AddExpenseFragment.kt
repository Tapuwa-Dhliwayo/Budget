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
import com.example.budgettracker.data.entity.ExpenseEntity
import com.example.budgettracker.data.repository.CategoryRepository
import com.example.budgettracker.data.repository.ExpenseRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.example.budgettracker.utils.DateUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AddExpenseFragment : Fragment(R.layout.fragment_add_expense) {

    private val viewModel: ExpenseViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        ExpenseViewModelFactory(
            ExpenseRepository(database.expenseDao(), database.categoryDao()),
            GamificationRepository(database.gamificationDao())
        )
    }

    private lateinit var categories: List<CategoryEntity>

    private var editingExpenseId: Long = -1L
    private var selectedCategoryId: Long = 0L
    private var selectedDate: String = DateUtils.getCurrentDate()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editingExpenseId = arguments?.getLong("expenseId") ?: -1L

        val amountInput: TextInputEditText = view.findViewById(R.id.input_amount)
        val descriptionInput: TextInputEditText = view.findViewById(R.id.input_description)
        val categorySpinner: Spinner = view.findViewById(R.id.spinner_category)
        val dateButton: MaterialButton = view.findViewById(R.id.button_date)
        val saveButton: MaterialButton = view.findViewById(R.id.button_save)
        val cancelButton: MaterialButton = view.findViewById(R.id.button_cancel)

        // Initial date display
        dateButton.text = formatDateForButton(selectedDate)

        // 📅 Date picker
        dateButton.setOnClickListener {
            showDatePicker(dateButton)
        }

        // Load categories FIRST
        loadCategories(categorySpinner)

        // Edit mode
        if (editingExpenseId != -1L) {
            viewLifecycleOwner.lifecycleScope.launch {
                val expense = viewModel.getExpenseById(editingExpenseId)
                expense?.let {
                    amountInput.setText(it.amount.toString())
                    descriptionInput.setText(it.description)
                    selectedCategoryId = it.categoryId
                    selectedDate = it.date
                    dateButton.text = formatDateForButton(it.date)
                }
            }
        }

        // Save
        saveButton.setOnClickListener {

            val amountStr = amountInput.text.toString()
            val description = descriptionInput.text.toString()

            when {
                amountStr.isBlank() -> toast("Enter amount")
                description.isBlank() -> toast("Enter description")
                selectedCategoryId == 0L -> toast("Select category")
                else -> {
                    val amount = amountStr.toDoubleOrNull()
                    if (amount == null || amount <= 0) {
                        toast("Invalid amount")
                        return@setOnClickListener
                    }

                    viewLifecycleOwner.lifecycleScope.launch {

                        if (editingExpenseId == -1L) {
                            // ➕ ADD
                            viewModel.addExpense(
                                amount = amount,
                                description = description,
                                date = selectedDate,
                                categoryId = selectedCategoryId
                            )
                        } else {
                            // ✏️ EDIT
                            val expense = ExpenseEntity(
                                id = editingExpenseId,
                                amount = amount,
                                description = description,
                                date = selectedDate,
                                categoryId = selectedCategoryId
                            )
                            viewModel.updateExpense(expense)
                        }

                        findNavController().navigateUp()
                    }
                }
            }
        }

        cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showDatePicker(dateButton: MaterialButton) {

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select expense date")
            .build()

        picker.addOnPositiveButtonClickListener { millis ->
            val date = DateUtils.millisToIsoDate(millis)
            selectedDate = date
            dateButton.text = formatDateForButton(date)
        }

        picker.show(parentFragmentManager, "EXPENSE_DATE_PICKER")
    }

    private fun loadCategories(spinner: Spinner) {
        val database = AppDatabase.getInstance(requireContext())
        val repository = CategoryRepository(database.categoryDao())

        viewLifecycleOwner.lifecycleScope.launch {
            categories = repository.getAllCategories()

            val names = categories.map { "${it.icon} ${it.name}" }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                names
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            spinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectedCategoryId = categories[position].id
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        selectedCategoryId = 0L
                    }
                }

            // Restore selection in edit mode
            if (editingExpenseId != -1L && selectedCategoryId != 0L) {
                val index = categories.indexOfFirst { it.id == selectedCategoryId }
                if (index >= 0) spinner.setSelection(index)
            } else if (categories.isNotEmpty()) {
                selectedCategoryId = categories[0].id
            }
        }
    }

    private fun formatDateForButton(date: String): String =
        when {
            DateUtils.isToday(date) -> "Today"
            DateUtils.isYesterday(date) -> "Yesterday"
            else -> DateUtils.formatDateForDisplay(date)
        }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
