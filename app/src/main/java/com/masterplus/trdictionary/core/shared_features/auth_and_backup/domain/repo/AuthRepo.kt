package com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo

import com.google.firebase.auth.AuthCredential
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    suspend fun signInWithEmail(email: String, password: String): Resource<User>

    suspend fun signUpWithEmail(email: String, password: String): Resource<User>

    suspend fun signInWithCredential(credential: AuthCredential): Resource<User>

    suspend fun resetPassword(email: String): Resource<UiText>



    fun userFlow(): Flow<User?>

    fun isLogin(): Boolean

    fun currentUser(): User?

    suspend fun logOut(): Resource<UiText>

    suspend fun deleteUser(credential: AuthCredential): Resource<UiText>
}