package com.eyal.exam.pelecard.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyal.exam.pelecard.data.entities.SettingsConfig
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.repos.NavigationRepository
import com.eyal.exam.pelecard.repos.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val navigationRepository: NavigationRepository
) : ViewModel() {

    private val _settingsConfiguration = MutableStateFlow<SettingsConfig?>(null)
    val settingsConfiguration = _settingsConfiguration.asStateFlow()

    companion object {
        const val TAG = "SettingsViewModel"
    }

    init {
        viewModelScope.launch {
            settingsRepository.settingsConfiguration.collect { settingsConfig ->
                _settingsConfiguration.value = settingsConfig
                Log.d(TAG, "init: settingsConfiguration.collect: settingsConfig is $settingsConfig")
            }
        }
    }

    fun navigateBack() = with(viewModelScope) {
        launch {
            navigationRepository.navigateTo(NavEvent.NavigateToMain)
        }
    }

    fun updateSettingsConfigurations(settingsConfig: SettingsConfig) = with(viewModelScope) {
        launch {
            settingsRepository.updateSettingsConfigurations(settingsConfig)
            Log.d(TAG, "updateSettingsConfigurations: The New SettingsConfig is $settingsConfig")
        }
    }

}