package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.manager

import com.google.firebase.auth.AuthCredential
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.AuthRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.AuthManager
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.BackupManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthManagerImpl @Inject constructor(
    private val authRepo: AuthRepo,
    private val backupManager: BackupManager,
): AuthManager {
    override suspend fun signInWithEmail(email: String, password: String): Resource<UiText> {
        val result = authRepo.signInWithEmail(email, password)
        return handleAfterSignIn(result)
    }

    override suspend fun signInWithCredential(credential: AuthCredential): Resource<UiText> {
        val result = authRepo.signInWithCredential(credential)
        return handleAfterSignIn(result)
    }

    override suspend fun signUpWithEmail(email: String, password: String): Resource<UiText> {
        val result = authRepo.signUpWithEmail(email, password)
        return handleAfterSignIn(result)
    }

    override suspend fun resetPassword(email: String): Resource<UiText> {
        return authRepo.resetPassword(email)
    }

    override suspend fun signOut(makeBackupBeforeSignOut: Boolean): Resource<UiText> {
        if(makeBackupBeforeSignOut){
            val user = authRepo.currentUser()?:return Resource.Error(UiText.Resource(R.string.user_not_found))
            val backupResult = backupManager.uploadBackup(user)
            if(backupResult is Resource.Error){
                return Resource.Error(UiText.Resource(R.string.backup_not_executed_try_later))
            }
        }
        return when(val result = authRepo.logOut()){
            is Resource.Error -> {
                result
            }
            is Resource.Success -> {
                backupManager.deleteAllLocalUserData(true)
                result
            }
        }
    }

    override fun currentUser(): User? {
        return authRepo.currentUser()
    }

    override suspend fun hasBackupMetas(): Boolean {
        return backupManager.hasBackupMetas()
    }

    override fun userFlow(): Flow<User?> {
        return authRepo.userFlow()
    }

    private suspend fun handleAfterSignIn(authResult: Resource<User>): Resource<UiText> {
        return when(authResult){
            is Resource.Success -> {
                val user = authResult.data
                backupManager.refreshBackupMetas(user).let { metaResult->
                    when(metaResult){
                        is Resource.Error -> {
                            Resource.Error(error = UiText.Resource(R.string.backup_files_not_downloaded))
                        }
                        is Resource.Success -> { Resource.Success(UiText.Resource(R.string.successfully_log_in))}
                    }
                }
            }
            is Resource.Error -> { Resource.Error(authResult.error) }
        }
    }

}