package com.example.budgettracker

import com.example.budgettracker.data.repository.BackupRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class BackupRepositoryTest {

    private lateinit var store: FakeBackupStore

    @Before
    fun setup() {
        store = FakeBackupStore()
    }

    @Test
    fun getStatus_withoutBackup_marksBackupNeeded() = runTest {
        val repository = BackupRepository(store) { LocalDate.parse("2026-05-01") }

        val status = repository.getStatus()

        assertTrue(status.backupNeeded)
        assertEquals(null, status.lastBackupDate)
        assertTrue(status.guidance.contains("Create a backup"))
    }

    @Test
    fun getStatus_withRecentBackup_isHealthy() = runTest {
        store.lastDate = "2026-04-25"
        store.lastPath = "/tmp/recent.db"
        val repository = BackupRepository(store) { LocalDate.parse("2026-05-01") }

        val status = repository.getStatus()

        assertFalse(status.backupNeeded)
        assertEquals(6L, status.daysSinceBackup)
        assertEquals("/tmp/recent.db", status.lastBackupPath)
    }

    @Test
    fun createBackup_savesMetadataForToday() = runTest {
        val repository = BackupRepository(store) { LocalDate.parse("2026-05-01") }

        val status = repository.createBackup()

        assertFalse(status.backupNeeded)
        assertEquals("2026-05-01", status.lastBackupDate)
        assertEquals("/tmp/mini_budget_backup.db", status.lastBackupPath)
    }
}
