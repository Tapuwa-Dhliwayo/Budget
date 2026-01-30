package com.example.budgettracker.ui.summary

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
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

        //Header components
        val headerTitle: TextView = view.findViewById(R.id.header_title)
        val headerProfileBtn: ImageButton = view.findViewById(R.id.header_profile_btn)
        val headerUserName: TextView = view.findViewById(R.id.header_user_name)

        // Summary components
        val monthText: TextView = view.findViewById(R.id.text_month)
        val totalSpentText: TextView = view.findViewById(R.id.text_total_spent)
        val totalBudgetText: TextView = view.findViewById(R.id.text_total_budget)
        val remainingText: TextView = view.findViewById(R.id.text_remaining)
        val categoriesContainer: LinearLayout = view.findViewById(R.id.container_categories)
        val topCategoriesText: TextView = view.findViewById(R.id.text_top_categories)
        val streakText: TextView = view.findViewById(R.id.text_streak)
        val badgesText: TextView = view.findViewById(R.id.text_badges)

        // Set header title
        headerTitle.text = "Summary"

        //Navigate to profile
        headerProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_summary_to_profile)
        }

        //Load user name for header
        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getInstance(requireContext())
            val userRepo = UserProfileRepository(db.userProfileDao())
            val user = userRepo.getOrCreateUser()
            headerUserName.text = "Hello, ${user.firstName}!"
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (!state.isLoading) {
                        state.monthlyOverview?.let { overview ->
                            monthText.text = DateUtils.getMonthName(overview.monthId)
                            totalSpentText.text = "Total Spent: ${CurrencyUtils.format(overview.totalSpent)}"
                            totalBudgetText.text = "Budget: ${CurrencyUtils.format(overview.totalBudget)}"
                            remainingText.text = "Remaining: ${CurrencyUtils.format(overview.remaining)}"
                        }

                        // Category summaries
                        categoriesContainer.removeAllViews()
                        state.categorySummaries.forEach { summary ->
                            val categoryText = TextView(requireContext()).apply {
                                text = "${summary.categoryIcon} ${summary.categoryName}: ${CurrencyUtils.format(summary.amount)} (${PercentageUtils.format(summary.percentage)})"
                                textSize = 16f
                                setPadding(0, 8, 0, 8)
                            }
                            categoriesContainer.addView(categoryText)
                        }

                        // Top categories
                        val topText = state.topCategories.take(3).mapIndexed { index, summary ->
                            "${index + 1}. ${summary.categoryIcon} ${summary.categoryName} - ${CurrencyUtils.format(summary.amount)}"
                        }.joinToString("\n")
                        topCategoriesText.text = topText

                        // Gamification
                        state.gamificationStatus?.let { gamification ->
                            streakText.text = "Current Streak: ${gamification.currentStreak} days 🔥"
                            badgesText.text = "Badges Earned: ${gamification.badgesEarned.size}"
                        }
                    }
                }
            }
        }
    }
}
