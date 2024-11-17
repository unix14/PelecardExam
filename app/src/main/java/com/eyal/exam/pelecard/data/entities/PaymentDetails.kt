package com.eyal.exam.pelecard.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_details")
data class PaymentDetails(
    // Primary key with auto-increment
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val amount: Double,
    val isPayments: Boolean,
    val numberOfPayments: Int,
    val isSignature: Boolean,
    val currency: String,
    val signatureFilePath: String? = null,

    val isCompleted: Boolean = false
)