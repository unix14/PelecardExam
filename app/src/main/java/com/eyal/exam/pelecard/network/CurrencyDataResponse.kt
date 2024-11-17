package com.eyal.exam.pelecard.network

import com.eyal.exam.pelecard.models.CurrencyData

data class CurrencyDataResponse(
    val data: Map<String, CurrencyData>
)