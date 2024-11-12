package com.eyal.exam.pelecard.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavigationComponent(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = modifier
    ) {
        composable("main") {
//            MainScreen()
        }
        composable("settings") {
//            SettingsScreen()
        }
        composable("signature") {
//            SignatureScreen()
        }
        composable("receipt") {
//            ReceiptScreen()
        }
        composable("currencyConversion") {
//            CurrencyConversionScreen()
        }
    }
}