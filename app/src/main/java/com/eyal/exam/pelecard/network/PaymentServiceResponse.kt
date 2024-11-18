package com.eyal.exam.pelecard.network

import com.google.gson.annotations.SerializedName

data class PaymentServiceResponse(
    val status: String,
    val message: String,
    val body: PaymentServiceResponseBody
)

data class PaymentServiceResponseBody(
    @SerializedName("is_success")
    val isSuccess: Boolean,

    @SerializedName("popup_text")
    val popupText: String
)
