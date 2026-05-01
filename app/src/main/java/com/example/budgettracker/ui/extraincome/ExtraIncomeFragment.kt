package com.example.budgettracker.ui.extraincome

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.model.ExtraIncomeAllocationType
import com.example.budgettracker.data.model.ExtraIncomeEntry
import com.example.budgettracker.data.model.ExtraIncomeImpactSummary
import com.example.budgettracker.data.model.ExtraIncomeType
import com.example.budgettracker.data.repository.ExtraIncomeRepository
import com.example.budgettracker.ui.common.configureToolbar
import com.example.budgettracker.utils.CurrencyUtils
import com.example.budgettracker.utils.DateUtils
import com.example.budgettracker.utils.PercentageUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ExtraIncomeFragment : Fragment(R.layout.fragment_extra_income) {

    private val viewModel: ExtraIncomeViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        ExtraIncomeViewModelFactory(
            ExtraIncomeRepository(db.extraIncomeDao(), db.debtDao())
        )
    }

    private var debtOptions: List<Pair<Long, String>> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val summaryText: TextView = view.findViewById(R.id.text_extra_income_summary)
        val entriesContainer: LinearLayout = view.findViewById(R.id.layout_extra_income_entries)
        val emptyText: TextView = view.findViewById(R.id.text_empty_extra_income)
        val addButton: FloatingActionButton = view.findViewById(R.id.fab_add_extra_income)

        addButton.setOnClickListener { showAddIncomeDialog() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                debtOptions = state.debtOptions
                state.error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
                state.summary?.let { summary ->
                    renderSummary(summaryText, summary)
                    renderEntries(entriesContainer, emptyText, summary.recentEntries)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        configureToolbar(
            title = "Extra Income",
            subtitle = "See what extra effort moved forward",
            menuRes = null
        )
        viewModel.load()
    }

    private fun renderSummary(textView: TextView, summary: ExtraIncomeImpactSummary) {
        textView.text = "Extra Income This Month\n" +
                "${CurrencyUtils.format(summary.totalIncome)}\n" +
                "Recovery: ${CurrencyUtils.format(summary.recoveryAmount)} (${PercentageUtils.formatWhole(summary.recoveryPercentage)})\n" +
                "Debt: ${CurrencyUtils.format(summary.debtRecoveryAmount)} · Savings/goals: ${CurrencyUtils.format(summary.savingsAndGoalsAmount)}\n" +
                "Living/personal: ${CurrencyUtils.format(summary.spendingPersonalAmount)} · Unallocated: ${CurrencyUtils.format(summary.unallocatedAmount)}\n\n" +
                "${summary.mainImpact}\n" +
                summary.guidance
    }

    private fun renderEntries(
        container: LinearLayout,
        emptyText: TextView,
        entries: List<ExtraIncomeEntry>
    ) {
        container.removeAllViews()
        emptyText.visibility = if (entries.isEmpty()) View.VISIBLE else View.GONE
        entries.forEach { entry ->
            container.addView(createEntryCard(entry))
        }
    }

    private fun createEntryCard(entry: ExtraIncomeEntry): View {
        val card = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 16, 18, 16)
            setBackgroundResource(R.drawable.bg_expense_filter_panel)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 10, 0, 0)
            }
        }
        card.addView(TextView(requireContext()).apply {
            text = "${entry.source} · ${entry.incomeType.label}"
            textSize = 17f
            setTextColor(requireContext().getColor(R.color.ink_primary))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        })
        card.addView(TextView(requireContext()).apply {
            text = CurrencyUtils.format(entry.amount)
            textSize = 22f
            setTextColor(requireContext().getColor(R.color.ink_primary))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        })
        val debtText = entry.linkedDebtName?.let { " · $it" }.orEmpty()
        card.addView(TextView(requireContext()).apply {
            text = "${DateUtils.formatDateForDisplay(entry.dateReceived)} · ${entry.allocationType.label}$debtText"
            textSize = 14f
            setTextColor(requireContext().getColor(R.color.ink_secondary))
        })
        if (entry.notes.isNotBlank()) {
            card.addView(TextView(requireContext()).apply {
                text = entry.notes
                textSize = 14f
                setPadding(0, 8, 0, 0)
                setTextColor(requireContext().getColor(R.color.ink_secondary))
            })
        }
        return card
    }

    private fun showAddIncomeDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_extra_income, null)
        val source = view.findViewById<EditText>(R.id.input_extra_income_source)
        val amount = view.findViewById<EditText>(R.id.input_extra_income_amount)
        val notes = view.findViewById<EditText>(R.id.input_extra_income_notes)
        val typeSpinner = view.findViewById<Spinner>(R.id.spinner_extra_income_type)
        val allocationSpinner = view.findViewById<Spinner>(R.id.spinner_extra_income_allocation)
        val debtSpinner = view.findViewById<Spinner>(R.id.spinner_extra_income_debt)

        setupSpinner(typeSpinner, ExtraIncomeType.values().map { it.label })
        setupSpinner(allocationSpinner, ExtraIncomeAllocationType.values().map { it.label })
        val debtLabels = listOf("No linked debt") + debtOptions.map { it.second }
        setupSpinner(debtSpinner, debtLabels)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Extra Income")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                val allocation = ExtraIncomeAllocationType.values()[allocationSpinner.selectedItemPosition]
                val linkedDebtId = if (
                    allocation == ExtraIncomeAllocationType.DEBT_PAYMENT &&
                    debtSpinner.selectedItemPosition > 0
                ) {
                    debtOptions[debtSpinner.selectedItemPosition - 1].first
                } else {
                    null
                }
                viewModel.addIncome(
                    source = source.text.toString(),
                    incomeType = ExtraIncomeType.values()[typeSpinner.selectedItemPosition],
                    amount = amount.text.toString().toDoubleOrNull() ?: 0.0,
                    allocationType = allocation,
                    linkedDebtId = linkedDebtId,
                    notes = notes.text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupSpinner(spinner: Spinner, items: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}
