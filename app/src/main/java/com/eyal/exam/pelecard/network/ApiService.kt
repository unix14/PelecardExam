package com.eyal.exam.pelecard.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("latest")
    suspend fun getCurrencyConversionRates(
        @Query("base_currency") baseCurrency: String,
        @Query("currencies") currencies: String? = null,
    ): CurrencyConversionResponse

    @GET("status")
    suspend fun getStatus(): StatusDataResponse
}