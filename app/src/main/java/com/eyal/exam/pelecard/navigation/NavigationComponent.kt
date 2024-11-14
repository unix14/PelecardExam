package com.eyal.exam.pelecard.navigation


import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.repos.NavigationRepository
import com.eyal.exam.pelecard.ui.main.MainScreen
import com.eyal.exam.pelecard.ui.receipt.ReceiptScreen
import com.eyal.exam.pelecard.ui.settings.SettingsScreen
import com.eyal.exam.pelecard.ui.signature.SignatureScreen

@ExperimentalMaterialApi
@Composable
fun NavigationComponent(navRepo: NavigationRepository, navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = modifier
    ) {
        composable("main") {
            MainScreen()
        }
        composable("settings") {
            SettingsScreen()
        }
        composable("signature") {
            SignatureScreen()
        }
        composable("receipt") {
            ReceiptScreen()
        }
        composable("currencyConversion") {
//            CurrencyConversionScreen()
        }
    }
    LaunchedEffect(Unit) {
        navRepo.navigationEvents.collect { event ->
            when (event) {
                is NavEvent.NavigateToMain -> navController.navigate("main")
                is NavEvent.NavigateToSettings -> navController.navigate("settings")
                is NavEvent.NavigateToSignature -> navController.navigate("signature") // todo use nav args / options/ extras
                is NavEvent.NavigateToReceipt -> navController.navigate("receipt") // todo use nav args / options/ extras
                else -> {}
            }
        }
    }
}