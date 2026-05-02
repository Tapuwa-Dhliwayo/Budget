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
import com.example.budgettracker.ui.common.configureToolbar
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (!state.isLoading) {
                    firstNameInput.setText(state.firstName)
                    lastNameInput.setText(state.lastName)

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

            viewModel.updateProfile(firstName, lastName)
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
