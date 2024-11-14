package com.eyal.exam.pelecard.models

data class SettingsConfig(
    var settingsMap: Map<SettingId, SettingsItem<Any>>
)