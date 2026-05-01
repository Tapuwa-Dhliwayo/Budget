package com.example.budgettracker.ui.networth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgettracker.data.model.AssetType
import com.example.budgettracker.data.repository.NetWorthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NetWorthViewModel(
    private val repository: NetWorthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NetWorthUiState(isLoading = true))
    val uiState: StateFlow<NetWorthUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                _uiState.value = NetWorthUiState(
                    summary = repository.getSummary(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to load net worth: ${e.message}"
                )
            }
        }
    }

    fun addAsset(name: String, assetType: AssetType, value: Double, notes: String) {
        viewModelScope.launch {
            try {
                repository.addAsset(name, assetType, value, notes)
                load()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to add asset: ${e.message}")
            }
        }
    }

    fun updateAssetValue(assetId: Long, value: Double, notes: String) {
        viewModelScope.launch {
            try {
                repository.updateAssetValue(assetId, value, notes)
                load()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to update asset: ${e.message}")
            }
        }
    }

    fun createSnapshot(notes: String) {
        viewModelScope.launch {
            try {
                repository.createSnapshot(notes = notes)
                load()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Failed to create snapshot: ${e.message}")
            }
        }
    }
}
