package com.eyal.exam.pelecard.ui.conversion

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.repos.CurrencyRepository
import com.eyal.exam.pelecard.repos.NavigationRepository
import com.eyal.exam.pelecard.ui.main.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val navigationRepository: NavigationRepository
) : ViewModel() {

    //----------------- UI State -----------------
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> get() = _uiState

    companion object {
        private const val TAG = "CurrencyConversionViewModel"
    }

    fun fetchConversionRate(baseCurrency: String, currencies: String? = null) = with(viewModelScope) {
        launch {
            _uiState.emit(UiState.Loading)
            Log.d(TAG, "fetchConversionRate: Loading $baseCurrency $currencies")
            try {
                val data = currencyRepository.fetchConversionRate(baseCurrency, currencies)
                val rates: MutableMap<String, Double>? = data.rates?.toMutableMap()
                // remove base currency from response -> since we usually bring the whole list ( the base currency will show up there)
                rates?.remove(baseCurrency)
                _uiState.emit(UiState.Success(data.copy(rates = rates)))
                Log.d(TAG, "fetchConversionRate: Success!! $data")
            } catch (e: Exception) {
                _uiState.emit(UiState.Error(e.message ?: "An error occurred"))
                Log.e(TAG, "fetchConversionRate: ${e.message}")
            }
        }
    }

    fun navigateBack() = with(viewModelScope) {
        launch {
            navigationRepository.navigateTo(NavEvent.NavigateToMain)
        }
    }
}