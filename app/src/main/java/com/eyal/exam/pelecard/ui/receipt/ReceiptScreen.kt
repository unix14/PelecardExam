package com.eyal.exam.pelecard.ui.receipt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.composables.ActionButton
import com.eyal.exam.pelecard.composables.ReceiptDetail
import com.eyal.exam.pelecard.composables.SignaturePreview
import com.eyal.exam.pelecard.models.PaymentDetails
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.utils.getNumberFormat
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.text.DecimalFormat

@Composable
fun ReceiptScreen (
    paymentDetails: PaymentDetails,
    viewModel: ReceiptViewModel = hiltViewModel()
) {
    // todo use ui state
    val uiState by viewModel.uiState.collectAsState(UiState.Idle)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text("Receipt", fontSize = 22.sp)
            ReceiptDetail(key = "Amount:", value = getNumberFormat(paymentDetails.amount))
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
                val filePathString = URLDecoder.decode(paymentDetails.signatureFilePath, StandardCharsets.UTF_8.toString())
                SignaturePreview(filePath = filePathString, modifier = Modifier.padding(top= 20.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

        ActionButton(
            text = "Convert",
            color = Color.Cyan,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                viewModel.onConvertClicked(paymentDetails)
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