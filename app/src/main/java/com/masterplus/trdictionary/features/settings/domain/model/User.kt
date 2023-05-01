package com.masterplus.trdictionary.features.settings.domain.model

import android.net.Uri

data class User(
    val uid: String,
    val email: String?,
    val photoUri: Uri?,
    val name: String?
)
