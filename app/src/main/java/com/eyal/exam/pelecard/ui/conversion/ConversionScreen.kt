package com.eyal.exam.pelecard.ui.conversion

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.ui.common_ui.CurrencyPicker
import com.eyal.exam.pelecard.ui.common_ui.CurrencyTable
import com.eyal.exam.pelecard.ui.common_ui.LottieProgressBar
import com.eyal.exam.pelecard.models.ConversionScreenParams
import com.eyal.exam.pelecard.network.CurrencyConversionResponse
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.ui.common_ui.PeleAppBar

@Composable
fun ConversionScreen(
    screenParams: ConversionScreenParams,
    viewModel: ConversionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(UiState.Idle)
    var amount by remember { mutableDoubleStateOf(screenParams.amount) }
    var currency by remember { mutableStateOf(screenParams.currency) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PeleAppBar("Conversion Rates",
            rightIcon = Icons.AutoMirrored.Filled.ArrowForward,
            rightButtonDescription = "Back Icon",
            onRightClick = {
                viewModel.navigateBack()
            }
        )

        when (uiState) {
            UiState.Idle -> {
                // First time entering the screen -> will load the latest currencies list
                viewModel.fetchConversionRate(currency)
            }
            is UiState.Success<*> -> {
                // show results
                val results = (uiState as UiState.Success<*>).data
                if(results is CurrencyConversionResponse) {

                    Row(
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextField(value = amount.toString(), onValueChange = { newAmount ->
                            amount = newAmount.toDoubleOrNull() ?: 0.0
                        }, modifier = Modifier.width(120.dp))

                        Box(modifier = Modifier.padding(horizontal = 28.dp))

                        CurrencyPicker(
                            currency = currency,
                            onCurrencySelected = { newCurrency ->
                                currency = newCurrency
                                viewModel.fetchConversionRate(currency)
                            }
                        )
                    }

                    Box(modifier = Modifier
                        .height(380.dp)
                        .border(border = BorderStroke(width = 6.dp, color = Color.Gray))) {
                        CurrencyTable(results, screenParams.copy(amount = amount))
                    }
                } else {
                    Text("Error from server")
                }

                Button(onClick = { viewModel.fetchConversionRate(currency) }) {
                    Text("Refresh")
                }
            }
            is UiState.Error -> {
                Text((uiState as UiState.Error).message)
            }
            UiState.Loading -> {
                LottieProgressBar()
            }
            else -> {
                throw Error("Unimplemented UiState: type ${uiState::class.java.simpleName} of ${UiState::class.java.simpleName} is not implemented for ConversionScreen()")
            }
        }
    }
}