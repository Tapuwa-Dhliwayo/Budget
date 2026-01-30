package com.example.budgettracker.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.repository.UserProfileRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

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
        val saveButton: Button = view.findViewById(R.id.btn_save_profile)
        val exitButton: Button = view.findViewById(R.id.btn_exit_app)

        // Observe UI state
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (!state.isLoading) {
                    firstNameInput.setText(state.firstName)
                    lastNameInput.setText(state.lastName)

                    if (state.saveSuccess) {
                        Toast.makeText(
                            requireContext(),
                            "Profile updated successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.clearSaveSuccess()
                    }

                    state.error?.let { error ->
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Save profile
        saveButton.setOnClickListener {
            val firstName = firstNameInput.text.toString().trim()
            val lastName = lastNameInput.text.toString().trim()

            if (firstName.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "First name cannot be empty",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            viewModel.updateProfile(firstName, lastName)
        }

        // Exit app with confirmation
        exitButton.setOnClickListener {
            showExitConfirmation()
        }

        // Handle back button
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            }
        )
    }

    private fun showExitConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Exit App")
            .setMessage("Are you sure you want to exit MiniBudget Tracker?")
            .setPositiveButton("Exit") { _, _ ->
                requireActivity().finishAffinity()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
