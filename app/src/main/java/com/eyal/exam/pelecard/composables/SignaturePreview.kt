package com.eyal.exam.pelecard.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter
import java.io.File

@Composable
fun SignaturePreview(filePath: String) {
    // Check if the file path is valid
    if (filePath.isNotBlank()) {
        val file = File(filePath)
        
        if (file.exists()) {
            // Use Coil to load the image from the file
            Image(
                painter = rememberAsyncImagePainter(model = file),
                contentDescription = "Saved Signature",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // Adjust aspect ratio if needed
            )
        } else {
            Text("File not found at: $filePath")
        }
    } else {
        Text("No signature file path provided")
    }
}