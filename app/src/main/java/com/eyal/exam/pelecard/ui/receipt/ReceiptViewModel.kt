package com.eyal.exam.pelecard.ui.receipt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyal.exam.pelecard.models.ConversionScreenParams
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.data.entities.PaymentDetails
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.repos.NavigationRepository
import com.eyal.exam.pelecard.repos.PaymentRepository
import com.eyal.exam.pelecard.ui.signature.SignatureViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor(
    private val navigationRepository: NavigationRepository,
    private val paymentRepository: PaymentRepository,
) : ViewModel() {


    //----------------- UI State -----------------
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> get() = _uiState

    fun loadPaymentId(paymentId: Int) {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            Log.d(SignatureViewModel.TAG, "loadPaymentId: paymentId is $paymentId")
            val paymentDetails: PaymentDetails? = paymentRepository.getPaymentById(paymentId)
            Log.d(SignatureViewModel.TAG, "loadPaymentId: paymentDetails is $paymentDetails")
            _uiState.emit(UiState.Success(paymentDetails))
        }
    }

    fun onFinishedClicked() {
        viewModelScope.launch {
            _uiState.emit(UiState.Loading)
            delay(1250) // for convenient loading animation
            goToMainScreen()
            _uiState.emit(UiState.Idle)
        }
    }

    private fun goToMainScreen() {
        viewModelScope.launch {
            navigationRepository.navigateTo(NavEvent.NavigateToMain)
        }
    }

    fun onConvertClicked(paymentDetails: PaymentDetails) {
        viewModelScope.launch {
            navigationRepository.navigateTo(NavEvent.NavigateToConversion(
                amount = paymentDetails.amount,
                currency = paymentDetails.currency
            ))
        }
    }
}