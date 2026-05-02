package com.example.budgettracker.ui.debt

import android.graphics.Typeface
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
import com.google.android.material.card.MaterialCardView
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
            subtitle = "Deal real damage one payment at a time",
            menuRes = null
        )
        viewModel.load()
    }

    private fun renderSummary(textView: TextView, summary: DebtBattleSummary?) {
        if (summary == null) {
            textView.text = "Boss Arena\nAdd a debt boss to begin tracking progress."
            return
        }
        val strongest = summary.strongestAttack ?: "No extra attacks logged yet"
        textView.text = "Boss Arena\n" +
                "Total HP: ${CurrencyUtils.format(summary.totalCurrentBalance)}\n" +
                "Lifetime damage: ${CurrencyUtils.format(summary.totalDamageDealt)}\n" +
                "This month: ${CurrencyUtils.format(summary.netDamage)} net damage\n" +
                "Minimum: ${CurrencyUtils.format(summary.minimumPayments)} · Extra: ${CurrencyUtils.format(summary.extraPayments)} · Interest/fees: ${CurrencyUtils.format(summary.interestAndFees)}\n" +
                "Strongest attack: $strongest"
    }

    private fun createDebtCard(debt: DebtBoss): View {
        val card = MaterialCardView(requireContext()).apply {
            radius = dp(22).toFloat()
            cardElevation = 0f
            setCardBackgroundColor(requireContext().getColor(R.color.ra_surface_card))
            strokeColor = requireContext().getColor(colorForDebt(debt))
            strokeWidth = dp(1)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, dp(12), 0, 0) }
        }

        val content = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(16), dp(18), dp(16))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        val header = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.CENTER_VERTICAL
        }
        header.addView(TextView(requireContext()).apply {
            text = debt.name
            textSize = 20f
            setTextColor(requireContext().getColor(R.color.ra_text))
            setTypeface(typeface, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        })
        header.addView(TextView(requireContext()).apply {
            text = threatLabel(debt).uppercase()
            textSize = 10f
            setTypeface(typeface, Typeface.BOLD)
            setTextColor(requireContext().getColor(colorForDebt(debt)))
            setBackgroundResource(R.drawable.ra_chip_bg)
            setPadding(dp(9), dp(4), dp(9), dp(4))
        })

        val type = TextView(requireContext()).apply {
            text = "${debt.debtType.label} · ${debt.strategy.label} strategy"
            textSize = 13f
            setTextColor(requireContext().getColor(R.color.ra_text_subtle))
            setPadding(0, dp(4), 0, 0)
        }

        val hpRow = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = android.view.Gravity.BOTTOM
            setPadding(0, dp(14), 0, 0)
        }
        hpRow.addView(TextView(requireContext()).apply {
            text = CurrencyUtils.format(debt.currentBalance)
            textSize = 28f
            setTextColor(requireContext().getColor(R.color.ra_text))
            setTypeface(typeface, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        })
        hpRow.addView(TextView(requireContext()).apply {
            text = "${String.format("%.0f", debt.progressPercentage)}% DMG"
            textSize = 14f
            setTextColor(requireContext().getColor(colorForDebt(debt)))
            setTypeface(typeface, Typeface.BOLD)
        })

        val progress = LinearProgressIndicator(requireContext()).apply {
            max = 100
            setProgress(debt.progressPercentage.toInt().coerceIn(0, 100), false)
            setIndicatorColor(requireContext().getColor(colorForDebt(debt)))
            trackColor = requireContext().getColor(R.color.ra_surface_elevated)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(0, dp(10), 0, dp(10)) }
        }

        val battleIntel = TextView(requireContext()).apply {
            text = "Starting HP: ${CurrencyUtils.format(debt.startingBalance)} · Damage dealt: ${CurrencyUtils.format(debt.damageDealt)}\n" +
                    "Minimum hit: ${CurrencyUtils.format(debt.minimumPayment)} · Due day: ${debt.paymentDueDay}\n" +
                    debt.projectedPayoffLabel
            textSize = 14f
            setTextColor(requireContext().getColor(R.color.ra_text_muted))
            setBackgroundResource(R.drawable.ra_inner_panel_bg)
            setPadding(dp(12), dp(10), dp(12), dp(10))
        }

        val warning = TextView(requireContext()).apply {
            text = debt.warning.orEmpty()
            visibility = if (debt.warning == null) View.GONE else View.VISIBLE
            textSize = 13f
            setTextColor(requireContext().getColor(R.color.ra_danger))
            setPadding(0, dp(10), 0, 0)
        }
        val actions = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, dp(12), 0, 0)
        }
        actions.addView(MaterialButton(requireContext()).apply {
            text = "Hit"
            minWidth = 0
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            setOnClickListener { showPaymentDialog(debt, DebtPaymentType.MINIMUM) }
        })
        actions.addView(MaterialButton(requireContext()).apply {
            text = "Extra"
            minWidth = 0
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                setMargins(dp(8), 0, dp(8), 0)
            }
            setOnClickListener { showPaymentDialog(debt, DebtPaymentType.EXTRA) }
        })
        actions.addView(MaterialButton(requireContext()).apply {
            text = "Regen"
            minWidth = 0
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            setOnClickListener { showPaymentDialog(debt, DebtPaymentType.INTEREST) }
        })

        content.addView(header)
        content.addView(type)
        content.addView(hpRow)
        content.addView(progress)
        content.addView(battleIntel)
        content.addView(warning)
        content.addView(actions)
        card.addView(content)
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
            .setTitle("Log boss action for ${debt.name}")
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

    private fun threatLabel(debt: DebtBoss): String {
        return when {
            debt.warning != null -> "Threat Alert"
            debt.progressPercentage >= 75.0 -> "Near Defeat"
            debt.progressPercentage >= 40.0 -> "Weakened"
            debt.progressPercentage > 0.0 -> "Engaged"
            else -> "Unscouted"
        }
    }

    private fun colorForDebt(debt: DebtBoss): Int {
        return when {
            debt.warning != null -> R.color.ra_danger
            debt.progressPercentage >= 75.0 -> R.color.ra_success
            debt.progressPercentage >= 40.0 -> R.color.ra_primary
            debt.progressPercentage > 0.0 -> R.color.ra_warning
            else -> R.color.ra_text_subtle
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}
