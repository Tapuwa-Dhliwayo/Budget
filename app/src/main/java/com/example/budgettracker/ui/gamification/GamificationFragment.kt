package com.example.budgettracker.ui.gamification

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.model.Badge
import com.example.budgettracker.data.model.GamificationStatus
import com.example.budgettracker.data.model.RecoveryXpEvent
import com.example.budgettracker.data.repository.GamificationRepository
import com.example.budgettracker.ui.common.configureToolbar
import com.example.budgettracker.utils.DateUtils
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.launch

class GamificationFragment : Fragment(R.layout.fragment_gamification) {

    private val viewModel: GamificationViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        GamificationViewModelFactory(
            GamificationRepository(db.gamificationDao(), db.gamificationEventDao())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val summaryText: TextView = view.findViewById(R.id.text_recovery_progress_summary)
        val progress: LinearProgressIndicator = view.findViewById(R.id.progress_recovery_level)
        val badgesContainer: LinearLayout = view.findViewById(R.id.layout_recovery_badges)
        val eventsContainer: LinearLayout = view.findViewById(R.id.layout_recovery_events)
        val emptyEvents: TextView = view.findViewById(R.id.text_empty_recovery_events)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                state.error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
                state.status?.let { status ->
                    renderSummary(summaryText, progress, status)
                    renderBadges(badgesContainer, status.badgesEarned)
                    renderEvents(eventsContainer, emptyEvents, status.recentEvents)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        configureToolbar(
            title = "Recovery Progress",
            subtitle = "Reward recovery, not perfection",
            menuRes = null
        )
        viewModel.load()
    }

    private fun renderSummary(
        textView: TextView,
        progress: LinearProgressIndicator,
        status: GamificationStatus
    ) {
        val range = (status.nextLevelXp - status.currentLevelXp).coerceAtLeast(1)
        val progressInLevel = (status.totalXp - status.currentLevelXp).coerceAtLeast(0)
        val progressPercent = ((progressInLevel.toDouble() / range.toDouble()) * 100.0).toInt().coerceIn(0, 100)
        progress.max = 100
        progress.setProgress(progressPercent, false)
        textView.text = "Level ${status.level}: ${status.title}\n" +
                "${status.totalXp} recovery XP\n" +
                "Current logging streak: ${status.currentStreak} days · Best: ${status.longestStreak} days\n" +
                "Badges earned: ${status.badgesEarned.size}\n\n" +
                "Useful recovery actions earn XP. Difficult weeks still count when you log honestly and review what happened."
    }

    private fun renderBadges(container: LinearLayout, badges: List<Badge>) {
        container.removeAllViews()
        if (badges.isEmpty()) {
            container.addView(simpleText("No badges yet. Log honestly or complete recovery actions to earn the first one."))
            return
        }
        badges.forEach { badge ->
            container.addView(simpleText("${badge.title}\n${badge.description}"))
        }
    }

    private fun renderEvents(
        container: LinearLayout,
        emptyText: TextView,
        events: List<RecoveryXpEvent>
    ) {
        container.removeAllViews()
        emptyText.visibility = if (events.isEmpty()) View.VISIBLE else View.GONE
        events.forEach { event ->
            container.addView(
                simpleText(
                    "${event.eventType.label} · +${event.xpEarned} XP\n" +
                        "${DateUtils.formatDateForDisplay(event.occurredDate)} · ${event.message}"
                )
            )
        }
    }

    private fun simpleText(value: String): TextView {
        return TextView(requireContext()).apply {
            text = value
            textSize = 14f
            setTextColor(requireContext().getColor(R.color.ink_secondary))
            setPadding(0, 8, 0, 8)
        }
    }
}
