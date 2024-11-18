package com.eyal.exam.pelecard.data.entities

import com.eyal.exam.pelecard.models.SettingId
import com.eyal.exam.pelecard.models.SettingsItem

data class SettingsConfig(

    ///todo refactor?????
    var settingsMap: Map<SettingId, SettingsItem<Any>>
)