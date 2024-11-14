package com.eyal.exam.pelecard.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyal.exam.pelecard.abs.Constants
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.models.PaymentDetails
import com.eyal.exam.pelecard.models.SettingsConfig
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.repos.NavigationRepository
import com.eyal.exam.pelecard.repos.CurrencyRepository
import com.eyal.exam.pelecard.repos.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val settingsRepository: SettingsRepository,
    private val navigationRepository: NavigationRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _settingsConfiguration = MutableStateFlow<SettingsConfig?>(null)
    val settingsConfiguration = _settingsConfiguration.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settingsConfiguration.collect { settingsConfig ->
                _settingsConfiguration.value = settingsConfig
            }
        }
    }

    //----------------- UI State -----------------
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> get() = _uiState


    //----------------- Payment Details -----------------
    private val _paymentDetails = MutableStateFlow<PaymentDetails>(
        Constants.DEFAULT_PAYMENT_DETAILS
    )
    val paymentDetails: StateFlow<PaymentDetails> get() = _paymentDetails

    //----------------- Functions -----------------
    fun updatePaymentDetails(paymentDetails: PaymentDetails) {
        _paymentDetails.value = paymentDetails
    }

    fun goToSettings() {
        viewModelScope.launch {
            navigationRepository.navigateTo(NavEvent.NavigateToSettings)
        }
    }

    fun goToNextScreen() {
        //todo change to navigate to receipt if no signature is required
        viewModelScope.launch {
            navigationRepository.navigateTo(NavEvent.NavigateToSignature(paymentDetails.value))
        }
    }

    fun fetchConversionRate(baseCurrency: String, currencies: String? = null) {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            try {
                val data = currencyRepository.fetchConversionRate(baseCurrency, currencies)
                _uiState.emit(UiState.Success(data))
            } catch (e: Exception) {
                Log.d(TAG, "fetchData: ${e.message}")
                _uiState.emit(UiState.Error(e.message ?: "An error occurred"))
            }
        }
    }

    fun fetchCurrenciesData(currencies: String? = null) {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            try {
                val data = currencyRepository.fetchCurrenciesData(currencies)
                _uiState.emit(UiState.Success(data))
            } catch (e: Exception) {
                Log.d(TAG, "fetchData: ${e.message}")
                _uiState.emit(UiState.Error(e.message ?: "An error occurred"))
            }
        }
    }
}