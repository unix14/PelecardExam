package com.eyal.exam.pelecard.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.models.UiState

@ExperimentalMaterialApi
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val uiState by mainViewModel.uiState.collectAsState(UiState.Idle)
    val paymentDetails by mainViewModel.paymentDetails.collectAsState()
    var isAmountOfPaymentsListExpanded by remember { mutableStateOf(false) }
//    var currency by remember { mutableStateOf("USD") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
//        Text(text = uiState.toString())
//        Button(onClick = { mainViewModel.fetchConversionRate("ILS") }) {
//            Text("Fetch Data")
//        }

        TextField(
            value = paymentDetails.amount.toString(),
            label = { Text("Amount") },
            onValueChange = { newValue ->
                mainViewModel.updatePaymentDetails(paymentDetails.copy(amount = newValue.toDouble()))
            }, modifier = Modifier.fillMaxWidth().padding(16.dp),
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Payments:")
            Switch(
                checked = paymentDetails.isPayments,
                onCheckedChange = { isPayments ->
                    mainViewModel.updatePaymentDetails(paymentDetails.copy(isPayments = isPayments))
                },
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            if(paymentDetails.isPayments) {
                ExposedDropdownMenuBox(
                    expanded = isAmountOfPaymentsListExpanded,
                    onExpandedChange = {
                        isAmountOfPaymentsListExpanded = it
                    }) {

                    TextField(
                        value = paymentDetails.numberOfPayments.toString(),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isAmountOfPaymentsListExpanded)
                        },
                        label = { Text("# of payments") },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    )
                    ExposedDropdownMenu(
                        expanded = isAmountOfPaymentsListExpanded,
                        onDismissRequest = {
                            isAmountOfPaymentsListExpanded = false
                        },
                    ) {
                        for (i in 1..12) {
                            DropdownMenuItem(
                                onClick = {
                                    mainViewModel.updatePaymentDetails(paymentDetails.copy(numberOfPayments = i))
                                    isAmountOfPaymentsListExpanded = false
                                }) {
                                Text(i.toString())
                            }
                        }
                    }

                }
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Currency:")

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { mainViewModel.updatePaymentDetails(paymentDetails.copy(currency = "USD")) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (paymentDetails.currency == "USD") Color.LightGray else Color.White
                )
            ) {
                Text("USD")
            }

            Button(
                onClick = { mainViewModel.updatePaymentDetails(paymentDetails.copy(currency = "ILS")) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (paymentDetails.currency == "ILS") Color.LightGray else Color.White
                )
            ) {
                Text("ILS")
            }
        }


        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Signature:")
            Switch(checked = paymentDetails.isSignature, onCheckedChange = { isSignature ->
                mainViewModel.updatePaymentDetails(paymentDetails.copy(isSignature = isSignature))
            })
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {

                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green)) {
                Text("Submit", color = Color.White)
            }
            Button(
                onClick = {
                    //todo finish activity?
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)) {
                Text("Exit", color = Color.White)
            }
        }
    }
}