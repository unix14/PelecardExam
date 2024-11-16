package com.eyal.exam.pelecard.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyal.exam.pelecard.abs.Constants
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.models.PaymentDetails
import com.eyal.exam.pelecard.models.SettingId
import com.eyal.exam.pelecard.models.SettingsConfig
import com.eyal.exam.pelecard.repos.NavigationRepository
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
                    updatePaymentDetails(_paymentDetails.value.copy(currency = ""))
                    Log.d(TAG, "collect settingsConfig: Set currency to false")
                }
            }
        }
    }

    //----------------- Functions -----------------
    fun updatePaymentDetails(paymentDetails: PaymentDetails) {
        _paymentDetails.value = paymentDetails
    }

    fun goToSettings() {
        viewModelScope.launch {
            navigationRepository.navigateTo(NavEvent.NavigateToSettings)
        }
    }

    fun goToInfo() {
        viewModelScope.launch {
            navigationRepository.navigateTo(NavEvent.NavigateToInfo)
        }
    }

    fun goToNextScreen() {
        // decide on which screen to go next
        val nextNavEvent: NavEvent = _paymentDetails.value.let {
            if (it.isSignature) {
                NavEvent.NavigateToSignature(paymentDetails.value)
            } else {
                NavEvent.NavigateToReceipt(paymentDetails.value)
            }
        }
        viewModelScope.launch {
            navigationRepository.navigateTo(nextNavEvent)
        }
    }
}