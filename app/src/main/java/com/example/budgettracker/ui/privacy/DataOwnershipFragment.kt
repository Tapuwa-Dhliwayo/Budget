package com.example.budgettracker.ui.privacy

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.R
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.model.BackupStatus
import com.example.budgettracker.data.repository.AndroidBackupStore
import com.example.budgettracker.data.repository.BackupRepository
import com.example.budgettracker.data.repository.GamificationRepository
import com.example.budgettracker.ui.common.configureToolbar
import com.example.budgettracker.utils.DateUtils
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class DataOwnershipFragment : Fragment(R.layout.fragment_data_ownership) {

    private val viewModel: DataOwnershipViewModel by viewModels {
        val db = AppDatabase.getInstance(requireContext())
        DataOwnershipViewModelFactory(
            backupRepository = BackupRepository(AndroidBackupStore(requireContext(), db)),
            gamificationRepository = GamificationRepository(db.gamificationDao(), db.gamificationEventDao())
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val summaryText: TextView = view.findViewById(R.id.text_data_ownership_summary)
        val backupText: TextView = view.findViewById(R.id.text_backup_status)
        val createBackupButton: MaterialButton = view.findViewById(R.id.button_create_local_backup)

        createBackupButton.setOnClickListener { viewModel.createBackup() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                state.error?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
                state.message?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }
                renderSummary(summaryText)
                state.backupStatus?.let { renderBackupStatus(backupText, it) }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        configureToolbar(
            title = "Data Ownership",
            subtitle = "Local-first and private",
            menuRes = null
        )
        viewModel.load()
    }

    private fun renderSummary(textView: TextView) {
        textView.text = "Your financial recovery data stays on this device.\n\n" +
                "Mini Budget does not require an account, internet connection, bank sync, or cloud service for core features."
    }

    private fun renderBackupStatus(textView: TextView, status: BackupStatus) {
        val lastBackup = status.lastBackupDate?.let { DateUtils.formatDateForDisplay(it) } ?: "No backup yet"
        val days = status.daysSinceBackup?.let { "$it days ago" } ?: "Not available"
        val path = status.lastBackupPath ?: "Create a backup to record the local file path."
        textView.text = "Backup Status\n" +
                "Last backup: $lastBackup\n" +
                "Age: $days\n" +
                "Location: $path\n\n" +
                status.guidance
    }
}
