package com.eyal.exam.pelecard.ui.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyal.exam.pelecard.R
import com.eyal.exam.pelecard.models.UiState
import com.eyal.exam.pelecard.network.StatusDataResponse
import com.eyal.exam.pelecard.ui.common_ui.LottieProgressBar
import com.eyal.exam.pelecard.ui.common_ui.PeleAppBar

@Composable
fun InfoScreen(
    viewModel: InformationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState(UiState.Idle)
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        PeleAppBar(
            stringResource(R.string.information),
            rightIcon = Icons.AutoMirrored.Filled.ArrowForward,
            rightButtonDescription = stringResource(R.string.back_icon),
            onRightClick = {
                viewModel.navigateBack()
            }
        )

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
                        text = stringResource(R.string.account_id, data.accountId),
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = stringResource(R.string.total, quotas.month.total),
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = stringResource(R.string.used, quotas.month.used),
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = stringResource(R.string.remaining, quotas.month.remaining),
                        style = MaterialTheme.typography.body1
                    )


                    Spacer(modifier = Modifier.weight(1f))


                    Text(
                        text = stringResource(R.string.info_screen_msg),
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
                throw Error(
                    stringResource(
                        R.string.ui_state_info_screen_error_msg,
                        uiState::class.java.simpleName,
                        UiState::class.java.simpleName
                    )
                )
            }
        }
    }
}