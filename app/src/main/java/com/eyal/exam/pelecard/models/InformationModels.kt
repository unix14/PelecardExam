package com.eyal.exam.pelecard.models

import com.google.gson.annotations.SerializedName

data class StatusDataResponse(
    @SerializedName("account_id")
    val accountId: Long,
    val quotas: Quotas
)

data class Quotas(
    val month: QuotaDetails,
    val grace: QuotaDetails
)

data class QuotaDetails(
    val total: Int,
    val used: Int,
    val remaining: Int
)