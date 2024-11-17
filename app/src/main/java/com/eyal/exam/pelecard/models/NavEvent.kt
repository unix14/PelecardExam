package com.eyal.exam.pelecard.models

sealed class NavEvent(val routeName: RouteName) {
    data object NavigateToMain : NavEvent(RouteName.MAIN)
    data object NavigateToSettings : NavEvent(RouteName.SETTINGS)
    data class NavigateToSignature(val paymentId: Int) : NavEvent(RouteName.SIGNATURE)
    data class NavigateToReceipt(val paymentId: Int) : NavEvent(RouteName.RECEIPT)
    data class NavigateToConversion(val amount: Double, val currency: String) : NavEvent(RouteName.CONVERSION)
    data object NavigateToInfo : NavEvent(RouteName.INFO)
}