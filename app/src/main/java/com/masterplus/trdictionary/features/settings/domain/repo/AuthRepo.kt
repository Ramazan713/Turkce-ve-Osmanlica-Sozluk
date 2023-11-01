package com.masterplus.trdictionary.features.settings.domain.repo

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.masterplus.trdictionary.core.util.Resource
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.features.settings.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepo {

    fun getGoogleSignInIntent(): Intent

    suspend fun signInWithGoogle(activityResult: ActivityResult): Resource<User>

    fun userFlow(): Flow<User?>

    fun isLogin(): Boolean

    fun currentUser(): User?

    suspend fun logOut(): Resource<UiText>
}