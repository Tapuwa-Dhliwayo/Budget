package com.example.budgettracker.ui.summary

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.repository.AnalyticsRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.example.budgettracker.data.repository.UserProfileRepository
import com.example.budgettracker.ui.common.configureToolbar
import com.example.budgettracker.utils.CurrencyUtils
import com.example.budgettracker.utils.DateUtils
import com.example.budgettracker.utils.PercentageUtils
import kotlinx.coroutines.launch

class SummaryFragment : Fragment(R.layout.fragment_summary) {

    private val viewModel: SummaryViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val analyticsRepository = AnalyticsRepository(
            database.expenseDao(),
            database.categoryDao(),
            database.monthlyBudgetDao()
        )
        val gamificationRepository = GamificationRepository(database.gamificationDao())
        SummaryViewModelFactory(analyticsRepository, gamificationRepository)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Summary components
        val monthText: TextView = view.findViewById(R.id.text_month)
        val totalSpentText: TextView = view.findViewById(R.id.text_total_spent)
        val totalBudgetText: TextView = view.findViewById(R.id.text_total_budget)
        val remainingText: TextView = view.findViewById(R.id.text_remaining)
        val categoriesContainer: LinearLayout = view.findViewById(R.id.container_categories)
        val topCategoriesText: TextView = view.findViewById(R.id.text_top_categories)
        val streakText: TextView = view.findViewById(R.id.text_streak)
        val badgesText: TextView = view.findViewById(R.id.text_badges)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (!state.isLoading) {
                        state.monthlyOverview?.let { overview ->
                            monthText.text =
                                DateUtils.getMonthName(overview.monthId)
                            totalSpentText.text =
                                "Total Spent: ${CurrencyUtils.format(overview.totalSpent)}"
                            totalBudgetText.text =
                                "Budget: ${CurrencyUtils.format(overview.totalBudget)}"
                            remainingText.text =
                                "Remaining: ${CurrencyUtils.format(overview.remaining)}"
                        }

                        categoriesContainer.removeAllViews()
                        state.categorySummaries.forEach { summary ->
                            val categoryText = TextView(requireContext()).apply {
                                text =
                                    "${summary.categoryIcon} ${summary.categoryName}: " +
                                            "${CurrencyUtils.format(summary.amount)} " +
                                            "(${PercentageUtils.format(summary.percentage)})"
                                textSize = 16f
                                setTextColor(requireContext().getColor(R.color.ink_secondary))
                                setPadding(0, 8, 0, 8)
                            }
                            categoriesContainer.addView(categoryText)
                        }

                        val topText = state.topCategories
                            .take(3)
                            .mapIndexed { index, summary ->
                                "${index + 1}. ${summary.categoryIcon} ${summary.categoryName} - " +
                                        CurrencyUtils.format(summary.amount)
                            }
                            .joinToString("\n")

                        topCategoriesText.text = topText

                        state.gamificationStatus?.let { gamification ->
                            streakText.text =
                                "Current Streak: ${gamification.currentStreak} days 🔥"
                            badgesText.text =
                                "Badges Earned: ${gamification.badgesEarned.size}"
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Refresh the summary data when returning to this screen
        viewModel.refreshSummary()

        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val userRepo = UserProfileRepository(db.userProfileDao())
            val user = userRepo.getOrCreateUser()

            configureToolbar(
                title = "Summary",
                subtitle = "Hello, ${user.firstName}!",
                menuRes = null
            )
        }
    }
}
