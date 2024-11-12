package com.eyal.exam.pelecard

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PelecardApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialization code if needed
    }
}