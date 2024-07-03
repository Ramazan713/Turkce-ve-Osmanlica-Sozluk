package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo

import com.google.firebase.auth.AuthCredential
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.AuthRepo
import com.masterplus.trdictionary.core.utils.sample_data.user
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepoFake: AuthRepo {

    var returnedSignInWithEmailResponse: Resource<User> = Resource.Success(user())
    var returnedSignUpWithEmailResponse: Resource<User> = Resource.Success(user())
    var returnedSignInWithCredentialResponse: Resource<User> = Resource.Success(user())
    var returnedResetPasswordResponse: Resource<UiText> = Resource.Success(UiText.Text(""))
    var returnedLogOutResponse: Resource<UiText> = Resource.Success(UiText.Text(""))
    var returnedDeleteUserResponse: Resource<UiText> = Resource.Success(UiText.Text(""))

    var currentUser: User? = user()
    var returnedUserFlow: Flow<User?> = flow {  }
    var returnedIsLogin:  Boolean = true


    override suspend fun signInWithEmail(email: String, password: String): Resource<User> {
        return returnedSignInWithEmailResponse
    }

    override suspend fun signUpWithEmail(email: String, password: String): Resource<User> {
        return returnedSignUpWithEmailResponse
    }

    override suspend fun signInWithCredential(credential: AuthCredential): Resource<User> {
        return returnedSignInWithCredentialResponse
    }

    override suspend fun resetPassword(email: String): Resource<UiText> {
        return returnedResetPasswordResponse
    }

    override fun userFlow(): Flow<User?> {
        return returnedUserFlow
    }

    override fun isLogin(): Boolean {
        return returnedIsLogin
    }

    override fun currentUser(): User? {
        return currentUser
    }

    override suspend fun logOut(): Resource<UiText> {
        return returnedLogOutResponse
    }

    override suspend fun deleteUser(credential: AuthCredential): Resource<UiText> {
        return returnedDeleteUserResponse
    }

}