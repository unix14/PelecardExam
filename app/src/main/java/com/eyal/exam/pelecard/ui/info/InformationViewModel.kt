package com.eyal.exam.pelecard.ui.info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.repos.CurrencyRepository
import com.eyal.exam.pelecard.repos.NavigationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val navigationRepository: NavigationRepository
) : ViewModel() {

    //----------------- UI State -----------------
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> get() = _uiState

    companion object {
        private const val TAG = "InformationViewModel"
    }

    init {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            Log.d(TAG, "fetchStatus: Loading Status")
            try {
                val data = currencyRepository.fetchStatus()
                _uiState.emit(UiState.Success(data))
                Log.d(TAG, "fetchStatus: Success!! $data")
            } catch (e: Exception) {
                _uiState.emit(UiState.Error(e.message ?: "An error occurred"))
                Log.e(TAG, "fetchStatus: ${e.message}")
            }
        }
    }

    fun navigateBack() = with(viewModelScope) {
        launch {
            navigationRepository.navigateTo(NavEvent.NavigateToMain)
        }
    }
}