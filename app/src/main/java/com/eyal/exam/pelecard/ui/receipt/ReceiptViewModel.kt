package com.eyal.exam.pelecard.ui.receipt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyal.exam.pelecard.data.entities.PaymentDetails
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.repos.NavigationRepository
import com.eyal.exam.pelecard.repos.PaymentRepository
import com.eyal.exam.pelecard.repos.PaymentsServiceRepository
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
    private val paymentsServiceRepository: PaymentsServiceRepository,
) : ViewModel() {

    //----------------- UI State -----------------
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> get() = _uiState

    fun loadPaymentId(paymentId: Int) = with(viewModelScope) {
        launch {
            _uiState.emit(UiState.Loading)
            Log.d(SignatureViewModel.TAG, "loadPaymentId: paymentId is $paymentId")
            val paymentDetails: PaymentDetails? = paymentRepository.getPaymentById(paymentId)
            Log.d(SignatureViewModel.TAG, "loadPaymentId: paymentDetails is $paymentDetails")
            _uiState.emit(UiState.Success(paymentDetails))
        }
    }

    fun onFinishedClicked(paymentDetails: PaymentDetails) = with(viewModelScope) {
        launch {
            _uiState.emit(UiState.Loading)
            val newPaymentDetails = paymentDetails.copy(isCompleted= true)
            paymentRepository.updatePaymentDetails(newPaymentDetails)

            try {
                /// Act as if we send the payment to another API via POST method
                val response = paymentsServiceRepository.postPaymentDetails(newPaymentDetails)
                if(response.body.isSuccess) {
                    /// This is not an Error -> but acts similar
                    _uiState.emit(UiState.Error(response.body.popupText))
                }
            } catch (e: Throwable) {
                // ignore error -> if sometimes this fake API won't work
            }

            delay(1750) // for convenient loading animation
            goToMainScreen()
            _uiState.emit(UiState.Idle)
        }
    }

    private fun goToMainScreen() = with(viewModelScope) {
        launch {
            navigationRepository.navigateTo(NavEvent.NavigateToMain)
        }
    }

    fun onConvertClicked(paymentDetails: PaymentDetails) = with(viewModelScope) {
        launch {
            navigationRepository.navigateTo(NavEvent.NavigateToConversion(
                amount = paymentDetails.amount,
                currency = paymentDetails.currency
            ))
        }
    }
}