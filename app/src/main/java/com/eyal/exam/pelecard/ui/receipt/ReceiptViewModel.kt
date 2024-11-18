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

    companion object {
        const val TAG = "ReceiptViewModel"
    }

    fun loadPaymentId(paymentId: Int) = with(viewModelScope) {
        launch {
            _uiState.emit(UiState.Loading)
            Log.d(TAG, "loadPaymentId: paymentId is $paymentId")
            val paymentDetails: PaymentDetails? = paymentRepository.getPaymentById(paymentId)
            Log.d(TAG, "loadPaymentId: paymentDetails is $paymentDetails")
            _uiState.emit(UiState.Success(paymentDetails))
        }
    }

    fun onFinishedClicked(paymentDetails: PaymentDetails) = with(viewModelScope) {
        launch {
            _uiState.emit(UiState.Loading)
            Log.d(TAG, "onFinishedClicked: paymentDetails is $paymentDetails")

            val newPaymentDetails = paymentDetails.copy(isCompleted= true)
            paymentRepository.updatePaymentDetails(newPaymentDetails)
            Log.d(TAG, "onFinishedClicked: newPaymentDetails is $newPaymentDetails")

            try {
                /// Act as if we send the payment to another API via POST method
                val response = paymentsServiceRepository.postPaymentDetails(newPaymentDetails)
                if(response.body.isSuccess) {
                    /// This is not an Error -> but acts similar
                    _uiState.emit(UiState.Error(response.body.popupText))
                }
                Log.d(TAG, "onFinishedClicked: paymentsServiceRepository.response is $response")
            } catch (e: Throwable) {
                // ignore error -> if sometimes this fake API won't work
                Log.d(TAG, "onFinishedClicked: paymentsServiceRepository.error is $e")
            }

            delay(1750) // for convenient loading animation
            navigationRepository.navigateTo(NavEvent.NavigateToMain)
            _uiState.emit(UiState.Idle)
        }
    }

    fun onConvertClicked(paymentDetails: PaymentDetails) = with(viewModelScope) {
        launch {
            Log.d(TAG, "onConvertClicked: paymentDetails is $paymentDetails")
            navigationRepository.navigateTo(NavEvent.NavigateToConversion(
                amount = paymentDetails.amount,
                currency = paymentDetails.currency
            ))
        }
    }
}