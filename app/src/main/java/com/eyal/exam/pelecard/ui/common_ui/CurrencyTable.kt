package com.eyal.exam.pelecard.ui.common_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.eyal.exam.pelecard.models.ConversionScreenParams
import com.eyal.exam.pelecard.network.CurrencyConversionResponse
import com.eyal.exam.pelecard.helpers.getNumberFormat

@Composable
fun CurrencyTable(
    conversionRates: CurrencyConversionResponse,
    screenParams: ConversionScreenParams
) {
    val rates = conversionRates.rates?.toList()

    LazyColumn {
        items(rates?.size ?: 0) { index ->
            val rate: Double = rates?.get(index)?.second ?: 0.0
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = rates?.get(index)?.first ?: "",
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = (screenParams.amount * rate).getNumberFormat(),
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.End
                )
            }
            Divider()
        }
    }
}