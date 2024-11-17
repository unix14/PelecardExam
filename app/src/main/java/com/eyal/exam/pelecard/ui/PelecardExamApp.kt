package com.eyal.exam.pelecard.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.eyal.exam.pelecard.ui.navigation.NavigationComponent
import com.eyal.exam.pelecard.repos.NavigationRepository

@ExperimentalMaterialApi
@Composable
fun PelecardExamApp(navigationRepository: NavigationRepository) {
    val navController = rememberNavController()
    Scaffold { contentPadding ->
        NavigationComponent(
            navRepo = navigationRepository,
            navController = navController,
            modifier = Modifier.padding(contentPadding)
        )
    }
}