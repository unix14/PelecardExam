package com.eyal.exam.pelecard.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyal.exam.pelecard.abs.Constants
import com.eyal.exam.pelecard.models.NavEvent
import com.eyal.exam.pelecard.models.SettingsConfig
import com.eyal.exam.pelecard.repos.NavigationRepository
import com.eyal.exam.pelecard.repos.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    init {
        viewModelScope.launch {
            settingsRepository.settingsConfiguration.collect { settingsConfig ->
                _settingsConfiguration.value = settingsConfig
            }
        }
    }

    fun navigateBack() = with(viewModelScope) {
        launch {
            navigationRepository.navigateTo(NavEvent.NavigateToMain)
        }
    }

    //todo update settings configurations settingsRepository
    fun updateSettingsConfigurations(settingsConfig: SettingsConfig) = with(viewModelScope) {
        launch {
            settingsRepository.updateSettingsConfigurations(settingsConfig)
        }
    }

}