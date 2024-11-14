package com.eyal.exam.pelecard.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyal.exam.pelecard.models.PaymentDetails
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.repos.CurrencyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myRepository: CurrencyRepository
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    //----------------- UI State -----------------
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> get() = _uiState


    //----------------- Payment Details -----------------
    private val _paymentDetails = MutableStateFlow<PaymentDetails>(
        PaymentDetails(200, true, 12, true, "USD")
    )
    val paymentDetails: StateFlow<PaymentDetails> get() = _paymentDetails

    //----------------- Functions -----------------
    fun updatePaymentDetails(paymentDetails: PaymentDetails) {
        _paymentDetails.value = paymentDetails
    }

    fun fetchConversionRate(baseCurrency: String, currencies: String? = null) {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            try {
                val data = myRepository.fetchConversionRate(baseCurrency, currencies)
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
                val data = myRepository.fetchCurrenciesData(currencies)
                _uiState.emit(UiState.Success(data))
            } catch (e: Exception) {
                Log.d(TAG, "fetchData: ${e.message}")
                _uiState.emit(UiState.Error(e.message ?: "An error occurred"))
            }
        }
    }
}