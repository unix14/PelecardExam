package com.eyal.exam.pelecard.helpers

import android.util.Log
import androidx.navigation.NavController

class NavigationHelper {
    companion object {

        const val TAG = "NavigationHelper" // todo add logs
        fun navigate(navController: NavController, nextDestination: String, extraParam: String? = null) {
            var finalDestination = nextDestination

            // If should pass an extra parameter
            if(extraParam != null) {
                finalDestination += "/$extraParam"
            }

            Log.d(TAG, "finalDestination is $finalDestination")

            // navigate to finalDestination
            navController.navigate(finalDestination)
        }

        fun replace(navController: NavController, nextDestination: String, extraParam: String? = null) {
            // Attempt to pop the back stack
            val isPopped = navController.popBackStack()
            if (!isPopped) {
                // This is the first screen
                navigate(navController, nextDestination, extraParam) /// todo
            }
        }
    }
}