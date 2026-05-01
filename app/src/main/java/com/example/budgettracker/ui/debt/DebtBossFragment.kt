package com.example.budgettracker.ui.debt

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.model.DebtBattleSummary
import com.example.budgettracker.data.model.DebtBoss
import com.example.budgettracker.data.model.DebtPaymentType
import com.example.budgettracker.data.model.DebtStrategy
import com.example.budgettracker.data.model.DebtType
import com.example.budgettracker.data.repository.DebtRepository
import com.example.budgettracker.ui.common.configureToolbar
import com.example.budgettracker.utils.CurrencyUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.launch

class DebtBossFragment : Fragment(R.layout.fragment_debt_boss) {

    private val viewModel: DebtBossViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        DebtBossViewModelFactory(
            DebtRepository(db.debtDao(), db.debtPaymentDao())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val container: LinearLayout = view.findViewById(R.id.layout_debt_bosses)
        val summaryText: TextView = view.findViewById(R.id.text_debt_summary)
        val emptyText: TextView = view.findViewById(R.id.text_empty_debts)
        val addButton: FloatingActionButton = view.findViewById(R.id.fab_add_debt)

        addButton.setOnClickListener { showAddDebtDialog() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                renderSummary(summaryText, state.battleSummary)
                container.removeViews(2, (container.childCount - 2).coerceAtLeast(0))
                emptyText.visibility = if (state.debts.isEmpty()) View.VISIBLE else View.GONE
                state.debts.forEach { debt ->
                    container.addView(createDebtCard(debt))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        configureToolbar(
            title = "Debt Bosses",
            subtitle = "Defeat debt one payment at a time",
            menuRes = null
        )
        viewModel.load()
    }

    private fun renderSummary(textView: TextView, summary: DebtBattleSummary?) {
        if (summary == null) {
            textView.text = "Debt Battle Summary\nAdd a debt boss to begin tracking progress."
            return
        }
        val strongest = summary.strongestAttack ?: "No extra attacks logged yet"
        textView.text = "Debt Battle Summary\n" +
                "Total HP: ${CurrencyUtils.format(summary.totalCurrentBalance)}\n" +
                "Damage dealt: ${CurrencyUtils.format(summary.totalDamageDealt)}\n" +
                "This month net damage: ${CurrencyUtils.format(summary.netDamage)}\n" +
                "Minimum: ${CurrencyUtils.format(summary.minimumPayments)} · Extra: ${CurrencyUtils.format(summary.extraPayments)} · Interest/fees: ${CurrencyUtils.format(summary.interestAndFees)}\n" +
                "Strongest attack: $strongest"
    }

    private fun createDebtCard(debt: DebtBoss): View {
        val card = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 16, 18, 16)
            setBackgroundResource(R.drawable.bg_expense_filter_panel)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 12, 0, 0)
            layoutParams = params
        }

        val title = TextView(requireContext()).apply {
            text = "${debt.name} · ${debt.debtType.label}"
            textSize = 18f
            setTextColor(requireContext().getColor(R.color.ink_primary))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        }
        val hp = TextView(requireContext()).apply {
            text = "Boss HP: ${CurrencyUtils.format(debt.currentBalance)} of ${CurrencyUtils.format(debt.startingBalance)}\n" +
                    "Damage dealt: ${CurrencyUtils.format(debt.damageDealt)} (${String.format("%.0f", debt.progressPercentage)}%)\n" +
                    "Minimum damage: ${CurrencyUtils.format(debt.minimumPayment)} · Strategy: ${debt.strategy.label}\n" +
                    debt.projectedPayoffLabel
            textSize = 14f
            setTextColor(requireContext().getColor(R.color.ink_secondary))
        }
        val progress = LinearProgressIndicator(requireContext()).apply {
            max = 100
            setProgress(debt.progressPercentage.toInt().coerceIn(0, 100), false)
        }
        val warning = TextView(requireContext()).apply {
            text = debt.warning.orEmpty()
            visibility = if (debt.warning == null) View.GONE else View.VISIBLE
            textSize = 13f
            setTextColor(requireContext().getColor(R.color.red_700))
        }
        val actions = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 10, 0, 0)
        }
        actions.addView(MaterialButton(requireContext()).apply {
            text = "Log Payment"
            setOnClickListener { showPaymentDialog(debt, DebtPaymentType.MINIMUM) }
        })
        actions.addView(MaterialButton(requireContext()).apply {
            text = "Extra"
            setOnClickListener { showPaymentDialog(debt, DebtPaymentType.EXTRA) }
        })
        actions.addView(MaterialButton(requireContext()).apply {
            text = "Interest/Fee"
            setOnClickListener { showPaymentDialog(debt, DebtPaymentType.INTEREST) }
        })

        card.addView(title)
        card.addView(hp)
        card.addView(progress)
        card.addView(warning)
        card.addView(actions)
        return card
    }

    private fun showAddDebtDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_debt_boss, null)
        val name = view.findViewById<EditText>(R.id.input_debt_name)
        val startingBalance = view.findViewById<EditText>(R.id.input_debt_starting_balance)
        val interestRate = view.findViewById<EditText>(R.id.input_debt_interest_rate)
        val minimumPayment = view.findViewById<EditText>(R.id.input_debt_minimum_payment)
        val dueDay = view.findViewById<EditText>(R.id.input_debt_due_day)
        val notes = view.findViewById<EditText>(R.id.input_debt_notes)
        val debtReview = view.findViewById<CheckBox>(R.id.checkbox_debt_review)
        val interestApplies = view.findViewById<CheckBox>(R.id.checkbox_interest_applies)
        val typeSpinner = view.findViewById<Spinner>(R.id.spinner_debt_type)
        val strategySpinner = view.findViewById<Spinner>(R.id.spinner_debt_strategy)

        setupSpinner(typeSpinner, DebtType.values().map { it.label })
        setupSpinner(strategySpinner, DebtStrategy.values().map { it.label })
        strategySpinner.setSelection(DebtStrategy.values().indexOf(DebtStrategy.HYBRID))

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Debt Boss")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                viewModel.addDebt(
                    name = name.text.toString(),
                    debtType = DebtType.values()[typeSpinner.selectedItemPosition],
                    startingBalance = startingBalance.text.toString().toDoubleOrNull() ?: 0.0,
                    interestRate = interestRate.text.toString().toDoubleOrNull() ?: 0.0,
                    minimumPayment = minimumPayment.text.toString().toDoubleOrNull() ?: 0.0,
                    paymentDueDay = dueDay.text.toString().toIntOrNull() ?: 1,
                    isUnderDebtReview = debtReview.isChecked,
                    interestStillApplies = interestApplies.isChecked,
                    strategy = DebtStrategy.values()[strategySpinner.selectedItemPosition],
                    notes = notes.text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showPaymentDialog(debt: DebtBoss, defaultType: DebtPaymentType) {
        val view = layoutInflater.inflate(R.layout.dialog_debt_payment, null)
        val amount = view.findViewById<EditText>(R.id.input_debt_payment_amount)
        val notes = view.findViewById<EditText>(R.id.input_debt_payment_notes)
        val typeSpinner = view.findViewById<Spinner>(R.id.spinner_debt_payment_type)
        setupSpinner(typeSpinner, DebtPaymentType.values().map { it.label })
        typeSpinner.setSelection(DebtPaymentType.values().indexOf(defaultType))

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Log action for ${debt.name}")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                viewModel.recordPayment(
                    debtId = debt.debtId,
                    amount = amount.text.toString().toDoubleOrNull() ?: 0.0,
                    paymentType = DebtPaymentType.values()[typeSpinner.selectedItemPosition],
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
