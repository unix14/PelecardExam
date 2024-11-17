package com.eyal.exam.pelecard.ui.common_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.eyal.exam.pelecard.R

@Composable
fun LottieProgressBar() {
    // Load the Lottie composition from a JSON file in the raw resource folder
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_pb_circle))

    // Control the animation state
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever // Loop the animation forever
    )

    // Display the Lottie animation
    Box(modifier = Modifier
        .fillMaxSize()
        // add dark background
        .background(color = Color.Black.copy(alpha = 0.65f))) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(120.dp).align(Alignment.Center)
            )
    }
}
