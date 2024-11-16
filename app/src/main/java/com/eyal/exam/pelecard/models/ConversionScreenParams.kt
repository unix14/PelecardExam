package com.eyal.exam.pelecard.models

data class ConversionScreenParams(
    val amount: Int,
    val currency: String // todo use currency from api?
)