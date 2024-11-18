package com.eyal.exam.pelecard.repos

import com.eyal.exam.pelecard.abs.Constants
import com.eyal.exam.pelecard.data.entities.SettingsConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor() {

    private val _settingsConfiguration = MutableSharedFlow<SettingsConfig>(replay = 1)
    val settingsConfiguration = _settingsConfiguration.asSharedFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {


            ///todo fetch settings from shared preferences or room DB
            _settingsConfiguration.emit(Constants.DEFAULT_SETTINGS_CONFIG)
        }
    }

    suspend fun updateSettingsConfigurations(settingsConfig: SettingsConfig) {
        _settingsConfiguration.emit(settingsConfig)
    }
}