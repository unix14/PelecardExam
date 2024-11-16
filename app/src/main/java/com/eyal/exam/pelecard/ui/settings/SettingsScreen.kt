package com.eyal.exam.pelecard.ui.settings

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
import androidx.compose.material.Switch
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

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsConfig by viewModel.settingsConfiguration.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) { // todo extract TO app bar widget

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Settings",
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


        settingsConfig?.settingsMap?.forEach { (id, setting) ->
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(setting.title)
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = setting.value == true,
                    onCheckedChange = { isChecked ->
                        val newSettingsConfig = settingsConfig!!.copy(settingsMap = settingsConfig!!.settingsMap.toMutableMap().apply {
                            this[id] = setting.copy(value = isChecked)
                        })
                        viewModel.updateSettingsConfigurations(newSettingsConfig)
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }

}