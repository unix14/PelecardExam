package com.eyal.exam.pelecard.repos

import com.eyal.exam.pelecard.abs.ApiService
import com.eyal.exam.pelecard.models.CurrencyConversionResponse
import com.eyal.exam.pelecard.models.CurrencyDataResponse
import com.eyal.exam.pelecard.models.StatusDataResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun fetchConversionRate(baseCurrency: String, currencies: String? = null): CurrencyConversionResponse {
        return apiService.getCurrencyConversionRates(baseCurrency, currencies)
    }

    suspend fun fetchCurrenciesData(currencies: String? = null): CurrencyDataResponse {
        return apiService.getCurrenciesData(currencies)
    }

    suspend fun fetchStatus(): StatusDataResponse {
        return apiService.getStatus()
    }
}