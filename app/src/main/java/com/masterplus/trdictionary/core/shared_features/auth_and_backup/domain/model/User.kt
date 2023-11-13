package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model

import android.net.Uri

data class User(
    val uid: String,
    val email: String?,
    val photoUri: Uri?,
    val name: String?
)
