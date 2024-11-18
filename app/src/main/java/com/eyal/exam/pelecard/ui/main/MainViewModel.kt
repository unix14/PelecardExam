package com.eyal.exam.pelecard.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyal.exam.pelecard.abs.Constants
import com.eyal.exam.pelecard.data.entities.PaymentDetails
import com.eyal.exam.pelecard.data.entities.SettingsConfig
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.models.SettingId
import com.eyal.exam.pelecard.repos.NavigationRepository
import com.eyal.exam.pelecard.repos.PaymentRepository
import com.eyal.exam.pelecard.repos.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val navigationRepository: NavigationRepository,
    private val paymentRepository: PaymentRepository,
) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    //----------------- Settings Configuration -----------------
    private val _settingsConfiguration = MutableStateFlow<SettingsConfig?>(null)
    val settingsConfiguration = _settingsConfiguration.asStateFlow()

    //----------------- Payment Details -----------------
    private val _paymentDetails = MutableStateFlow<PaymentDetails>(
        Constants.DEFAULT_PAYMENT_DETAILS
    )
    val paymentDetails: StateFlow<PaymentDetails> get() = _paymentDetails

    init {
        viewModelScope.launch {
            Log.d(TAG, "init: _paymentDetails.id ${_paymentDetails.value.id}")

            // insert initial payment details
            val newId = paymentRepository.insert(_paymentDetails.value)
            Log.d(TAG, "init: insert Success -> new payment details record Id $newId")
            _paymentDetails.value = _paymentDetails.value.copy(id = newId.toInt())

            settingsRepository.settingsConfiguration.collect { settingsConfig ->
                _settingsConfiguration.value = settingsConfig
                Log.d(TAG, "collect settingsConfig: ${settingsConfig.settingsMap.size} Parameters")

                /// init payment details' invisible params according to the Settings configurations
                if(_settingsConfiguration.value?.settingsMap?.get(SettingId.SIGNATURE)?.value == false) {
                    updatePaymentDetails(_paymentDetails.value.copy(isSignature = false))
                    Log.d(TAG, "collect settingsConfig: Set isSignature to false")
                }
                if(_settingsConfiguration.value?.settingsMap?.get(SettingId.PAYMENTS)?.value == false) {
                    updatePaymentDetails(_paymentDetails.value.copy(isPayments = false))
                    Log.d(TAG, "collect settingsConfig: Set isPayments to false")
                }
                if(_settingsConfiguration.value?.settingsMap?.get(SettingId.CURRENCY)?.value == false) {
                    updatePaymentDetails(_paymentDetails.value.copy(isCurrency = false))
                    Log.d(TAG, "collect settingsConfig: Set currency to false")
                }
            }
        }
    }

    //----------------- Functions -----------------
    fun updatePaymentDetails(paymentDetails: PaymentDetails) = with(viewModelScope) {
        launch {
            // update the flow
            _paymentDetails.value = paymentDetails
            // save to Room DB
            paymentRepository.updatePaymentDetails(paymentDetails)
        }
    }

    fun goToSettings() = with(viewModelScope){
        launch {
            navigationRepository.navigateTo(NavEvent.NavigateToSettings)
        }
    }

    fun goToInfo() = with(viewModelScope) {
        launch {
            navigationRepository.navigateTo(NavEvent.NavigateToInfo)
        }
    }

    fun goToNextScreen() = with(viewModelScope) {
        launch {
            // decide on which screen to go next
            val nextNavEvent: NavEvent = _paymentDetails.value.let {
                if (it.isSignature) {
                    NavEvent.NavigateToSignature(paymentDetails.value.id)
                } else {
                    NavEvent.NavigateToReceipt(paymentDetails.value.id)
                }
            }
            navigationRepository.navigateTo(nextNavEvent)
        }
    }
}