package com.eyal.exam.pelecard.ui.signature

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyal.exam.pelecard.data.entities.PaymentDetails
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.repos.NavigationRepository
import com.eyal.exam.pelecard.repos.PaymentRepository
import com.eyal.exam.pelecard.repos.SignatureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignatureViewModel @Inject constructor(
    private val navigationRepository: NavigationRepository,
    private val paymentRepository: PaymentRepository,
    private val signatureRepository: SignatureRepository,
) : ViewModel() {

    companion object {
        const val TAG = "SignatureViewModel"
    }

    //----------------- UI State -----------------
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> get() = _uiState

    fun goToPreviousScreen() = with(viewModelScope) {
        launch {
            navigationRepository.navigateTo(NavEvent.NavigateToMain)
        }
    }

    fun onSubmitClicked(paymentId: Int, savedOffsets: List<Offset>, savedDensity: Density) = with(viewModelScope) {
        launch {
            Log.d(TAG, "onSubmitClicked: savedOffsets is $savedOffsets and savedDensity is $savedDensity")
            _uiState.value = UiState.Loading
            try {
                // 1. Get PaymentDetails from Payment ID
                Log.d(TAG, "onSubmitClicked: paymentId is $paymentId")
                val paymentDetails: PaymentDetails? = paymentRepository.getPaymentById(paymentId)

                paymentDetails?.let {
                    Log.d(TAG, "onSubmitClicked: paymentId is $paymentId")

                    // 2. Save Signature as file
                    val file = signatureRepository.saveSignature(savedOffsets, savedDensity, paymentId)
                    Log.d(TAG, "onSubmitClicked: newFilePath is ${file.absolutePath}")

                    // 3. Update file path inside of the payment details
                    val newPaymentDetails = paymentDetails.copy(signatureFilePath = file.absolutePath)
                    Log.d(TAG, "onSubmitClicked: newPaymentDetails is $newPaymentDetails")

                    // 4. update DB
                    paymentRepository.updatePaymentDetails(newPaymentDetails)
                    Log.d(TAG, "onSubmitClicked: newPaymentDetails is updated in paymentRepository")

                    // 5. Navigate to Next Screen
                    navigationRepository.navigateTo(NavEvent.NavigateToReceipt(paymentId))
                }
                delay(680) // for convenient loading animation
                _uiState.value = UiState.Success(true)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error saving signature")
            }
        }
    }
}