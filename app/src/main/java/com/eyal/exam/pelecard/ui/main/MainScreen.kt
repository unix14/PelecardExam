package com.eyal.exam.pelecard.ui.main

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.MainActivity
import com.eyal.exam.pelecard.composables.ActionButton
import com.eyal.exam.pelecard.composables.AnalogClockComposable
import com.eyal.exam.pelecard.models.SettingId
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.utils.AreYouSureDialog

@ExperimentalMaterialApi
@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    val activity = LocalContext.current as? MainActivity
    val showExitDialog = remember { mutableStateOf(false) }

    val settingsConfig by mainViewModel.settingsConfiguration.collectAsState()
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
                    mainViewModel.goToSettings()
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
            isError = paymentDetails.amount <= 0,
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
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )


        // show payments only if the setting is enabled
        if(settingsConfig?.settingsMap?.get(SettingId.PAYMENTS)?.value == true) {
            // show payments
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
                        },) {

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
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box (modifier = Modifier.fillMaxWidth()){
                                        Text(
                                            i.toString(),
                                            modifier = Modifier.align(Alignment.Center)
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        // show currency only if the setting is enabled
        if(settingsConfig?.settingsMap?.get(SettingId.CURRENCY)?.value == true) {
            // show currency
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
                        backgroundColor = if (paymentDetails.currency == "USD") Color.White else Color.LightGray
                    )
                ) {
                    Text("USD")
                }

                Button(
                    onClick = { mainViewModel.updatePaymentDetails(paymentDetails.copy(currency = "ILS")) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (paymentDetails.currency == "ILS") Color.White else Color.LightGray
                    )
                ) {
                    Text("ILS")
                }
            }
        }

        
        // show signature only if the setting is enabled
        if(settingsConfig?.settingsMap?.get(SettingId.SIGNATURE)?.value == true) {
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
        }
        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton(
                text = "Submit",
                color = Color.Green,
                isEnabled = paymentDetails.amount > 0,
                onClick = {

                },
            )
            ActionButton(
                text = "Exit",
                color = Color.Red,
                onClick = {
                    showExitDialog.value = true

                },
            )
        }
    }

    if(showExitDialog.value) {
        AreYouSureDialog(
            title = "Exit",
            subtitle = "Are you sure you want to exit?",
            onConfirm = {
                activity?.finish()
                showExitDialog.value = false
            },
            onDismiss = {
                // do nothing
                showExitDialog.value = false
            }
        )
    }
}