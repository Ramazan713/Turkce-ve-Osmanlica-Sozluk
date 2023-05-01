package com.masterplus.trdictionary.core.domain.preferences.model

data class PrefKey<out T>(
    val key: String,
    val default: T,
)