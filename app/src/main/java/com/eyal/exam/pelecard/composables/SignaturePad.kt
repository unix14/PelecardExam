package com.eyal.exam.pelecard.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

@Composable
fun SignaturePad(modifier: Modifier = Modifier, onCanvasChanged: (SnapshotStateList<Offset>, Density) -> Unit) {
    val points = remember { mutableStateListOf<Offset>() }
    val density = LocalDensity.current

    Box(modifier = modifier.width(330.dp).height(180.dp).background(Color.White).border(2.dp, Color.Black)) {
        Canvas(modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val position = change.position
                    // Only add points within the bounds of the Canvas
                    if (position.x in 0f..size.width.toFloat() && position.y in 0f..size.height.toFloat()) {
                        points.add(position)
                        onCanvasChanged(points, density)
                    }
                }
            }) {
            for (i in 1 until points.size) {
                drawLine(
                    color = Color.Black,
                    start = points[i - 1],
                    end = points[i],
                    strokeWidth = 4.dp.toPx()
                )
            }
        }
    }
}