package com.eyal.exam.pelecard.models

data class PaymentDetails(
    val amount: Int,
    val isPayments: Boolean,
    val numberOfPayments: Int,
    val isSignature: Boolean,
    val currency: String,
    val signatureFilePath: String? = null,
)