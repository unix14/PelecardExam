package com.eyal.exam.pelecard.utils

import java.text.DecimalFormat

/// Calculate amount in readable format
fun getNumberFormat(input: Int) : String {
    val decimalFormat = DecimalFormat("#,###,###")
    return decimalFormat.format(input)
}