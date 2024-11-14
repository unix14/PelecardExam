package com.eyal.exam.pelecard.ui.receipt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.composables.ActionButton
import com.eyal.exam.pelecard.models.UiState

@Composable
fun ReceiptScreen (
    viewModel: ReceiptViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(UiState.Idle)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Receipt Screen")
        /// todo add the receipt details
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