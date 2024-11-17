package com.eyal.exam.pelecard.ui.common_ui

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AreYouSureDialog(
    title: String,
    subtitle: String,
    positiveText: String = "Yes",
    negativeText: String = "No",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    enabled: Boolean = true,
) {
    var isDialogShowing = enabled
    if(isDialogShowing) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(title) },
            text = { Text(subtitle) },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                    isDialogShowing = false
                }) {
                    Text(positiveText)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
                    isDialogShowing = false
                }) {
                    Text(negativeText)
                }
            }
        )
    }}