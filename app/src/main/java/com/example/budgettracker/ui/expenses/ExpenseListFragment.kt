package com.example.budgettracker.ui.expenses

import android.content.res.ColorStateList
import android.graphics.Typeface
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
import com.example.budgettracker.data.repository.UserProfileRepository
import com.example.budgettracker.ui.common.configureToolbar
import com.example.budgettracker.utils.CurrencyUtils
import com.example.budgettracker.utils.DateUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class ExpenseListFragment : Fragment(R.layout.fragment_expense_list) {

    private enum class ExpenseFilter {
        ALL,
        DAY,
        WEEK,
        RECURRING
    }

    private data class ExpenseSections(
        val primaryTitle: String,
        val primaryExpenses: List<ExpenseWithCategory>,
        val recurringExpenses: List<ExpenseWithCategory> = emptyList()
    )

    private val pageSize = 8
    private val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private var currentFilter = ExpenseFilter.ALL
    private var selectedDate = LocalDate.now()
    private var currentPage = 0
    private var latestExpenses: List<ExpenseWithCategory> = emptyList()

    private val viewModel: ExpenseViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        ExpenseViewModelFactory(
            ExpenseRepository(db.expenseDao(), db.categoryDao()),
            GamificationRepository(db.gamificationDao())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Expense list components
        val container: LinearLayout = view.findViewById(R.id.layout_expenses)
        val emptyText: TextView = view.findViewById(R.id.text_empty_expenses)
        val totalText: TextView = view.findViewById(R.id.text_expense_total)
        val countText: TextView = view.findViewById(R.id.text_expense_count)
        val recurringTotalText: TextView = view.findViewById(R.id.text_recurring_total)
        val logRankText: TextView = view.findViewById(R.id.text_log_rank)
        val pagingLayout: LinearLayout = view.findViewById(R.id.layout_paging)
        val previousPageButton: MaterialButton = view.findViewById(R.id.button_previous_page)
        val nextPageButton: MaterialButton = view.findViewById(R.id.button_next_page)
        val pageText: TextView = view.findViewById(R.id.text_page)
        val periodControls: LinearLayout = view.findViewById(R.id.layout_period_controls)
        val periodButton: MaterialButton = view.findViewById(R.id.button_period)
        val previousPeriodButton: MaterialButton = view.findViewById(R.id.button_previous_period)
        val nextPeriodButton: MaterialButton = view.findViewById(R.id.button_next_period)
        val filterToggle: MaterialButtonToggleGroup = view.findViewById(R.id.toggle_expense_filter)
        val fabAddExpense: FloatingActionButton = view.findViewById(R.id.fab_add_expense)

        val render = {
            renderExpenses(
                container = container,
                emptyText = emptyText,
                totalText = totalText,
                countText = countText,
                recurringTotalText = recurringTotalText,
                logRankText = logRankText,
                pagingLayout = pagingLayout,
                previousPageButton = previousPageButton,
                nextPageButton = nextPageButton,
                pageText = pageText,
                periodControls = periodControls,
                periodButton = periodButton
            )
        }

        fabAddExpense.setOnClickListener {
            findNavController().navigate(R.id.addExpenseFragment)
        }

        filterToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            currentFilter = when (checkedId) {
                R.id.filter_day -> ExpenseFilter.DAY
                R.id.filter_week -> ExpenseFilter.WEEK
                R.id.filter_recurring -> ExpenseFilter.RECURRING
                else -> ExpenseFilter.ALL
            }
            currentPage = 0
            render()
        }

        periodButton.setOnClickListener {
            showDatePicker { date ->
                selectedDate = date
                currentPage = 0
                render()
            }
        }

        previousPeriodButton.setOnClickListener {
            selectedDate = when (currentFilter) {
                ExpenseFilter.WEEK -> selectedDate.minusWeeks(1)
                else -> selectedDate.minusDays(1)
            }
            currentPage = 0
            render()
        }

        nextPeriodButton.setOnClickListener {
            selectedDate = when (currentFilter) {
                ExpenseFilter.WEEK -> selectedDate.plusWeeks(1)
                else -> selectedDate.plusDays(1)
            }
            currentPage = 0
            render()
        }

        previousPageButton.setOnClickListener {
            if (currentPage > 0) {
                currentPage--
                render()
            }
        }

        nextPageButton.setOnClickListener {
            currentPage++
            render()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                latestExpenses = state.expenses
                render()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val userRepo = UserProfileRepository(db.userProfileDao())
            val user = userRepo.getOrCreateUser()

            configureToolbar(
                title = "Damage Log",
                subtitle = "Honest logs keep the run recoverable, ${user.firstName}.",
                menuRes = null
            )
        }
    }

    private fun renderExpenses(
        container: LinearLayout,
        emptyText: TextView,
        totalText: TextView,
        countText: TextView,
        recurringTotalText: TextView,
        logRankText: TextView,
        pagingLayout: LinearLayout,
        previousPageButton: MaterialButton,
        nextPageButton: MaterialButton,
        pageText: TextView,
        periodControls: LinearLayout,
        periodButton: MaterialButton
    ) {
        val sections = buildSections()
        val pagedPrimary = sections.primaryExpenses.drop(currentPage * pageSize).take(pageSize)
        val primaryTotalPages = maxOf(1, (sections.primaryExpenses.size + pageSize - 1) / pageSize)

        if (currentPage >= primaryTotalPages) {
            currentPage = primaryTotalPages - 1
        }

        val visibleExpenses = pagedPrimary + sections.recurringExpenses
        val scopedExpenses = sections.primaryExpenses + sections.recurringExpenses
        val recurringMonthlyTotal = latestExpenses
            .filter { it.isRecurring }
            .sumOf { it.amount }

        container.removeAllViews()
        emptyText.visibility = if (scopedExpenses.isEmpty()) View.VISIBLE else View.GONE

        totalText.text = CurrencyUtils.format(scopedExpenses.sumOf { it.amount })
        countText.text = expenseCountText(scopedExpenses.size)
        recurringTotalText.text = "${CurrencyUtils.formatCompact(recurringMonthlyTotal)} auto-damage"
        logRankText.text = logRankLabel(scopedExpenses.sumOf { it.amount })
        logRankText.setTextColor(requireContext().getColor(logRankColor(scopedExpenses.sumOf { it.amount })))
        periodControls.visibility =
            if (currentFilter == ExpenseFilter.RECURRING) View.GONE else View.VISIBLE
        periodButton.text = periodLabel()

        if (scopedExpenses.isNotEmpty()) {
            if (pagedPrimary.isNotEmpty()) {
                addSectionHeader(container, sections.primaryTitle)
                pagedPrimary.forEach { addExpenseCard(container, it) }
            }

            if (sections.recurringExpenses.isNotEmpty()) {
                addSectionHeader(container, "Recurring damage")
                sections.recurringExpenses.forEach { addExpenseCard(container, it) }
            }
        }

        val showPaging = sections.primaryExpenses.size > pageSize
        pagingLayout.visibility = if (showPaging) View.VISIBLE else View.GONE
        previousPageButton.isEnabled = currentPage > 0
        nextPageButton.isEnabled = currentPage < primaryTotalPages - 1
        pageText.text = "Page ${currentPage + 1} of $primaryTotalPages"

        if (visibleExpenses.isEmpty() && scopedExpenses.isNotEmpty()) {
            currentPage = 0
            renderExpenses(
                container,
                emptyText,
                totalText,
                countText,
                recurringTotalText,
                logRankText,
                pagingLayout,
                previousPageButton,
                nextPageButton,
                pageText,
                periodControls,
                periodButton
            )
        }
    }

    private fun buildSections(): ExpenseSections {
        val sortedExpenses = latestExpenses.sortedWith(
            compareByDescending<ExpenseWithCategory> { it.date }
                .thenByDescending { it.expenseId }
        )
        val recurring = sortedExpenses.filter { it.isRecurring }
        val oneOff = sortedExpenses.filterNot { it.isRecurring }

        return when (currentFilter) {
            ExpenseFilter.ALL -> ExpenseSections(
                primaryTitle = "Recent damage",
                primaryExpenses = oneOff,
                recurringExpenses = recurring
            )

            ExpenseFilter.DAY -> ExpenseSections(
                primaryTitle = formatExpenseDate(selectedDate.toString()),
                primaryExpenses = oneOff.filter { it.date == selectedDate.toString() }
            )

            ExpenseFilter.WEEK -> {
                val start = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val end = selectedDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                ExpenseSections(
                    primaryTitle = "${DateUtils.formatDateForDisplay(start.toString())} - ${DateUtils.formatDateForDisplay(end.toString())}",
                    primaryExpenses = oneOff.filter { expense ->
                        val date = LocalDate.parse(expense.date, isoFormatter)
                        !date.isBefore(start) && !date.isAfter(end)
                    }
                )
            }

            ExpenseFilter.RECURRING -> ExpenseSections(
                primaryTitle = "Recurring damage",
                primaryExpenses = recurring
            )
        }
    }

    private fun addSectionHeader(container: LinearLayout, title: String) {
        val header = TextView(requireContext()).apply {
            text = ">> $title"
            textSize = 13f
            setTextColor(resources.getColor(R.color.ra_primary_soft, null))
            setTypeface(typeface, Typeface.BOLD)
            setPadding(4, 18, 4, 8)
        }
        container.addView(header)
    }

    private fun addExpenseCard(container: LinearLayout, expense: ExpenseWithCategory) {
        val card = layoutInflater.inflate(
            R.layout.item_expense,
            container,
            false
        )

        val cardView = card as MaterialCardView
        val severityColor = severityColor(expense.amount)
        cardView.strokeColor = requireContext().getColor(severityColor)
        cardView.strokeWidth = if (expense.amount >= 300.0 || expense.isRecurring) dp(2) else dp(1)

        card.findViewById<TextView>(R.id.text_category_icon).text = expense.categoryIcon
        card.findViewById<TextView>(R.id.text_description).text = expense.description
        card.findViewById<TextView>(R.id.text_category).text =
            "${expense.categoryName} loadout"
        card.findViewById<TextView>(R.id.text_date).text = formatExpenseDate(expense.date).uppercase()
        card.findViewById<TextView>(R.id.text_amount).text = "-${CurrencyUtils.format(expense.amount)}"
        card.findViewById<TextView>(R.id.text_log_status).apply {
            text = severityLabel(expense.amount, expense.isRecurring)
            setTextColor(requireContext().getColor(severityColor))
            backgroundTintList = ColorStateList.valueOf(requireContext().getColor(R.color.ra_surface_elevated))
        }
        card.findViewById<TextView>(R.id.text_log_label).text =
            if (expense.isRecurring) "AUTO LOG" else "DAMAGE ENTRY"
        card.findViewById<TextView>(R.id.text_log_xp).text =
            if (expense.isRecurring) "+3 XP" else "+5 XP"
        card.findViewById<TextView>(R.id.text_recurring).visibility =
            if (expense.isRecurring) View.VISIBLE else View.GONE

        card.setOnClickListener {
            navigateToEdit(expense.expenseId)
        }

        card.setOnLongClickListener {
            confirmDelete(expense)
            true
        }

        container.addView(card)
    }

    private fun showDatePicker(onSelected: (LocalDate) -> Unit) {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select log date")
            .build()

        picker.addOnPositiveButtonClickListener { millis ->
            onSelected(LocalDate.parse(DateUtils.millisToIsoDate(millis), isoFormatter))
        }

        picker.show(parentFragmentManager, "EXPENSE_LIST_DATE_PICKER")
    }

    private fun periodLabel(): String {
        return when (currentFilter) {
            ExpenseFilter.WEEK -> {
                val start = selectedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val end = selectedDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                "${DateUtils.formatDateForDisplay(start.toString())} - ${DateUtils.formatDateForDisplay(end.toString())}"
            }
            ExpenseFilter.DAY -> formatExpenseDate(selectedDate.toString())
            ExpenseFilter.ALL -> "All damage"
            ExpenseFilter.RECURRING -> "Recurring damage"
        }
    }

    private fun formatExpenseDate(date: String): String =
        when {
            DateUtils.isToday(date) -> "Today"
            DateUtils.isYesterday(date) -> "Yesterday"
            else -> DateUtils.formatDateForDisplay(date)
        }

    private fun expenseCountText(count: Int): String =
        if (count == 1) "1 hit" else "$count hits"

    private fun severityLabel(amount: Double, recurring: Boolean): String {
        return when {
            recurring -> "AUTO-DAMAGE"
            amount >= 1000.0 -> "BOSS HIT"
            amount >= 300.0 -> "HEAVY HIT"
            amount >= 100.0 -> "MEDIUM HIT"
            else -> "LIGHT HIT"
        }
    }

    private fun severityColor(amount: Double): Int {
        return when {
            amount >= 1000.0 -> R.color.ra_danger
            amount >= 300.0 -> R.color.ra_warning
            amount >= 100.0 -> R.color.ra_accent
            else -> R.color.ra_success
        }
    }

    private fun logRankLabel(total: Double): String {
        return when {
            total <= 0.0 -> "RUN QUIET"
            total >= 5000.0 -> "CRITICAL RUN"
            total >= 1500.0 -> "HIGH PRESSURE"
            total >= 500.0 -> "WATCH ZONE"
            else -> "RUN STABLE"
        }
    }

    private fun logRankColor(total: Double): Int {
        return when {
            total >= 5000.0 -> R.color.ra_danger
            total >= 1500.0 -> R.color.ra_warning
            total >= 500.0 -> R.color.ra_accent
            else -> R.color.ra_success
        }
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
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
            .setTitle("Delete damage log?")
            .setMessage("This removes the log entry. Use it only if the entry was a mistake.")
            .setPositiveButton("Delete") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.deleteExpense(expense)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
