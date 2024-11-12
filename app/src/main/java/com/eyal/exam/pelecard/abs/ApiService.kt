package com.eyal.exam.pelecard.abs

import com.eyal.exam.pelecard.models.CurrencyConversionResponse
import com.eyal.exam.pelecard.models.CurrencyDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("latest")
    suspend fun getCurrencyConversionRates(
        @Query("base_currency") baseCurrency: String,
        @Query("currencies") currencies: String? = null,
    ): CurrencyConversionResponse


    @GET("currencies")
    suspend fun getCurrenciesData(
        @Query("currencies") currencies: String? = null,
    ): CurrencyDataResponse

}