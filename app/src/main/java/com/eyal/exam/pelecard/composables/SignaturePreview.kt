package com.eyal.exam.pelecard.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import java.io.File

@Composable
fun SignaturePreview(filePath: String, modifier: Modifier) {
    // Check if the file path is valid
    if (filePath.isNotBlank()) {
        val file = File(filePath)
        
        if (file.exists()) {
            // Use Coil to load the image from the file
            Image(
                painter = rememberAsyncImagePainter(model = file),
                contentDescription = "Saved Signature",
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f, matchHeightConstraintsFirst = false)
                    .border(2.dp, Color.Black)
            )
        } else {
            Text("File not found at: $filePath")
        }
    } else {
        Text("No signature file path provided")
    }
}