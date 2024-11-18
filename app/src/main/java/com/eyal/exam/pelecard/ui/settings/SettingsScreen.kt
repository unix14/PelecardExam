package com.eyal.exam.pelecard.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.eyal.exam.pelecard.ui.common_ui.PeleAppBar
import com.eyal.exam.pelecard.ui.common_ui.SettingsItemRow

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsConfig by viewModel.settingsConfiguration.collectAsState()
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
            stringResource(R.string.settings),
            rightIcon = Icons.AutoMirrored.Filled.ArrowForward,
            rightButtonDescription = stringResource(R.string.back_icon),
            onRightClick = {
                viewModel.navigateBack()
            }
        )

        settingsConfig?.settingsMap?.forEach { (id, setting) ->
            SettingsItemRow(text = setting.title, isOn = setting.value == true) { isChecked ->
                val newSettingsConfig = settingsConfig!!.copy(settingsMap = settingsConfig!!.settingsMap.toMutableMap().apply {
                    this[id] = setting.copy(value = isChecked)
                })
                viewModel.updateSettingsConfigurations(newSettingsConfig)
            }
        }
    }

}