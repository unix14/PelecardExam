package com.eyal.exam.pelecard.network

import com.google.gson.annotations.SerializedName

data class CurrencyConversionResponse(
    @SerializedName("data")
    val rates: Map<String, Double>? = null
)