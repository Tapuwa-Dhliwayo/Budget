package com.example.budgettracker.ui.more

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.ui.common.configureToolbar
import com.google.android.material.button.MaterialButton

class MoreFragment : Fragment(R.layout.fragment_more) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<MaterialButton>(R.id.button_more_summary).setOnClickListener {
            findNavController().navigate(R.id.summaryFragment)
        }

        view.findViewById<MaterialButton>(R.id.button_more_weekly_review).setOnClickListener {
            findNavController().navigate(R.id.weeklyReviewSummaryFragment)
        }

        view.findViewById<MaterialButton>(R.id.button_more_analytics).setOnClickListener {
            findNavController().navigate(R.id.analyticsFragment)
        }

        view.findViewById<MaterialButton>(R.id.button_more_net_worth).setOnClickListener {
            findNavController().navigate(R.id.netWorthFragment)
        }

        view.findViewById<MaterialButton>(R.id.button_more_extra_income).setOnClickListener {
            findNavController().navigate(R.id.extraIncomeFragment)
        }

        view.findViewById<MaterialButton>(R.id.button_more_goals).setOnClickListener {
            findNavController().navigate(R.id.goalsFragment)
        }

        view.findViewById<MaterialButton>(R.id.button_more_recovery_progress).setOnClickListener {
            findNavController().navigate(R.id.gamificationFragment)
        }

        view.findViewById<MaterialButton>(R.id.button_more_data_ownership).setOnClickListener {
            findNavController().navigate(R.id.dataOwnershipFragment)
        }

        view.findViewById<MaterialButton>(R.id.button_more_profile).setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        configureToolbar(
            title = "Command Base",
            subtitle = "Reviews, recovery tools, and local settings",
            menuRes = null
        )
    }
}
