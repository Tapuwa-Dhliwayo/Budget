package com.example.budgettracker.ui.goals

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
import com.example.budgettracker.data.model.GoalContributionSourceType
import com.example.budgettracker.data.model.GoalPlan
import com.example.budgettracker.data.model.GoalPriority
import com.example.budgettracker.data.model.GoalStatus
import com.example.budgettracker.data.model.GoalSummary
import com.example.budgettracker.data.model.GoalType
import com.example.budgettracker.data.repository.GoalRepository
import com.example.budgettracker.ui.common.configureToolbar
import com.example.budgettracker.utils.CurrencyUtils
import com.example.budgettracker.utils.DateUtils
import com.example.budgettracker.utils.PercentageUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.launch

class GoalsFragment : Fragment(R.layout.fragment_goals) {

    private val viewModel: GoalsViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        GoalsViewModelFactory(GoalRepository(db.goalDao(), db.goalContributionDao()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val summaryText: TextView = view.findViewById(R.id.text_goals_summary)
        val activeContainer: LinearLayout = view.findViewById(R.id.layout_active_goals)
        val completedContainer: LinearLayout = view.findViewById(R.id.layout_completed_goals)
        val emptyText: TextView = view.findViewById(R.id.text_empty_goals)
        val addButton: FloatingActionButton = view.findViewById(R.id.fab_add_goal)

        addButton.setOnClickListener { showAddGoalDialog() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                state.error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
                state.summary?.let { summary ->
                    renderSummary(summaryText, summary)
                    renderGoals(activeContainer, summary.activeGoals)
                    renderGoals(completedContainer, summary.completedGoals)
                    emptyText.visibility = if (summary.activeGoals.isEmpty() && summary.completedGoals.isEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        configureToolbar(
            title = "Goals",
            subtitle = "Plan without hurting recovery",
            menuRes = null
        )
        viewModel.load()
    }

    private fun renderSummary(textView: TextView, summary: GoalSummary) {
        textView.text = "Goal Plan\n" +
                "Active goals: ${summary.activeGoals.size}\n" +
                "Saved: ${CurrencyUtils.format(summary.totalCurrentAmount)} of ${CurrencyUtils.format(summary.totalTargetAmount)}\n" +
                "Remaining: ${CurrencyUtils.format(summary.totalRemainingAmount)}\n\n" +
                summary.guidance
    }

    private fun renderGoals(container: LinearLayout, goals: List<GoalPlan>) {
        container.removeAllViews()
        goals.forEach { goal ->
            container.addView(createGoalCard(goal))
        }
    }

    private fun createGoalCard(goal: GoalPlan): View {
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
            text = "${goal.name} · ${goal.goalType.label}"
            textSize = 17f
            setTextColor(requireContext().getColor(R.color.ink_primary))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        })
        card.addView(TextView(requireContext()).apply {
            text = "${CurrencyUtils.format(goal.currentAmount)} of ${CurrencyUtils.format(goal.targetAmount)}"
            textSize = 22f
            setTextColor(requireContext().getColor(R.color.ink_primary))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        })
        card.addView(LinearProgressIndicator(requireContext()).apply {
            max = 100
            setProgress(goal.progressPercentage.toInt().coerceIn(0, 100), false)
        })
        val requiredText = goal.requiredMonthlyContribution?.let {
            "Required: ${CurrencyUtils.format(it)}/month"
        } ?: "Required: add target date"
        card.addView(TextView(requireContext()).apply {
            text = "${PercentageUtils.formatWhole(goal.progressPercentage)} complete · ${goal.feasibilityStatus.label}\n" +
                    "$requiredText · Target: ${goal.targetDate?.let { DateUtils.formatDateForDisplay(it) } ?: "No date"}\n" +
                    "Health: ${goal.healthClassification.label} · Priority: ${goal.priority.label}\n" +
                    goal.guidance
            textSize = 14f
            setTextColor(requireContext().getColor(R.color.ink_secondary))
            setPadding(0, 8, 0, 0)
        })
        if (goal.status == GoalStatus.ACTIVE) {
            card.addView(MaterialButton(requireContext()).apply {
                text = "Add contribution"
                setOnClickListener { showContributionDialog(goal) }
            })
        }
        return card
    }

    private fun showAddGoalDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_goal, null)
        val name = view.findViewById<EditText>(R.id.input_goal_name)
        val targetAmount = view.findViewById<EditText>(R.id.input_goal_target_amount)
        val currentAmount = view.findViewById<EditText>(R.id.input_goal_current_amount)
        val targetDate = view.findViewById<EditText>(R.id.input_goal_target_date)
        val monthlyTarget = view.findViewById<EditText>(R.id.input_goal_monthly_target)
        val notes = view.findViewById<EditText>(R.id.input_goal_notes)
        val typeSpinner = view.findViewById<Spinner>(R.id.spinner_goal_type)
        val prioritySpinner = view.findViewById<Spinner>(R.id.spinner_goal_priority)

        setupSpinner(typeSpinner, GoalType.values().map { it.label })
        setupSpinner(prioritySpinner, GoalPriority.values().map { it.label })
        prioritySpinner.setSelection(GoalPriority.values().indexOf(GoalPriority.MEDIUM))

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Goal")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                viewModel.addGoal(
                    name = name.text.toString(),
                    goalType = GoalType.values()[typeSpinner.selectedItemPosition],
                    targetAmount = targetAmount.text.toString().toDoubleOrNull() ?: 0.0,
                    currentAmount = currentAmount.text.toString().toDoubleOrNull() ?: 0.0,
                    targetDate = targetDate.text.toString().trim().ifBlank { null },
                    monthlyContributionTarget = monthlyTarget.text.toString().toDoubleOrNull() ?: 0.0,
                    priority = GoalPriority.values()[prioritySpinner.selectedItemPosition],
                    notes = notes.text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showContributionDialog(goal: GoalPlan) {
        val view = layoutInflater.inflate(R.layout.dialog_goal_contribution, null)
        val amount = view.findViewById<EditText>(R.id.input_goal_contribution_amount)
        val notes = view.findViewById<EditText>(R.id.input_goal_contribution_notes)
        val sourceSpinner = view.findViewById<Spinner>(R.id.spinner_goal_contribution_source)
        setupSpinner(sourceSpinner, GoalContributionSourceType.values().map { it.label })

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add contribution to ${goal.name}")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                viewModel.addContribution(
                    goalId = goal.goalId,
                    amount = amount.text.toString().toDoubleOrNull() ?: 0.0,
                    sourceType = GoalContributionSourceType.values()[sourceSpinner.selectedItemPosition],
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
