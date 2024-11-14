package com.eyal.exam.pelecard.models

sealed class UiState {
    data object Idle : UiState()
    data object Loading : UiState()
    data class Success<T>(val data: T) : UiState()
    data class Error(val message: String) : UiState()
}