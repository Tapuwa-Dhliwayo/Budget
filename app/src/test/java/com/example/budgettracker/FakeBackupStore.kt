package com.example.budgettracker

import com.example.budgettracker.data.repository.BackupStore

class FakeBackupStore : BackupStore {
    var lastDate: String? = null
    var lastPath: String? = null
    var createdPath: String = "/tmp/mini_budget_backup.db"

    override suspend fun getLastBackupDate(): String? = lastDate

    override suspend fun getLastBackupPath(): String? = lastPath

    override suspend fun saveBackupMetadata(date: String, path: String) {
        lastDate = date
        lastPath = path
    }

    override suspend fun createBackupFile(): String = createdPath
}
