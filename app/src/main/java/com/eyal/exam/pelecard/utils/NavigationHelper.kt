package com.eyal.exam.pelecard.utils

import androidx.navigation.NavController
import com.google.gson.Gson

class NavigationHelper {

    companion object {

        fun <T> navigate(navController: NavController, nextDestination: String, extraParam: T? = null) {
            val gson = Gson()
            var finalDestination = nextDestination
            val extraParamAsJson = gson.toJson(extraParam)

            // If should pass an extra parameter
            if(extraParam != null) {
                finalDestination += "/$extraParamAsJson"
            }

            // If the back stack couldn't be popped, navigate to nextDestination
            navController.navigate(finalDestination)
        }

        fun <T> replace(navController: NavController, nextDestination: String, extraParam: T? = null) {
            // Attempt to pop the back stack
            val isPopped = navController.popBackStack()
            if (!isPopped) {
                // This is the first screen
                navigate(navController, nextDestination, extraParam)
            }
        }
    }
}