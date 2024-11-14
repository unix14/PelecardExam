package com.eyal.exam.pelecard.ui.main

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.composables.AnalogClockComposable
import com.eyal.exam.pelecard.models.UiState

@ExperimentalMaterialApi
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val uiState by mainViewModel.uiState.collectAsState(UiState.Idle)
    val paymentDetails by mainViewModel.paymentDetails.collectAsState()
    var isAmountOfPaymentsListExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
//        Text(text = uiState.toString())
//        Button(onClick = { mainViewModel.fetchConversionRate("ILS") }) {
//            Text("Fetch Data")
//        }

        Icon(
            imageVector = Icons.Outlined.Settings,
            contentDescription = "Settings Icon",
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    //todo navigate to settings screen
                }
        )

        Spacer(modifier = Modifier.weight(1f))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AnalogClockComposable()
        } else {
            Text("Analog clock is not supported on this device")
        }
        Spacer(modifier = Modifier.weight(1f))

        TextField(
            value = if(paymentDetails.amount != 0) {
                paymentDetails.amount.toString()
            } else "",
            label = { Text("Amount") },
            placeholder = { Text("Enter amount") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            singleLine = true,
            onValueChange = { newValue ->
                if(newValue.isEmpty()) {
                    mainViewModel.updatePaymentDetails(paymentDetails.copy(amount = 0))
                }
                else if(newValue.toIntOrNull() != null) {
                    mainViewModel.updatePaymentDetails(paymentDetails.copy(amount = newValue.toInt()))
                }
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
                    },
                    modifier = Modifier.defaultMinSize()
                    ) {

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
                .fillMaxWidth()
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