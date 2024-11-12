package com.eyal.exam.pelecard.abs

import com.eyal.exam.pelecard.models.CurrencyConversionResponse
import retrofit2.http.GET

interface ApiService {

    @GET("latest")
    suspend fun getCurrencyConversion(): CurrencyConversionResponse
}