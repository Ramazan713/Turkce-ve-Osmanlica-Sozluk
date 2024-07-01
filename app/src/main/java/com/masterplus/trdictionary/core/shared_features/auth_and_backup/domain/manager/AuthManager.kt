package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager

import com.google.firebase.auth.AuthCredential
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthManager {

    suspend fun signInWithEmail(email: String, password: String): Resource<UiText>

    suspend fun signUpWithEmail(email: String, password: String): Resource<UiText>

    suspend fun signInWithCredential(credential: AuthCredential): Resource<UiText>

    suspend fun resetPassword(email: String): Resource<UiText>

    fun userFlow(): Flow<User?>

    suspend fun signOut(makeBackupBeforeSignOut: Boolean): Resource<UiText>

    suspend fun deleteUser(credential: AuthCredential): Resource<UiText>


    fun currentUser(): User?

    suspend fun hasBackupMetas(): Boolean
}