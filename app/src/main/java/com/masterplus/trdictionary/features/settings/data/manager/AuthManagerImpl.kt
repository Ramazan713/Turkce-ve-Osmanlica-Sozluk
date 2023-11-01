package com.masterplus.trdictionary.features.settings.data.manager

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.util.Resource
import com.masterplus.trdictionary.core.util.UiText
import com.masterplus.trdictionary.features.settings.domain.model.User
import com.masterplus.trdictionary.features.settings.domain.repo.AuthRepo
import com.masterplus.trdictionary.features.settings.domain.manager.AuthManager
import com.masterplus.trdictionary.features.settings.domain.manager.BackupManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthManagerImpl @Inject constructor(
    private val authRepo: AuthRepo,
    private val backupManager: BackupManager,
): AuthManager {

    override suspend fun signInWithGoogle(activityResult: ActivityResult): Resource<User> {
        return when(val result = authRepo.signInWithGoogle(activityResult)){
            is Resource.Success -> {
                val user = result.data
                backupManager.refreshBackupMetas(user).let { metaResult->
                    when(metaResult){
                        is Resource.Error -> {
                            Resource.Error(error = UiText.Resource(R.string.backup_files_not_downloaded), data = user)
                        }
                        is Resource.Success -> { result }
                    }
                }
            }
            is Resource.Error -> {
                result
            }
        }
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


    override fun getGoogleSignInIntent(): Intent {
        return authRepo.getGoogleSignInIntent()
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

}