package com.eyal.exam.pelecard.ui.common_ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PeleAppBar(
    title: String,

    leftIcon: ImageVector? = null,
    leftButtonDescription: String = "",
    onLeftClick: () -> Unit = {},

    rightIcon: ImageVector? = null,
    rightButtonDescription: String = "",
    onRightClick: () -> Unit = {},

) {
    TopAppBar(
        title = {
            Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = title, fontSize = 24.sp)
        } },
        backgroundColor = Color.White,
        contentColor = Color.Black,
        elevation = 4.dp,
        // Add a button on the left using navigationIcon
        navigationIcon = {
            IconButton(onClick = onLeftClick) {
                if(leftIcon != null) {
                    Icon(
                        imageVector = leftIcon,
                        contentDescription = leftButtonDescription
                    )
                }
            }
        },
        // Add one or more buttons on the right using actions
        actions = {
            IconButton(onClick = onRightClick) {
                if(rightIcon != null) {
                    Icon(
                        imageVector = rightIcon,
                        contentDescription = rightButtonDescription
                    )
                }
            }
        }
    )
}
