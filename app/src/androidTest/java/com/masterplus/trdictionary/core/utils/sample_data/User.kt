package com.masterplus.trdictionary.core.utils.sample_data

import android.net.Uri
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import java.util.UUID

fun user(
    uid: String = UUID.randomUUID().toString(),
    email: String? = "example@gmail.com",
    photoUri: Uri? = null,
    name: String? = "name"
): User {
    return User(
        uid, email, photoUri, name
    )
}