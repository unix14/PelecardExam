package com.eyal.exam.pelecard.navigation


import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.models.PaymentDetails
import com.eyal.exam.pelecard.repos.NavigationRepository
import com.eyal.exam.pelecard.ui.main.MainScreen
import com.eyal.exam.pelecard.ui.receipt.ReceiptScreen
import com.eyal.exam.pelecard.ui.settings.SettingsScreen
import com.eyal.exam.pelecard.ui.signature.SignatureScreen
import com.google.gson.Gson

@ExperimentalMaterialApi
@Composable
fun NavigationComponent(navRepo: NavigationRepository, navController: NavHostController, modifier: Modifier = Modifier) {
    val gson = Gson()
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
        composable("signature/{paymentDetails}",
            arguments = listOf(navArgument("paymentDetails") { type = NavType.StringType })
        ) { backStackEntry ->
            val paymentDetailsJson = backStackEntry.arguments?.getString("paymentDetails")
            val paymentDetails = paymentDetailsJson?.let { json ->
                // Convert JSON string back to PaymentDetails object
                gson.fromJson(paymentDetailsJson, PaymentDetails::class.java)
            }
            SignatureScreen(paymentDetails)
        }
        composable("receipt/{paymentDetails}",
            arguments = listOf(navArgument("paymentDetails") { type = NavType.StringType })
        ) { backStackEntry ->
            val paymentDetailsJson = backStackEntry.arguments?.getString("paymentDetails")
            Log.d("wow", paymentDetailsJson ?: "Null JSON data")
            val paymentDetails = paymentDetailsJson?.let { json ->
                // Convert JSON string back to PaymentDetails object
                gson.fromJson(paymentDetailsJson, PaymentDetails::class.java)
            }
            ReceiptScreen(paymentDetails)
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
                is NavEvent.NavigateToSignature -> {
                    val paymentDetailsJson = gson.toJson(event.paymentDetails)
                    navController.navigate("signature/$paymentDetailsJson")
                }
                is NavEvent.NavigateToReceipt -> {
                    val paymentDetailsJson = gson.toJson(event.paymentDetails)
                    navController.navigate("receipt/$paymentDetailsJson")
                }
                else -> {}
            }
        }
    }
}