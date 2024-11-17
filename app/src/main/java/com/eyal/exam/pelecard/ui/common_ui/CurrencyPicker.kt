package com.eyal.exam.pelecard.ui.common_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.eyal.exam.pelecard.abs.Constants


@Composable
fun CurrencyPicker(modifier: Modifier = Modifier, currency: String, onCurrencySelected: (String) -> Unit) {
    var selectedCurrency by remember { mutableStateOf(currency.ifEmpty { Constants.DEFAULT_CURRENCY }) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = {
                selectedCurrency = "USD"
                onCurrencySelected(selectedCurrency)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (selectedCurrency == "USD") Color.White else Color.LightGray
            )
        ) {
            Text("USD") // todo extract hardcoded text to R.strings?
        }

        Button(
            onClick = {
                selectedCurrency = "ILS"
                onCurrencySelected(selectedCurrency)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (selectedCurrency == "ILS") Color.White else Color.LightGray
            )
        ) {
            Text("ILS")
        }
    }
}