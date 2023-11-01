package com.masterplus.trdictionary.features.settings.data.repo

import android.content.Intent
import androidx.activity.result.ActivityResult

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.GoogleAuthProvider
import com.masterplus.trdictionary.features.settings.domain.model.User
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.util.Resource
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.features.settings.data.mapper.toUser
import com.masterplus.trdictionary.features.settings.domain.repo.AuthRepo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseAuthRepo @Inject constructor(
    private val googleSignInClient: GoogleSignInClient,
    private val firebaseAuth: FirebaseAuth
): AuthRepo {


    override fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    override suspend fun signInWithGoogle(activityResult: ActivityResult): Resource<User> {
        val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
        try {
            val account = task.getResult(ApiException::class.java) ?:
                return Resource.Error(UiText.Resource(R.string.something_went_wrong))
            val idToken = account.idToken?: return Resource.Error(UiText.Resource(R.string.something_went_wrong))
            return firebaseAuthWithGoogle(idToken)
        }catch (e: Exception){
            return Resource.Error(UiText.Resource(R.string.something_went_wrong))
        }
    }

    override fun userFlow(): Flow<User?> {
        return callbackFlow {
            val authStateListener = AuthStateListener{auth->
                launch {
                    send(auth.currentUser?.toUser())
                }
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
        return try {
            googleSignInClient.signOut().await()
            firebaseAuth.signOut()
            Resource.Success(UiText.Resource(R.string.successfully_log_out))
        }catch (e: Exception){
            Resource.Error(e.localizedMessage?.let { UiText.Text(it) }?: UiText.Resource(R.string.error))
        }
    }

    private suspend fun firebaseAuthWithGoogle(idToken: String): Resource<User> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            val result = firebaseAuth.signInWithCredential(credential).await()
            result.user?.let { user-> Resource.Success(user.toUser()) } ?: kotlin.run {
                Resource.Error(UiText.Resource(R.string.something_went_wrong))
            }
        }catch (e: Exception){
            Resource.Error(e.localizedMessage?.let { UiText.Text(it) }?: UiText.Resource(R.string.something_went_wrong))
        }
    }

}