package com.eyal.exam.pelecard.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.eyal.exam.pelecard.navigation.NavigationComponent

@Composable
fun PelecardExamApp() {
    val navController = rememberNavController()
    Scaffold { contentPadding ->
        NavigationComponent(
            navController = navController,
            modifier = Modifier.padding(contentPadding)
        )
    }
}