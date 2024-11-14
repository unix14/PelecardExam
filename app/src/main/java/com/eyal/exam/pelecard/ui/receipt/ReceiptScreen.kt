package com.eyal.exam.pelecard.ui.receipt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.eyal.exam.pelecard.models.UiState

@Composable
fun ReceiptScreen (
    viewModel: ReceiptViewModel = hiltViewModel()
) {
    // todo use ui state
    val uiState by viewModel.uiState.collectAsState(UiState.Idle)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Receipt", fontSize = 22.sp)
            /// todo add the receipt details
            ReceiptDetail(key = "Amount:", value ="aaaa")
            ReceiptDetail(key = "Payments:", value ="aaaa")
            ReceiptDetail(key = "Currency:", value ="aaaa")

            Text("Signature:", fontSize = 18.sp)
            /// todo add signature view

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(
                    text = "Finish",
                    color = Color.Green,
                    onClick = {
                        viewModel.onFinishedClicked()
                    },
                )
            }
    }
}