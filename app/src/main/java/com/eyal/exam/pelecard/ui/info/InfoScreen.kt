package com.eyal.exam.pelecard.ui.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.composables.LottieProgressBar
import com.eyal.exam.pelecard.models.StatusDataResponse
import com.eyal.exam.pelecard.models.UiState

@Composable
fun InfoScreen(
    viewModel: InformationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(UiState.Idle)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) {

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Information",
                style = MaterialTheme.typography.h6,
            )
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Back Icon",
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .clickable {
                        viewModel.navigateBack()
                    }
            )
        }


        Spacer(modifier = Modifier.weight(1f))

        when(uiState) {
            UiState.Idle -> {
              // do nothing
            }
            is UiState.Success<*> -> {
                val data = (uiState as UiState.Success<*>).data
                if(data is StatusDataResponse) {
                    val quotas = data.quotas

                    Text(
                        text = "Account ID: ${data.accountId}",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = "Total: ${quotas.month.total}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Used: ${quotas.month.used}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = "Remaining: ${quotas.month.remaining}",
                        style = MaterialTheme.typography.body1
                    )


                    Spacer(modifier = Modifier.weight(1f))


                    Text(
                        text = "This screen shows the status of the API usage for FreeCurrency API.",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(bottom = 16.dp)
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