package com.eyal.exam.pelecard.ui.common_ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsItemRow(text: String, isOn: Boolean, isFullWidth: Boolean = true, onCheckedChange: ((Boolean) -> Unit)?) {
    Row(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .clickable(
                onClick = {
                    if (onCheckedChange != null) {
                        onCheckedChange(!isOn)
                    }
                }
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text)
        if(isFullWidth) {
            Spacer(modifier = Modifier.weight(1f))
        }
        Switch(
            checked = isOn,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}