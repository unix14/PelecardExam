package com.eyal.exam.pelecard.models

sealed class NavEvent {
    data object NavigateToMain : NavEvent()
    data object NavigateToSettings : NavEvent()
    data class NavigateToSignature(val paymentDetails: PaymentDetails) : NavEvent()
    data class NavigateToReceipt(val paymentDetails: PaymentDetails) : NavEvent()
    data object NavigateToCurrencyConversion : NavEvent()
}