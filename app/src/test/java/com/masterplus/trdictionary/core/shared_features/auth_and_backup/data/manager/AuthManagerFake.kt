package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.manager

import com.google.firebase.auth.AuthCredential
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.AuthManager
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.core.utils.sample_data.user
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.junit.jupiter.api.Assertions.*

class AuthManagerFake: AuthManager{

    var uiText = UiText.Text("sample")

    var returnedSignInWithEmailResponse: Resource<UiText> = Resource.Success(uiText)
    var returnedSignUpWithEmailResponse: Resource<UiText> = Resource.Success(uiText)
    var returnedSignInWithCredentialResponse: Resource<UiText> = Resource.Success(uiText)
    var returnedResetPasswordResponse: Resource<UiText> = Resource.Success(uiText)
    var returnedSignOutResponse: Resource<UiText> = Resource.Success(uiText)
    var returnedDeleteUserResponse: Resource<UiText> = Resource.Success(uiText)

    var returnedHasBackupMetas: Boolean = true
    var returnedCurrentUser: User? = user()
    var returnedUserFlow: Flow<User?> = flow {  }

    override suspend fun signInWithEmail(email: String, password: String): Resource<UiText> {
        return returnedSignInWithEmailResponse
    }

    override suspend fun signUpWithEmail(email: String, password: String): Resource<UiText> {
        return returnedSignUpWithEmailResponse
    }

    override suspend fun signInWithCredential(credential: AuthCredential): Resource<UiText> {
        return returnedSignInWithCredentialResponse
    }

    override suspend fun resetPassword(email: String): Resource<UiText> {
        return returnedResetPasswordResponse
    }

    override fun userFlow(): Flow<User?> {
        return returnedUserFlow
    }

    override suspend fun signOut(makeBackupBeforeSignOut: Boolean): Resource<UiText> {
        return returnedSignOutResponse
    }

    override suspend fun deleteUser(credential: AuthCredential): Resource<UiText> {
        return returnedDeleteUserResponse
    }

    override fun currentUser(): User? {
        return returnedCurrentUser
    }

    override suspend fun hasBackupMetas(): Boolean {
        return returnedHasBackupMetas
    }

}