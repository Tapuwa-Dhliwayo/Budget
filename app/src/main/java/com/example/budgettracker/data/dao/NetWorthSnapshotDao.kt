package com.example.budgettracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgettracker.data.entity.NetWorthSnapshotEntity

@Dao
interface NetWorthSnapshotDao {
    @Query("SELECT * FROM net_worth_snapshots ORDER BY snapshotDate DESC, id DESC LIMIT :limit")
    suspend fun getRecentSnapshots(limit: Int): List<NetWorthSnapshotEntity>

    @Query("SELECT * FROM net_worth_snapshots ORDER BY snapshotDate DESC, id DESC LIMIT 1")
    suspend fun getLatestSnapshot(): NetWorthSnapshotEntity?

    @Query("SELECT * FROM net_worth_snapshots WHERE snapshotDate < :date ORDER BY snapshotDate DESC, id DESC LIMIT 1")
    suspend fun getSnapshotBefore(date: String): NetWorthSnapshotEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: NetWorthSnapshotEntity): Long
}
