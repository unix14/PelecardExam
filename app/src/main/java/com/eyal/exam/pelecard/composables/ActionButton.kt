package com.eyal.exam.pelecard.composables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ActionButton(text: String, color: Color, isEnabled: Boolean = true, onClick: () -> Unit) {
    // Consistent button style
    val buttonModifier = Modifier
        .width(180.dp)
        .height(65.dp)
        .padding(8.dp)
    Button(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
    ) {
        Text(text, color = Color.White)
    }
}