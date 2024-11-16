package com.eyal.exam.pelecard.models

sealed class NavEvent(val routeName: RouteName) {
    data object NavigateToMain : NavEvent(RouteName.MAIN)
    data object NavigateToSettings : NavEvent(RouteName.SETTINGS)
    data class NavigateToSignature(val paymentDetails: PaymentDetails) : NavEvent(RouteName.SIGNATURE)
    data class NavigateToReceipt(val paymentDetails: PaymentDetails) : NavEvent(RouteName.RECEIPT)
    data class NavigateToConversion(val screenParams: ConversionScreenParams) : NavEvent(RouteName.CONVERSION)
    data object NavigateToInfo : NavEvent(RouteName.INFO)
}