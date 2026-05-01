package com.example.budgettracker.data.repository

import com.example.budgettracker.data.dao.AssetDao
import com.example.budgettracker.data.dao.DebtDao
import com.example.budgettracker.data.dao.NetWorthSnapshotDao
import com.example.budgettracker.data.entity.AssetEntity
import com.example.budgettracker.data.entity.NetWorthSnapshotEntity
import com.example.budgettracker.data.model.AssetType
import com.example.budgettracker.data.model.FinancialAsset
import com.example.budgettracker.data.model.NetWorthSnapshot
import com.example.budgettracker.data.model.NetWorthSummary
import com.example.budgettracker.utils.DateUtils

class NetWorthRepository(
    private val assetDao: AssetDao,
    private val debtDao: DebtDao,
    private val snapshotDao: NetWorthSnapshotDao
) {
    suspend fun getSummary(): NetWorthSummary {
        val assets = assetDao.getActiveAssets().map { it.toModel() }
        val debts = debtDao.getAllDebts()
        val totalAssets = assets.sumOf { it.currentValue }
        val totalDebts = debts.sumOf { it.currentBalance }
        val recentSnapshots = snapshotDao.getRecentSnapshots(6).map { it.toModel() }
        val latestSnapshot = recentSnapshots.firstOrNull()
        val previousSnapshot = latestSnapshot
            ?.let { snapshotDao.getSnapshotBefore(it.snapshotDate) }
            ?.toModel()
            ?: recentSnapshots.drop(1).firstOrNull()
        val movement = latestSnapshot?.let { latest ->
            previousSnapshot?.let { previous -> latest.netWorth - previous.netWorth }
        }
        val assetMovement = latestSnapshot?.let { latest ->
            previousSnapshot?.let { previous -> latest.totalAssets - previous.totalAssets }
        }
        val debtMovement = latestSnapshot?.let { latest ->
            previousSnapshot?.let { previous -> previous.totalDebts - latest.totalDebts }
        }

        return NetWorthSummary(
            assets = assets,
            recentSnapshots = recentSnapshots,
            totalAssets = totalAssets,
            totalDebts = totalDebts,
            currentNetWorth = totalAssets - totalDebts,
            latestSnapshot = latestSnapshot,
            previousSnapshot = previousSnapshot,
            movementSincePrevious = movement,
            assetMovementSincePrevious = assetMovement,
            debtMovementSincePrevious = debtMovement,
            guidance = guidanceFor(totalAssets, totalDebts, latestSnapshot, previousSnapshot)
        )
    }

    suspend fun addAsset(
        name: String,
        assetType: AssetType,
        currentValue: Double,
        notes: String
    ): Long {
        require(name.isNotBlank()) { "Asset name is required" }
        require(currentValue >= 0.0) { "Asset value cannot be negative" }

        val now = System.currentTimeMillis()
        return assetDao.insertAsset(
            AssetEntity(
                name = name.trim(),
                assetType = assetType.name,
                currentValue = currentValue,
                notes = notes.trim(),
                createdAt = now,
                updatedAt = now
            )
        )
    }

    suspend fun updateAssetValue(assetId: Long, currentValue: Double, notes: String) {
        require(currentValue >= 0.0) { "Asset value cannot be negative" }
        val asset = assetDao.getAssetById(assetId) ?: error("Asset not found")
        assetDao.updateAsset(
            asset.copy(
                currentValue = currentValue,
                notes = notes.trim(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun createSnapshot(
        snapshotDate: String = DateUtils.getCurrentDate(),
        notes: String = ""
    ): NetWorthSnapshot {
        val assets = assetDao.getActiveAssets()
        val debts = debtDao.getAllDebts()
        val totalAssets = assets.sumOf { it.currentValue }
        val totalDebts = debts.sumOf { it.currentBalance }
        val now = System.currentTimeMillis()
        val snapshot = NetWorthSnapshotEntity(
            snapshotDate = snapshotDate,
            totalAssets = totalAssets,
            totalDebts = totalDebts,
            netWorth = totalAssets - totalDebts,
            notes = notes.trim(),
            createdAt = now,
            updatedAt = now
        )
        val id = snapshotDao.insertSnapshot(snapshot)
        return snapshot.copy(id = id).toModel()
    }

    private fun guidanceFor(
        totalAssets: Double,
        totalDebts: Double,
        latestSnapshot: NetWorthSnapshot?,
        previousSnapshot: NetWorthSnapshot?
    ): String {
        if (totalAssets == 0.0 && totalDebts == 0.0) {
            return "Add your first asset or debt to see the full recovery picture."
        }
        if (latestSnapshot == null) {
            return "Create your first snapshot to start seeing long-term movement."
        }
        if (previousSnapshot == null) {
            return "Your first snapshot is saved. Future snapshots will show whether your position is improving."
        }
        val movement = latestSnapshot.netWorth - previousSnapshot.netWorth
        return when {
            movement > 0.0 ->
                "Your position improved since the previous snapshot. That is real recovery movement."
            movement < 0.0 ->
                "Your position moved backward since the previous snapshot. You have a clear baseline to recover from."
            else ->
                "Your position stayed level since the previous snapshot. Small changes can start moving this forward."
        }
    }

    private fun AssetEntity.toModel(): FinancialAsset {
        return FinancialAsset(
            assetId = id,
            name = name,
            assetType = parseAssetType(assetType),
            currentValue = currentValue,
            notes = notes,
            updatedAt = updatedAt
        )
    }

    private fun NetWorthSnapshotEntity.toModel(): NetWorthSnapshot {
        return NetWorthSnapshot(
            snapshotId = id,
            snapshotDate = snapshotDate,
            totalAssets = totalAssets,
            totalDebts = totalDebts,
            netWorth = netWorth,
            notes = notes
        )
    }

    private fun parseAssetType(value: String): AssetType {
        return AssetType.values().firstOrNull { it.name == value } ?: AssetType.OTHER
    }
}
