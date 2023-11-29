package com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.manager

import android.credentials.Credential
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.google.firebase.auth.AuthCredential
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.AuthRepoFake
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.manager.AuthManager
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthManagerTest {

    private lateinit var backupManager: BackupManagerFake
    private lateinit var authRepo: AuthRepoFake

    private lateinit var authManager: AuthManagerImpl

    private val errorText = UiText.Text("Error")

    @BeforeEach
    fun setUp() {
        backupManager = BackupManagerFake()
        authRepo = AuthRepoFake()

        authManager = AuthManagerImpl(authRepo, backupManager)
    }


    @Test
    fun signInWithEmail_IfFailed_returnsError() = runTest {
        authRepo.returnedSignInWithEmailResponse = Resource.Error(errorText)
        val response = authManager.signInWithEmail("","")
        assertThat(response is Resource.Error).isTrue()
    }

    @Test
    fun signInWithEmail_IfSucceed_calledRefreshBackupMeta() = runTest {
        mockkObject(backupManager)
        val response = authManager.signInWithEmail("","")

        assertThat(response is Resource.Success).isTrue()
        coVerify {
            backupManager.refreshBackupMetas(any())
        }
    }

    @Test
    fun handleAfterSignIn_IfLoginSucceedAndRefreshBackupFailed_returnsError() = runTest {
        backupManager.returnedRefreshBackupMetasResponse = Resource.Error(errorText)
        val response = authManager.signInWithEmail("","")
        assertThat((response as Resource.Error).error).isEqualTo(UiText.Resource(R.string.backup_files_not_downloaded))
    }

    @Test
    fun handleAfterSignIn_IfLoginAndRefreshBackupSucceed_returnsSucceed() = runTest {
        val response = authManager.signInWithEmail("","")
        assertThat((response as Resource.Success).data).isEqualTo(UiText.Resource(R.string.successfully_log_in))
    }



    @Test
    fun signUpWithEmail_IfFailed_returnsError() = runTest {
        authRepo.returnedSignUpWithEmailResponse = Resource.Error(errorText)
        val response = authManager.signUpWithEmail("","")
        assertThat(response is Resource.Error).isTrue()
    }

    @Test
    fun signUpWithEmail_IfSucceed_calledRefreshBackupMeta() = runTest {
        mockkObject(backupManager)
        val response = authManager.signUpWithEmail("","")

        assertThat(response is Resource.Success).isTrue()
        coVerify {
            backupManager.refreshBackupMetas(any())
        }
    }


    @Test
    fun signInWithCredential_IfFailed_returnsError() = runTest {
        authRepo.returnedSignInWithCredentialResponse = Resource.Error(errorText)
        val response = authManager.signInWithCredential(mockk())
        assertThat(response is Resource.Error).isTrue()
    }

    @Test
    fun signInWithCredential_IfSucceed_calledRefreshBackupMeta() = runTest {
        mockkObject(backupManager)
        val response = authManager.signInWithCredential(mockk())

        assertThat(response is Resource.Success).isTrue()
        coVerify {
            backupManager.refreshBackupMetas(any())
        }
    }


    @Test
    fun signOut_WhenParamIsFalseAndFailed_returnsError() = runTest {
        authRepo.returnedLogOutResponse = Resource.Error(errorText)
        val response = authManager.signOut(false)
        assertThat(response is Resource.Error).isTrue()
    }

    @Test
    fun signOut_WhenParamIsFalseAndSucceed_calledDeleteAllLocalUserData() = runTest {
        mockkObject(backupManager)
        val response = authManager.signOut(false)
        assertThat(response is Resource.Success).isTrue()
        coVerify {
            backupManager.deleteAllLocalUserData(true)
        }
    }

    @Test
    fun signOut_WhenParamIsTrueAndNoUser_returnsError() = runTest {
        authRepo.currentUser = null
        val response = authManager.signOut(true)
        assertThat(response is Resource.Error).isTrue()
    }

    @Test
    fun signOut_WhenParamIsTrueAndUploadBackupFailed_returnsError() = runTest {
        backupManager.returnedUploadBackupResponse = Resource.Error(errorText)
        val response = authManager.signOut(true)
        assertThat(response is Resource.Error).isTrue()
    }

    @Test
    fun signOut_WhenParamIsTrueAndSucceedAll_returnsSucceed() = runTest {
        mockkObject(backupManager)
        val response = authManager.signOut(true)
        assertThat(response is Resource.Success).isTrue()
        coVerify {
            backupManager.deleteAllLocalUserData(true)
        }
    }

}