package com.example.budgettracker

import com.example.budgettracker.data.dao.AssetDao
import com.example.budgettracker.data.entity.AssetEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAssetDao : AssetDao {
    private val assets = mutableListOf<AssetEntity>()
    private val flow = MutableStateFlow<List<AssetEntity>>(emptyList())
    private var nextId = 1L

    override fun observeActiveAssets(): Flow<List<AssetEntity>> = flow

    override suspend fun getActiveAssets(): List<AssetEntity> {
        return assets.filter { it.isActive }.sortedWith(compareBy({ it.assetType }, { it.name }))
    }

    override suspend fun getAssetById(id: Long): AssetEntity? {
        return assets.find { it.id == id }
    }

    override suspend fun insertAsset(asset: AssetEntity): Long {
        val id = nextId++
        assets.add(asset.copy(id = id))
        updateFlow()
        return id
    }

    override suspend fun updateAsset(asset: AssetEntity) {
        val index = assets.indexOfFirst { it.id == asset.id }
        if (index >= 0) {
            assets[index] = asset
            updateFlow()
        }
    }

    private fun updateFlow() {
        flow.value = assets.filter { it.isActive }
    }
}
