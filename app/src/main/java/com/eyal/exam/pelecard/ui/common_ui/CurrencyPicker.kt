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
import androidx.compose.ui.res.stringResource
import com.eyal.exam.pelecard.R
import com.eyal.exam.pelecard.abs.Constants


@Composable
fun CurrencyPicker(modifier: Modifier = Modifier, currency: String, onCurrencySelected: (String) -> Unit) {
    var selectedCurrency by remember { mutableStateOf(currency.ifEmpty { Constants.DEFAULT_CURRENCY }) }
    val usd = stringResource(R.string.currency_usd)
    val ils = stringResource(R.string.currency_ils)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = {
                selectedCurrency = usd
                onCurrencySelected(selectedCurrency)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (selectedCurrency == usd) Color.White else Color.LightGray
            )
        ) {
            Text(usd)
        }

        Button(
            onClick = {
                selectedCurrency = ils
                onCurrencySelected(selectedCurrency)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (selectedCurrency == ils) Color.White else Color.LightGray
            )
        ) {
            Text(ils)
        }
    }
}