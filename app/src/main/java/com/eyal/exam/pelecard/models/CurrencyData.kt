package com.eyal.exam.pelecard.models

import com.google.gson.annotations.SerializedName

data class CurrencyData(//delete if useless
    val code: String,
    val name: String,
    val symbol: String,
    val type: String,
    val rounding: Int,

    @SerializedName("symbol_native")
    val symbolNative: String,

    @SerializedName("decimal_digits")
    val decimalDigits: Int,

    @SerializedName("name_plural")
    val namePlural: String,
)