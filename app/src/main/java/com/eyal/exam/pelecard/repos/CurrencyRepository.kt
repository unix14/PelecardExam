package com.eyal.exam.pelecard.repos

import com.eyal.exam.pelecard.abs.ApiService
import com.eyal.exam.pelecard.models.CurrencyConversionResponse
import com.eyal.exam.pelecard.models.CurrencyDataResponse
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun fetchConversionRate(baseCurrency: String, currencies: String? = null): CurrencyConversionResponse {
        return apiService.getCurrencyConversionRates(baseCurrency, currencies)
    }

    suspend fun fetchCurrenciesData(currencies: String? = null): CurrencyDataResponse {
        return apiService.getCurrenciesData(currencies)
    }
}