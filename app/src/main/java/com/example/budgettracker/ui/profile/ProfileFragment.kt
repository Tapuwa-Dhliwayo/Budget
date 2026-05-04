package com.example.budgettracker.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Spinner
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.repository.BudgetRepository
import com.example.budgettracker.data.repository.UserProfileRepository
import com.example.budgettracker.ui.common.configureToolbar
import com.example.budgettracker.utils.AppPreferences
import com.example.budgettracker.utils.DateUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.time.LocalDate

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        ProfileViewModelFactory(
            UserProfileRepository(db.userProfileDao())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firstNameInput: EditText = view.findViewById(R.id.edit_first_name)
        val lastNameInput: EditText = view.findViewById(R.id.edit_last_name)
        val budgetStartDayInput: EditText = view.findViewById(R.id.edit_budget_start_day)
        val themeSpinner: Spinner = view.findViewById(R.id.spinner_theme)
        val saveButton: Button = view.findViewById(R.id.btn_save_profile)
        val exitButton: Button = view.findViewById(R.id.btn_exit_app)

        val themeValues = listOf("Recovery Arcade", "Soft Recovery")
        themeSpinner.adapter = ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item_theme, themeValues)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (!state.isLoading) {
                    firstNameInput.setText(state.firstName)
                    lastNameInput.setText(state.lastName)
                    budgetStartDayInput.setText(state.budgetStartDay.toString())
                    themeSpinner.setSelection(if (state.themeKey == "soft_recovery") 1 else 0)

                    if (state.saveSuccess) {
                        Toast.makeText(
                            requireContext(),
                            "Pilot profile saved.",
                            Toast.LENGTH_SHORT
                        ).show()

                        findNavController().navigateUp()
                        viewModel.clearSaveSuccess()
                    }

                    state.error?.let { error ->
                        Toast.makeText(
                            requireContext(),
                            error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        saveButton.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()
            val lastName = lastNameInput.text.toString().trim()

            if (firstName.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Add a first name for your pilot profile.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val day = budgetStartDayInput.text.toString().toIntOrNull()?.coerceIn(1, 28) ?: 1
            val theme = if (themeSpinner.selectedItemPosition == 1) "soft_recovery" else "recovery_arcade"
            val oldDay = DateUtils.budgetStartDay
            val today = LocalDate.now()
            val oldMonthId = DateUtils.getMonthIdFor(today, oldDay)
            val newMonthId = DateUtils.getMonthIdFor(today, day)
            val applyUpdate = {
                AppPreferences.setBudgetStartDay(requireContext(), day)
                AppPreferences.setTheme(requireContext(), theme)
                DateUtils.budgetStartDay = day
                viewModel.updateProfile(firstName, lastName, day, theme)

                viewLifecycleOwner.lifecycleScope.launch {
                    val budgetRepository = BudgetRepository(AppDatabase.getInstance(requireContext()).monthlyBudgetDao())
                    budgetRepository.initializeReanchoredCycleIfMissing(oldMonthId, newMonthId)
                    requireActivity().recreate()
                }
            }

            if (oldDay != day) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Apply Budget Cycle Start Day Change?")
                    .setMessage("Changing to day $day means dates May 1–May 27 remain in Apr 28–May 27 cycle. Historical cycles are preserved and only future cycles are re-anchored.")
                    .setPositiveButton("Apply") { _, _ -> applyUpdate() }
                    .setNegativeButton("Cancel", null)
                    .show()
            } else {
                applyUpdate()
            }
        }

        exitButton.setOnClickListener {
            showExitConfirmation()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()

        configureToolbar(
            title = "Pilot Profile",
            subtitle = null,
            menuRes = null
        )
    }

    private fun showExitConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Exit App")
            .setMessage("Exit Mini Budget? Your local recovery data stays on this device.")
            .setPositiveButton("Exit") { _, _ ->
                requireActivity().finishAffinity()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
