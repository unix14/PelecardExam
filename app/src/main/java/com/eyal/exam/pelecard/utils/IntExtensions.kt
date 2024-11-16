package com.eyal.exam.pelecard.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

/// Calculate amount in readable format
fun getNumberFormat(input: Int) : String { //todo convert amount to Double???
    val decimalFormat = DecimalFormat("#,###,###")
    return decimalFormat.format(input)
}

fun getNumberFormat(input: Double) : String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.ROOT).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 2
    }
    return numberFormat.format(input)
}

//todo rename file

// Messy to read at first, but it refactors text like this "RECEIPT/{PAYMENTDETAILS}" into "Receipt"
fun getReadableRouteName(routeName: String?) : String? {
    (routeName?.indexOf('/') ?: -1).let { indexOfSlash->
        return routeName?.replaceFirstChar {
            // Capitalize only the first char
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }.run {
            if(this != null && indexOfSlash > -1){
                // we found a slash for parameters
                this.substring(0, indexOfSlash)
            } else this
        }
    }
}