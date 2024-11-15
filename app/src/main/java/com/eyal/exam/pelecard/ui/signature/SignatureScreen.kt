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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.composables.ActionButton
import com.eyal.exam.pelecard.composables.SignaturePad
import com.eyal.exam.pelecard.models.PaymentDetails
import com.eyal.exam.pelecard.utils.AreYouSureDialog
import com.eyal.exam.pelecard.utils.SignatureHelper
import com.eyal.exam.pelecard.utils.SignatureHelper.Companion.saveBitmapToFile
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SignatureScreen (
    paymentDetails: PaymentDetails?,
    viewModel: SignatureViewModel = hiltViewModel()
) {
    val showAreYouSureDialog = remember { mutableStateOf(false) }
    val didStartedSigning = remember { mutableStateOf(false) }
    var _savedOffsets = remember<SnapshotStateList<Offset>?> { null }
    var _savedDensity = remember<Density?>{ null }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Please sign below",
            modifier = Modifier.padding(16.dp)
        )
        /// todo add signature view
        val context = LocalContext.current

        SignaturePad(
            onCanvasChanged = { points, density ->
                didStartedSigning.value = true
                _savedOffsets = points
                _savedDensity = density
            }
        )

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
                    if(didStartedSigning.value && _savedOffsets != null && _savedDensity != null) {

                        ///todo move this code to ViewModel
                        // Capture the signature as an image
                        val bitmap = SignatureHelper.captureSignatureAsBitmap(_savedOffsets!!, _savedDensity!!)
                        val file = saveBitmapToFile(context, bitmap, "signature")

                        // Now you have the signature saved in a file. Use it as needed.
                        Toast.makeText(context, "Signature saved to ${file.absolutePath}", Toast.LENGTH_SHORT).show()

                        val newFilePath = URLEncoder.encode(file.absolutePath, StandardCharsets.UTF_8.toString())
                        val newPaymentDetails = paymentDetails?.copy(signatureFilePath = newFilePath)

                        newPaymentDetails?.let {
                            viewModel.goToNextScreen(newPaymentDetails)
                        } ?: {
                            Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Please sign first", Toast.LENGTH_SHORT).show()
                    }
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