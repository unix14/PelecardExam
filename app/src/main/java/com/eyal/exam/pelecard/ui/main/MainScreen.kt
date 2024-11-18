package com.eyal.exam.pelecard.ui.main

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.MainActivity
import com.eyal.exam.pelecard.R
import com.eyal.exam.pelecard.models.SettingId
import com.eyal.exam.pelecard.ui.common_ui.ActionButton
import com.eyal.exam.pelecard.ui.common_ui.AnalogClockComposable
import com.eyal.exam.pelecard.ui.common_ui.AreYouSureDialog
import com.eyal.exam.pelecard.ui.common_ui.CurrencyPicker
import com.eyal.exam.pelecard.ui.common_ui.PeleAppBar
import com.eyal.exam.pelecard.ui.common_ui.SettingsItemRow

@ExperimentalMaterialApi
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val activity = LocalContext.current as? MainActivity
    var showExitDialog by remember { mutableStateOf(false) }

    val settingsConfig by viewModel.settingsConfiguration.collectAsState()
    val paymentDetails by viewModel.paymentDetails.collectAsState()
    var isAmountOfPaymentsListExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PeleAppBar(
            stringResource(R.string.main),
            leftIcon = Icons.Outlined.Info,
            leftButtonDescription = stringResource(R.string.information_icon),
            onLeftClick = {
                viewModel.goToInfo()
            },
            rightIcon = Icons.Outlined.Settings,
            rightButtonDescription = stringResource(R.string.settings_icon),
            onRightClick = {
                viewModel.goToSettings()
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AnalogClockComposable()
        } else {
            Text(stringResource(R.string.analog_clock_unavailable_error_msg))
        }
        Spacer(modifier = Modifier.weight(1f))

        TextField(
            value = if(paymentDetails.amount != 0.0) {
                paymentDetails.amount.toString()
            } else "",
            isError = paymentDetails.amount <= 0,
            label = { Text(stringResource(R.string.amount)) },
            placeholder = { Text(stringResource(R.string.enter_amount)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
            ),
            singleLine = true,
            onValueChange = { newValue ->
                if(newValue.isEmpty()) {
                    viewModel.updatePaymentDetails(paymentDetails.copy(amount = 0.0))
                }
                else if(newValue.toDoubleOrNull() != null) {
                    viewModel.updatePaymentDetails(paymentDetails.copy(amount = newValue.toDouble()))
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

                SettingsItemRow(text = stringResource(R.string.payments), isOn = paymentDetails.isPayments, isFullWidth = false) { isPayments ->
                    viewModel.updatePaymentDetails(paymentDetails.copy(isPayments = isPayments))
                }

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
                            label = { Text(stringResource(R.string.number_of_payments)) },
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
                                        viewModel.updatePaymentDetails(paymentDetails.copy(numberOfPayments = i))
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
                Text(stringResource(R.string.currency))

                Spacer(modifier = Modifier.weight(1f))

                CurrencyPicker(
                    currency = paymentDetails.currency,
                    onCurrencySelected = { newCurrency ->
                        viewModel.updatePaymentDetails(paymentDetails.copy(currency = newCurrency))
                    }
                )
            }
        }
        
        // show signature only if the setting is enabled
        if(settingsConfig?.settingsMap?.get(SettingId.SIGNATURE)?.value == true) {
            SettingsItemRow(
                text = stringResource(R.string.signature),
                isOn = paymentDetails.isSignature,
                isFullWidth = false,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(16.dp)
            ) { isSignature ->
                    viewModel.updatePaymentDetails(paymentDetails.copy(isSignature = isSignature))
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
                text = stringResource(R.string.submit),
                color = Color.Green,
                isEnabled = paymentDetails.amount > 0,
                onClick = {
                    viewModel.goToNextScreen()
                },
            )
            ActionButton(
                text = stringResource(R.string.exit),
                color = Color.Red,
                onClick = {
                    showExitDialog = true

                },
            )
        }
    }

    AreYouSureDialog(
        title = stringResource(R.string.exit_app),
        subtitle = stringResource(R.string.exit_app_msg),
        onConfirm = {
            activity?.finish()
            showExitDialog = false
        },
        onDismiss = {
            // do nothing
            showExitDialog = false
        },
        enabled = showExitDialog
    )
}