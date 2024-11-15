package com.eyal.exam.pelecard.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density
import java.io.File
import java.io.FileOutputStream

class SignatureHelper {
    companion object {

        fun captureSignatureAsBitmap(points: List<Offset>, density: Density): Bitmap {
            val width = 800 // Adjust the width of the Bitmap as needed
            val height = 400 // Adjust the height of the Bitmap as needed

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)
            val paint = Paint().apply {
                color = Color.BLACK
                strokeWidth = 4f * density.density
                isAntiAlias = true
            }

            for (i in 1 until points.size) {
                canvas.drawLine(
                    points[i - 1].x,
                    points[i - 1].y,
                    points[i].x,
                    points[i].y,
                    paint
                )
            }

            return bitmap
        }

        fun saveBitmapToFile(context: Context, bitmap: Bitmap, fileName: String): File {
            val file = File(context.cacheDir, "$fileName.png")
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            return file
        }

    }
}