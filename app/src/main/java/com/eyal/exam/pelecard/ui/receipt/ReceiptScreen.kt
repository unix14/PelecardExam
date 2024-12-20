package com.eyal.exam.pelecard.ui.receipt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.R
import com.eyal.exam.pelecard.data.entities.PaymentDetails
import com.eyal.exam.pelecard.helpers.getNumberFormat
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.ui.common_ui.ActionButton
import com.eyal.exam.pelecard.ui.common_ui.LottieProgressBar
import com.eyal.exam.pelecard.ui.common_ui.PeleAppBar
import com.eyal.exam.pelecard.ui.common_ui.ReceiptDetail
import com.eyal.exam.pelecard.ui.common_ui.SignaturePreview

@Composable
fun ReceiptScreen (
    paymentId: Int,
    viewModel: ReceiptViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(UiState.Idle)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        PeleAppBar(stringResource(R.string.receipt))

        when (uiState) {
            UiState.Idle -> {
                // First time entering the screen -> will load the latest currencies list
                viewModel.loadPaymentId(paymentId)
            }
            is UiState.Success<*> -> {
                // show results
                val paymentDetails = (uiState as UiState.Success<*>).data

                if(paymentDetails is PaymentDetails) {
                    Spacer(modifier = Modifier.weight(1f))
                    ReceiptDetail(key = stringResource(R.string.amount), value = paymentDetails.amount.getNumberFormat())
                    if(paymentDetails.isPayments) {
                        ReceiptDetail(
                            key = stringResource(R.string.payments),
                            value = paymentDetails.numberOfPayments.toString()
                        )
                    }
                    if(paymentDetails.isCurrency) {
                        ReceiptDetail(key = stringResource(R.string.currency), value = paymentDetails.currency)
                    }

                    if(paymentDetails.isSignature) {
                        Text(stringResource(R.string.signature), fontSize = 18.sp, modifier = Modifier.padding(top= 20.dp))
                        SignaturePreview(filePath = paymentDetails.signatureFilePath ?: "", modifier = Modifier.padding(top= 20.dp))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    ActionButton(
                        text = stringResource(R.string.convert),
                        color = Color.Cyan,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            paymentDetails.let { viewModel.onConvertClicked(it) }
                        },
                    )
                    ActionButton(
                        text = stringResource(R.string.finish),
                        color = Color.Green,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        onClick = {
                            viewModel.onFinishedClicked(paymentDetails)
                        },
                    )
                }
            }
            is UiState.Error -> {
                Text((uiState as UiState.Error).message)
            }
            UiState.Loading -> {
                LottieProgressBar()
            }
            else -> {
                throw Error(
                    stringResource(
                        R.string.ui_state_receipt_screen_error_msg,
                        uiState::class.java.simpleName,
                        UiState::class.java.simpleName
                    ))
            }
        }
    }
}