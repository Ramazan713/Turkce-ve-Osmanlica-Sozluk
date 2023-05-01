package com.masterplus.trdictionary.features.settings.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.masterplus.trdictionary.features.settings.domain.model.User

fun FirebaseUser.toUser(): User {
    return User(
        uid = uid,
        name = displayName,
        email = email,
        photoUri = photoUrl
    )
}