package com.eyal.exam.pelecard.ui.navigation


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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.eyal.exam.pelecard.MainActivity
import com.eyal.exam.pelecard.abs.Constants
import com.eyal.exam.pelecard.helpers.NavigationHelper
import com.eyal.exam.pelecard.helpers.getReadableRouteName
import com.eyal.exam.pelecard.models.ConversionScreenParams
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.models.RouteName
import com.eyal.exam.pelecard.repos.NavigationRepository
import com.eyal.exam.pelecard.ui.common_ui.AreYouSureDialog
import com.eyal.exam.pelecard.ui.conversion.ConversionScreen
import com.eyal.exam.pelecard.ui.info.InfoScreen
import com.eyal.exam.pelecard.ui.main.MainScreen
import com.eyal.exam.pelecard.ui.receipt.ReceiptScreen
import com.eyal.exam.pelecard.ui.settings.SettingsScreen
import com.eyal.exam.pelecard.ui.signature.SignatureScreen
import com.eyal.exam.pelecard.ui.signature.SignatureViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        composable("${RouteName.SIGNATURE.name.lowercase()}/{${Constants.NAV_PARAM_PAYMENT_ID}}",
            arguments = listOf(navArgument(Constants.NAV_PARAM_PAYMENT_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val paymentId = backStackEntry.arguments?.getInt(Constants.NAV_PARAM_PAYMENT_ID) ?: 0
            SignatureScreen(paymentId)
        }
        composable("${RouteName.RECEIPT.name.lowercase()}/{${Constants.NAV_PARAM_PAYMENT_ID}}",
            arguments = listOf(navArgument(Constants.NAV_PARAM_PAYMENT_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val paymentId = backStackEntry.arguments?.getInt(Constants.NAV_PARAM_PAYMENT_ID) ?: 0
            ReceiptScreen(paymentId)
        }
        composable("${RouteName.CONVERSION.name.lowercase()}/{${Constants.NAV_PARAM_CONVERSION_AMOUNT}}/{${Constants.NAV_PARAM_CONVERSION_CURRENCY}}",
            arguments = listOf(navArgument(Constants.NAV_PARAM_CONVERSION_AMOUNT) { type = NavType.FloatType },
                navArgument(Constants.NAV_PARAM_CONVERSION_CURRENCY) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val amount = backStackEntry.arguments?.getFloat(Constants.NAV_PARAM_CONVERSION_AMOUNT)?.toDouble() ?: 0.0
            Log.d(SignatureViewModel.TAG, "amount: amount $amount")

            val currency = backStackEntry.arguments?.getString(Constants.NAV_PARAM_CONVERSION_CURRENCY) ?: Constants.DEFAULT_CURRENCY
            ConversionScreen(ConversionScreenParams(amount, currency))
        }
        composable(RouteName.INFO.name.lowercase()) {
            InfoScreen()
        }
    }
    LaunchedEffect(Unit) {
        navRepo.navigationEvents.collect { event ->
            currentRoute = event.routeName.name.lowercase()
            when (event) {
                is NavEvent.NavigateToMain ->
                    NavigationHelper.replace(navController, currentRoute!!, null)
                is NavEvent.NavigateToSettings ->
                    NavigationHelper.navigate(navController, currentRoute!!, null)
                is NavEvent.NavigateToSignature ->
                    NavigationHelper.replace(navController, currentRoute!!, event.paymentId.toString())
                is NavEvent.NavigateToReceipt ->
                    NavigationHelper.replace(navController, currentRoute!!, event.paymentId.toString())
                is NavEvent.NavigateToConversion ->
                    NavigationHelper.navigate(navController, currentRoute!!, "${event.amount}/${event.currency}")
                is NavEvent.NavigateToInfo ->
                    NavigationHelper.navigate(navController, currentRoute!!, null)
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
        subtitle = "Are you sure you want to exit ${currentRoute.getReadableRouteName()} screen?",
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