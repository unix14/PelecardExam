package com.eyal.exam.pelecard.models

data class SettingsItem<T>(
    val title: String,
    var value: T
)