package com.eyal.exam.pelecard.ui.signature

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    paymentDetails: PaymentDetails,
    viewModel: SignatureViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var showAreYouSureDialog by remember { mutableStateOf(false) }
    val didStartedSigning = remember { mutableStateOf(false) }
    var savedOffsets = remember<SnapshotStateList<Offset>?> { null }
    var savedDensity = remember<Density?>{ null }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "Please sign below",
            modifier = Modifier.padding(16.dp)
        )

        SignaturePad(
            onCanvasChanged = { points, density ->
                didStartedSigning.value = true
                savedOffsets = points
                savedDensity = density
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
                    if(didStartedSigning.value && savedOffsets != null && savedDensity != null) {

                        ///todo move this code to ViewModel // todo use ui state for saving the file + pb lottie
                        // Capture the signature as an image
                        val bitmap = SignatureHelper.captureSignatureAsBitmap(savedOffsets!!, savedDensity!!)
                        val file = saveBitmapToFile(context, bitmap, "signature_") // todo create unique file name to be saved

                        val newFilePath = URLEncoder.encode(file.absolutePath, StandardCharsets.UTF_8.toString())
                        val newPaymentDetails = paymentDetails.copy(signatureFilePath = newFilePath)

                        Log.d("wow", "SignatureScreen: Encoded String is $newFilePath")

                        viewModel.goToNextScreen(newPaymentDetails)
                    } else {
                        Toast.makeText(context, "Please sign first", Toast.LENGTH_SHORT).show()
                    }
                },
            )
            ActionButton(
                text = "Cancel",
                color = Color.Red,
                onClick = {
                    showAreYouSureDialog = true
                },
            )
        }
    }

    AreYouSureDialog(
        title = "Cancel Payment",
        subtitle = "Are you sure you want to cancel this payment?",
        positiveText = "Cancel Payment",
        negativeText = "Let's Sign",
        enabled = showAreYouSureDialog,
        onConfirm = {
            viewModel.goToPreviousScreen()
            showAreYouSureDialog = false
        },
        onDismiss = {
            // do nothing
            showAreYouSureDialog = false
        }
    )
}