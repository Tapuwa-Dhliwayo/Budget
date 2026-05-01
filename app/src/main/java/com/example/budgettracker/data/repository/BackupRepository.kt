package com.example.budgettracker.data.repository

import android.content.Context
import com.example.budgettracker.data.database.AppDatabase
import com.example.budgettracker.data.model.BackupStatus
import com.example.budgettracker.utils.DateUtils
import java.io.File
import java.time.LocalDate
import java.time.temporal.ChronoUnit

interface BackupStore {
    suspend fun getLastBackupDate(): String?
    suspend fun getLastBackupPath(): String?
    suspend fun saveBackupMetadata(date: String, path: String)
    suspend fun createBackupFile(): String
}

class BackupRepository(
    private val store: BackupStore,
    private val todayProvider: () -> LocalDate = { LocalDate.now() }
) {
    suspend fun getStatus(): BackupStatus {
        val lastDate = store.getLastBackupDate()
        val lastPath = store.getLastBackupPath()
        val daysSince = lastDate?.let {
            runCatching { ChronoUnit.DAYS.between(LocalDate.parse(it), todayProvider()) }.getOrNull()
        }
        val needsBackup = daysSince == null || daysSince > 14
        return BackupStatus(
            lastBackupDate = lastDate,
            lastBackupPath = lastPath,
            daysSinceBackup = daysSince,
            backupNeeded = needsBackup,
            guidance = guidanceFor(lastDate, daysSince)
        )
    }

    suspend fun createBackup(): BackupStatus {
        val path = store.createBackupFile()
        val today = todayProvider().toString()
        store.saveBackupMetadata(today, path)
        return getStatus()
    }

    private fun guidanceFor(lastDate: String?, daysSince: Long?): String {
        return when {
            lastDate == null ->
                "Your data is stored on this device. Create a backup so your recovery progress has a fallback."
            daysSince != null && daysSince > 14 ->
                "You have not created a backup recently. A quick backup helps protect your recovery progress."
            else ->
                "Your latest backup is recent. Keep backing up regularly so your data stays in your hands."
        }
    }
}

class AndroidBackupStore(
    private val context: Context,
    private val database: AppDatabase
) : BackupStore {
    private val prefs = context.getSharedPreferences("backup_status", Context.MODE_PRIVATE)

    override suspend fun getLastBackupDate(): String? {
        return prefs.getString(KEY_LAST_BACKUP_DATE, null)
    }

    override suspend fun getLastBackupPath(): String? {
        return prefs.getString(KEY_LAST_BACKUP_PATH, null)
    }

    override suspend fun saveBackupMetadata(date: String, path: String) {
        prefs.edit()
            .putString(KEY_LAST_BACKUP_DATE, date)
            .putString(KEY_LAST_BACKUP_PATH, path)
            .apply()
    }

    override suspend fun createBackupFile(): String {
        database.openHelper.writableDatabase.query("PRAGMA wal_checkpoint(FULL)").use { }
        val source = context.getDatabasePath(DATABASE_NAME)
        require(source.exists()) { "Database file was not found" }

        val backupDir = File(context.getExternalFilesDir(null) ?: context.filesDir, "backups")
        if (!backupDir.exists()) backupDir.mkdirs()
        val timestamp = DateUtils.getCurrentDate().replace("-", "")
        val backupFile = File(backupDir, "mini_budget_backup_$timestamp.db")
        source.copyTo(backupFile, overwrite = true)
        copySidecarIfPresent("${source.path}-wal", File(backupDir, "${backupFile.name}-wal"))
        copySidecarIfPresent("${source.path}-shm", File(backupDir, "${backupFile.name}-shm"))
        return backupFile.absolutePath
    }

    private fun copySidecarIfPresent(sourcePath: String, destination: File) {
        val source = File(sourcePath)
        if (source.exists()) source.copyTo(destination, overwrite = true)
    }

    private companion object {
        const val DATABASE_NAME = "budget_tracker.db"
        const val KEY_LAST_BACKUP_DATE = "last_backup_date"
        const val KEY_LAST_BACKUP_PATH = "last_backup_path"
    }
}
