package com.eyal.exam.pelecard.ui.signature

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.composables.ActionButton
import com.eyal.exam.pelecard.composables.SignatureView
import com.eyal.exam.pelecard.models.PaymentDetails
import com.eyal.exam.pelecard.utils.AreYouSureDialog
import com.eyal.exam.pelecard.utils.SignatureHelper.Companion.saveBitmapToFile

@Composable
fun SignatureScreen (
    paymentDetails: PaymentDetails?,
    viewModel: SignatureViewModel = hiltViewModel()
) {
    val showAreYouSureDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Please sign below",
            modifier = Modifier.padding(16.dp)
        )
        /// todo add signature view
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
                onClick = {
                    viewModel.goToNextScreen()
                },
            )
            ActionButton(
                text = "Cancel",
                color = Color.Red,
                onClick = {
                    showAreYouSureDialog.value = true
                },
            )
        }
    }

    if(showAreYouSureDialog.value) {
        AreYouSureDialog(
            title = "Cancel Payment",
            subtitle = "Are you sure you want to cancel this payment?",
            onConfirm = {
                viewModel.goToPreviousScreen()
                showAreYouSureDialog.value = false
            },
            onDismiss = {
                // do nothing
                showAreYouSureDialog.value = false
            }
        )
    }
}