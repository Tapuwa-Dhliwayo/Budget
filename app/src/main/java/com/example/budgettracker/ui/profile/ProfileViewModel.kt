package com.example.budgettracker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation.findNavController
import com.example.budgettracker.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userProfileRepository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState(isLoading = true)
            try {
                val user = userProfileRepository.getOrCreateUser()
                _uiState.value = ProfileUiState(
                    isLoading = false,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    fullName = "${user.firstName} ${user.lastName}".trim()
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState(
                    isLoading = false,
                    error = "Failed to load profile"
                )
            }
        }
    }

    fun updateProfile(firstName: String, lastName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, saveSuccess = false)
            try {
                userProfileRepository.updateUser(firstName, lastName)
                _uiState.value = ProfileUiState(
                    isLoading = false,
                    firstName = firstName,
                    lastName = lastName,
                    fullName = "$firstName $lastName".trim(),
                    saveSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to update profile"
                )
            }
        }
    }

    fun clearSaveSuccess() {
        _uiState.value = _uiState.value.copy(saveSuccess = false)
    }
}
