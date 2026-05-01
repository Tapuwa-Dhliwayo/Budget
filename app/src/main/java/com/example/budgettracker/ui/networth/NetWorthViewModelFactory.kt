package com.example.budgettracker.ui.networth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.budgettracker.data.repository.NetWorthRepository

class NetWorthViewModelFactory(
    private val repository: NetWorthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NetWorthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NetWorthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
