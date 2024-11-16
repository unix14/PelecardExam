package com.eyal.exam.pelecard.navigation


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.eyal.exam.pelecard.MainActivity
import com.eyal.exam.pelecard.abs.Constants
import com.eyal.exam.pelecard.models.ConversionScreenParams
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.models.PaymentDetails
import com.eyal.exam.pelecard.models.RouteName
import com.eyal.exam.pelecard.repos.NavigationRepository
import com.eyal.exam.pelecard.ui.conversion.ConversionScreen
import com.eyal.exam.pelecard.ui.info.InfoScreen
import com.eyal.exam.pelecard.ui.main.MainScreen
import com.eyal.exam.pelecard.ui.receipt.ReceiptScreen
import com.eyal.exam.pelecard.ui.settings.SettingsScreen
import com.eyal.exam.pelecard.ui.signature.SignatureScreen
import com.eyal.exam.pelecard.utils.AreYouSureDialog
import com.eyal.exam.pelecard.utils.NavigationHelper
import com.eyal.exam.pelecard.utils.getReadableRouteName
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

private val TAG = "NavigationComponent"

@ExperimentalMaterialApi
@Composable
fun NavigationComponent(
    navRepo: NavigationRepository,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as? MainActivity // todo refactor out of here
    var showExitDialog by remember { mutableStateOf(false) }
    var currentRoute by remember { mutableStateOf<String?>(Constants.DEFAULT_STARTING_ROUTE_NAME) }
    val gson = Gson()
    NavHost(
        navController = navController,
        startDestination = Constants.DEFAULT_STARTING_ROUTE_NAME,
        modifier = modifier
    ) {
        composable(RouteName.MAIN.name.lowercase()) {
            MainScreen()
        }
        composable(RouteName.SETTINGS.name.lowercase()) {
            SettingsScreen()
        }
        composable("${RouteName.SIGNATURE.name.lowercase()}/{${Constants.NAV_PARAM_PAYMENT_DETAILS}}",
            arguments = listOf(navArgument(Constants.NAV_PARAM_PAYMENT_DETAILS) { type = NavType.StringType })
        ) { backStackEntry ->
            val paymentDetailsJson = backStackEntry.arguments?.getString(Constants.NAV_PARAM_PAYMENT_DETAILS)
            val paymentDetails = paymentDetailsJson?.let { json ->
                // Convert JSON string back to PaymentDetails object
                gson.fromJson(paymentDetailsJson, PaymentDetails::class.java)
            }
            paymentDetails?.let {
                SignatureScreen(it)
            }
        }
        composable("${RouteName.RECEIPT.name.lowercase()}/{${Constants.NAV_PARAM_PAYMENT_DETAILS}}",
            arguments = listOf(navArgument(Constants.NAV_PARAM_PAYMENT_DETAILS) { type = NavType.StringType })
        ) { backStackEntry ->
            val paymentDetailsJson = backStackEntry.arguments?.getString(Constants.NAV_PARAM_PAYMENT_DETAILS)
            val paymentDetails = paymentDetailsJson?.let { json ->
                // Convert JSON string back to PaymentDetails object
                gson.fromJson(paymentDetailsJson, PaymentDetails::class.java)
            }
            paymentDetails?.let {
                ReceiptScreen(it)
            }
        }
        composable("${RouteName.CONVERSION.name.lowercase()}/{${Constants.NAV_PARAM_CONVERSION_SCREEN_PARAMS}}",
            arguments = listOf(navArgument(Constants.NAV_PARAM_CONVERSION_SCREEN_PARAMS) { type = NavType.StringType })
        ) { backStackEntry -> //todo extract to function in NavigationHelper
            val conversionScreenJson = backStackEntry.arguments?.getString(Constants.NAV_PARAM_CONVERSION_SCREEN_PARAMS)
            val conversionScreenParams = conversionScreenJson?.let { json ->
                // Convert JSON string back to PaymentDetails object
                gson.fromJson(conversionScreenJson, ConversionScreenParams::class.java)
            }
            conversionScreenParams?.let {
                ConversionScreen(it)
            }
        }
        composable(RouteName.INFO.name.lowercase()) {
            InfoScreen()
        }
    }
    LaunchedEffect(Unit) {
        navRepo.navigationEvents.collect { event ->
            currentRoute = event.routeName.name.lowercase()
            when (event) {
                is NavEvent.NavigateToMain -> NavigationHelper.replace(navController, currentRoute!!, null)
                is NavEvent.NavigateToSettings -> NavigationHelper.navigate(navController, currentRoute!!, null)
                is NavEvent.NavigateToSignature -> NavigationHelper.replace(navController, currentRoute!!, event.paymentDetails)
                is NavEvent.NavigateToReceipt -> NavigationHelper.replace(navController, currentRoute!!, event.paymentDetails)
                is NavEvent.NavigateToConversion -> NavigationHelper.navigate(navController, currentRoute!!, event.screenParams)
                is NavEvent.NavigateToInfo -> NavigationHelper.navigate(navController, currentRoute!!, null)
                else -> {
                    throw Error("Unimplemented Navigation: type ${event::class.java.simpleName} of ${NavEvent::class.java.simpleName} is not implemented for NavigationComponent()")
                }
            }
        }
    }
    // Handle back presses -> Do nothing or show a confirmation dialog, if needed
    BackHandler(enabled = currentRoute != Constants.DEFAULT_STARTING_ROUTE_NAME) {
        Log.d(TAG, "BackHandler: Click back from device on route: ${navController.currentDestination?.route}")
        // Attempt to pop the back stack
        val isPopped = navController.popBackStack()
        if(!isPopped) {
            // There's no more screen behind
            showExitDialog = true
        } else {
            currentRoute = navController.currentDestination?.route
        }
    }

    AreYouSureDialog(
        title = "Exit Dialog",
        subtitle = "Are you sure you want to exit ${getReadableRouteName(currentRoute) ?: "this"} screen?",
        positiveText = "Exit",
        negativeText = "Cancel",
        enabled = showExitDialog,
        onConfirm = {
            if(currentRoute == Constants.DEFAULT_STARTING_ROUTE_NAME) {
                activity?.finish()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    navRepo.navigateTo(NavEvent.NavigateToMain)
                }
            }
            showExitDialog = false
        },
        onDismiss = {
            // do nothing
            showExitDialog = false
        }
    )

}