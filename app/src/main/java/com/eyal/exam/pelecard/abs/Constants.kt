package com.eyal.exam.pelecard.abs

import com.eyal.exam.pelecard.data.entities.PaymentDetails
import com.eyal.exam.pelecard.data.entities.SettingsConfig
import com.eyal.exam.pelecard.models.RouteName
import com.eyal.exam.pelecard.models.SettingId
import com.eyal.exam.pelecard.models.SettingsItem

object Constants {
    const val DEFAULT_CONVERSION_API_BASE_URL = "https://api.freecurrencyapi.com/v1/"
    const val DEFAULT_CONVERSION_API_KEY = "fca_live_1vL9nXbTLMcwTBtCOp1SE2W4JM4KAt1b1d9eVqkx"
    const val DEFAULT_PAYMENT_API_BASE_URL = "https://dummyjson.com/c/" /// Used as a fake API response
    const val DEFAULT_PAYMENT_API_POST_ENDPOINT = "16f1-6182-4598-83c4"

    // App's default values
    const val DEFAULT_CURRENCY = "USD"
    val DEFAULT_PAYMENT_DETAILS = PaymentDetails(0, 200.0, true,
        12, true, null, true,DEFAULT_CURRENCY)
    val DEFAULT_SETTINGS_CONFIG = SettingsConfig(
        mapOf(
            SettingId.PAYMENTS to SettingsItem("Allow payments", true),
            SettingId.CURRENCY to SettingsItem("Allow currency", true),
            SettingId.SIGNATURE to SettingsItem("Allow signature", true)
        )
    )

    /// navigation Params
    val DEFAULT_STARTING_ROUTE_NAME = RouteName.MAIN.name.lowercase()

    const val NAV_PARAM_PAYMENT_ID = "paymentID"
    const val NAV_PARAM_CONVERSION_AMOUNT = "amount"
    const val NAV_PARAM_CONVERSION_CURRENCY = "currency"

}