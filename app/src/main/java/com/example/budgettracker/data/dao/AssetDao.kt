package com.example.budgettracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.budgettracker.data.entity.AssetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AssetDao {
    @Query("SELECT * FROM assets WHERE isActive = 1 ORDER BY assetType, name")
    fun observeActiveAssets(): Flow<List<AssetEntity>>

    @Query("SELECT * FROM assets WHERE isActive = 1 ORDER BY assetType, name")
    suspend fun getActiveAssets(): List<AssetEntity>

    @Query("SELECT * FROM assets WHERE id = :id LIMIT 1")
    suspend fun getAssetById(id: Long): AssetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: AssetEntity): Long

    @Update
    suspend fun updateAsset(asset: AssetEntity)
}
