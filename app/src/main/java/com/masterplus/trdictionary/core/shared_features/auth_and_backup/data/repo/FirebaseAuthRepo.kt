package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.domain.utils.safeCall
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper.toUser
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.AuthRepo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseAuthRepo @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): AuthRepo {
    override suspend fun signInWithEmail(email: String, password: String): Resource<User> {
        return safeCall {
            firebaseAuth.signInWithEmailAndPassword(email, password).await().user!!.toUser()
        }
    }
    override suspend fun signUpWithEmail(email: String, password: String): Resource<User> {
        return safeCall {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await().user!!.toUser()
        }
    }

    override suspend fun signInWithCredential(credential: AuthCredential): Resource<User> {
        return firebaseCredentialSignIn(credential)
    }


    override suspend fun resetPassword(email: String): Resource<UiText> {
        return safeCall {
            firebaseAuth.sendPasswordResetEmail(email)
            return@safeCall UiText.Resource(R.string.email_send_for_reset_password)
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
        return safeCall {
            firebaseAuth.signOut()
            UiText.Resource(R.string.successfully_log_out)
        }
    }

    override suspend fun deleteUser(credential: AuthCredential): Resource<UiText> {
        return safeCall {
            firebaseAuth.currentUser?.let { user->
                val result = user.reauthenticateAndRetrieveData(credential).await()
                if(result != null){
                    result.user?.delete()?.await()
                    return@safeCall UiText.Resource(R.string.delete_account_success)
                }
            }
            throw Exception()
        }
    }

    private suspend fun firebaseCredentialSignIn(credential: AuthCredential): Resource<User> {
        return safeCall {
            firebaseAuth.signInWithCredential(credential).await().user!!.toUser()
        }
    }
}