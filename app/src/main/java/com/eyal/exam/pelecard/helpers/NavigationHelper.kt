package com.eyal.exam.pelecard.helpers

import android.util.Log
import androidx.navigation.NavController

class NavigationHelper {
    companion object {
        const val TAG = "NavigationHelper"

        fun navigate(navController: NavController, nextDestination: String, extraParam: String? = null) {
            var finalDestination = nextDestination

            // If should pass an extra parameter
            if(extraParam != null) {
                finalDestination += "/$extraParam"
            }

            // navigate to finalDestination
            navController.navigate(finalDestination)
            Log.d(TAG, "navigate: navigate to $finalDestination")

        }

        fun replace(navController: NavController, nextDestination: String, extraParam: String? = null) {
            // Attempt to pop the back stack
            val isPopped = navController.popBackStack()
            Log.d(TAG, "replace: isPopped is $isPopped")
            if (!isPopped) {
                // This is the first screen
                navigate(navController, nextDestination, extraParam)
            }
        }
    }
}