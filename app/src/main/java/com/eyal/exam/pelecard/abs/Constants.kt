package com.eyal.exam.pelecard.abs

import com.eyal.exam.pelecard.models.PaymentDetails
import com.eyal.exam.pelecard.models.SettingId
import com.eyal.exam.pelecard.models.SettingsConfig
import com.eyal.exam.pelecard.models.SettingsItem

object Constants {
    const val DEFAULT_CONVERSION_API_BASE_URL = "https://api.freecurrencyapi.com/v1/"
    const val DEFAULT_CONVERSION_API_KEY = "fca_live_1vL9nXbTLMcwTBtCOp1SE2W4JM4KAt1b1d9eVqkx"

    // App's default values
    val DEFAULT_PAYMENT_DETAILS = PaymentDetails(200, true, 12, true, "USD")
    val DEFAULT_SETTINGS_CONFIG = SettingsConfig(
        mapOf(
            SettingId.PAYMENTS to SettingsItem("Allow payments", true),
            SettingId.CURRENCY to SettingsItem("Allow currency", true),
            SettingId.SIGNATURE to SettingsItem("Allow Signature", true)
        )
    )
}