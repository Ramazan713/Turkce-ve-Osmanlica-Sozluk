package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper.toUser
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.AuthRepo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseAuthRepo @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepo {

    private val commonErrorUiText = UiText.Resource(R.string.something_went_wrong)

    override suspend fun signInWithEmail(email: String, password: String): Resource<User> {
        return handleException {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            result.user?.toUser()?.let { user->
                return@handleException Resource.Success(user)
            }
            return@handleException Resource.Error(commonErrorUiText)
        }
    }
    override suspend fun signUpWithEmail(email: String, password: String): Resource<User> {
        return handleException {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.toUser()?.let { user->
                return@handleException Resource.Success(user)
            }
            return@handleException Resource.Error(commonErrorUiText)
        }
    }

    override suspend fun signInWithCredential(credential: AuthCredential): Resource<User> {
        return handleException {
            firebaseCredentialSignIn(credential)
        }
    }


    override suspend fun resetPassword(email: String): Resource<UiText> {
        return handleException {
            firebaseAuth.sendPasswordResetEmail(email)
            return@handleException Resource.Success(UiText.Resource(R.string.email_send_for_reset_password))
        }
    }

    override fun userFlow(): Flow<User?> {
        return callbackFlow {
            val authStateListener = AuthStateListener{auth->
                launch { send(auth.currentUser?.toUser()) }
            }
            firebaseAuth.addAuthStateListener(authStateListener)
            awaitClose {
                firebaseAuth.removeAuthStateListener(authStateListener)
            }
        }
    }

    override fun isLogin(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun currentUser(): User? {
        return firebaseAuth.currentUser?.toUser()
    }

    override suspend fun logOut(): Resource<UiText> {
        return handleException {
            firebaseAuth.signOut()
            Resource.Success(UiText.Resource(R.string.successfully_log_out))
        }
    }

    private suspend fun firebaseCredentialSignIn(credential: AuthCredential): Resource<User> {
        val result = firebaseAuth.signInWithCredential(credential).await()
        return result.user?.let { user-> Resource.Success(user.toUser()) } ?: kotlin.run {
            Resource.Error(commonErrorUiText)
        }
    }

    private suspend fun <T> handleException(onTry: suspend () -> Resource<T>): Resource<T> {
        return try {
            onTry()
        }catch (e: Exception){
            if(e is CancellationException) throw e
            val message = e.localizedMessage?.let { error-> UiText.Text(error) } ?: commonErrorUiText
            Resource.Error(message)
        }
    }

}