package com.eyal.exam.pelecard.ui.receipt

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.ui.common_ui.ActionButton
import com.eyal.exam.pelecard.ui.common_ui.ReceiptDetail
import com.eyal.exam.pelecard.ui.common_ui.SignaturePreview
import com.eyal.exam.pelecard.data.entities.PaymentDetails
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.ui.common_ui.PeleAppBar
import com.eyal.exam.pelecard.helpers.getNumberFormat
import com.eyal.exam.pelecard.network.CurrencyConversionResponse
import com.eyal.exam.pelecard.ui.common_ui.CurrencyPicker
import com.eyal.exam.pelecard.ui.common_ui.CurrencyTable
import com.eyal.exam.pelecard.ui.common_ui.LottieProgressBar
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun ReceiptScreen (
    paymentId: Int,
    viewModel: ReceiptViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState(UiState.Idle)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        PeleAppBar("Receipt",)

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
                    ReceiptDetail(key = "Amount:", value = paymentDetails.amount.getNumberFormat())
                    if(paymentDetails.isPayments) {
                        ReceiptDetail(
                            key = "Payments:",
                            value = paymentDetails.numberOfPayments.toString()
                        )
                    }
                    if(paymentDetails.currency.isNotEmpty()) {
                        ReceiptDetail(key = "Currency:", value = paymentDetails.currency)
                    }

                    if(paymentDetails.isSignature) {
                        Text("Signature:", fontSize = 18.sp, modifier = Modifier.padding(top= 20.dp))
//                        val filePathString = URLDecoder.decode(paymentDetails.signatureFilePath, StandardCharsets.UTF_8.toString()) // todo remove
                        SignaturePreview(filePath = paymentDetails.signatureFilePath ?: "", modifier = Modifier.padding(top= 20.dp))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    ActionButton(
                        text = "Convert",
                        color = Color.Cyan,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {
                            paymentDetails.let { viewModel.onConvertClicked(it) }
                        },
                    )
                    ActionButton(
                        text = "Finish",
                        color = Color.Green,
                        modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                        onClick = {
                            viewModel.onFinishedClicked()
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
                throw Error("Unimplemented UiState: type ${uiState::class.java.simpleName} of ${UiState::class.java.simpleName} is not implemented for ConversionScreen()")
            }
        }
    }
}