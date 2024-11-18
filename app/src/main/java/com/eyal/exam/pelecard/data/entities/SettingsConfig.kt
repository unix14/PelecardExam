package com.eyal.exam.pelecard.data.entities

import com.eyal.exam.pelecard.models.SettingId
import com.eyal.exam.pelecard.models.SettingsItem

data class SettingsConfig(
    var settingsMap: Map<SettingId, SettingsItem<Any>>
)