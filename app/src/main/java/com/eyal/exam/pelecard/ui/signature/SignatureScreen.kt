package com.eyal.exam.pelecard.ui.signature

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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.R
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.ui.common_ui.ActionButton
import com.eyal.exam.pelecard.ui.common_ui.AreYouSureDialog
import com.eyal.exam.pelecard.ui.common_ui.LottieProgressBar
import com.eyal.exam.pelecard.ui.common_ui.PeleAppBar
import com.eyal.exam.pelecard.ui.common_ui.SignaturePad

@Composable
fun SignatureScreen(
    paymentId: Int,
    viewModel: SignatureViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState(UiState.Idle)
    var showAreYouSureDialog by remember { mutableStateOf(false) }
    val didStartedSigning = remember { mutableStateOf(false) }
    var savedOffsets = remember<SnapshotStateList<Offset>?> { null }
    var savedDensity = remember<Density?> { null }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PeleAppBar(stringResource(R.string.signature))

        when (uiState) {
            is UiState.Success<*>, UiState.Idle -> {
                // do nothing
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
                        R.string.ui_state_signature_screen_error_msg,
                        uiState::class.java.simpleName,
                        UiState::class.java.simpleName
                    ))
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.please_sign_below),
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
                text = stringResource(R.string.submit),
                color = Color.Green,
                onClick = {
                    if (didStartedSigning.value && savedOffsets != null && savedDensity != null) {
                        viewModel.onSubmitClicked(paymentId, savedOffsets!!, savedDensity!!)
                    } else {
                        Toast.makeText(context, context.getString(R.string.please_sign_first), Toast.LENGTH_SHORT).show()
                    }
                },
            )
            ActionButton(
                text = stringResource(R.string.cancel),
                color = Color.Red,
                onClick = {
                    showAreYouSureDialog = true
                },
            )
        }
    }

    AreYouSureDialog(
        title = stringResource(R.string.are_you_sure),
        subtitle = stringResource(R.string.cancel_payment_msg),
        positiveText = stringResource(R.string.cancel_payment),
        negativeText = stringResource(R.string.let_s_sign),
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