package com.eyal.exam.pelecard.repos

import com.eyal.exam.pelecard.network.ApiService
import com.eyal.exam.pelecard.network.CurrencyConversionResponse
import com.eyal.exam.pelecard.network.CurrencyDataResponse
import com.eyal.exam.pelecard.network.StatusDataResponse
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