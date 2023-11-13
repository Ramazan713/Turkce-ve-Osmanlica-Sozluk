package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User

fun FirebaseUser.toUser(): User {
    return User(
        uid = uid,
        name = displayName,
        email = email,
        photoUri = photoUrl
    )
}