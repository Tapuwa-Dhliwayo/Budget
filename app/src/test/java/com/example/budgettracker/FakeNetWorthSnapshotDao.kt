package com.example.budgettracker

import com.example.budgettracker.data.dao.NetWorthSnapshotDao
import com.example.budgettracker.data.entity.NetWorthSnapshotEntity

class FakeNetWorthSnapshotDao : NetWorthSnapshotDao {
    private val snapshots = mutableListOf<NetWorthSnapshotEntity>()
    private var nextId = 1L

    override suspend fun getRecentSnapshots(limit: Int): List<NetWorthSnapshotEntity> {
        return snapshots
            .sortedWith(compareByDescending<NetWorthSnapshotEntity> { it.snapshotDate }.thenByDescending { it.id })
            .take(limit)
    }

    override suspend fun getLatestSnapshot(): NetWorthSnapshotEntity? {
        return getRecentSnapshots(1).firstOrNull()
    }

    override suspend fun getSnapshotBefore(date: String): NetWorthSnapshotEntity? {
        return snapshots
            .filter { it.snapshotDate < date }
            .sortedWith(compareByDescending<NetWorthSnapshotEntity> { it.snapshotDate }.thenByDescending { it.id })
            .firstOrNull()
    }

    override suspend fun insertSnapshot(snapshot: NetWorthSnapshotEntity): Long {
        val id = nextId++
        snapshots.add(snapshot.copy(id = id))
        return id
    }
}
