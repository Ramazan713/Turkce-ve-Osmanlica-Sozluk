package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.masterplus.trdictionary.core.data.preferences.SettingsPreferencesFake
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.domain.utils.UiText
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.manager.AuthManagerFake
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.manager.BackupManagerFake
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.use_cases.ValidateEmailUseCase
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.use_cases.ValidatePasswordUseCase
import com.masterplus.trdictionary.core.utils.sample_data.user
import com.masterplus.trdictionary.utils.MainDispatcherRule
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherRule::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest{

    private lateinit var settingsPreferences: SettingsPreferencesFake
    private lateinit var authManager: AuthManagerFake
    private lateinit var backupManager: BackupManagerFake
    private lateinit var validateEmailUseCase: ValidateEmailUseCase
    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase

    private lateinit var authViewModel: AuthViewModel

    @BeforeEach
    fun setUp(){
        settingsPreferences = SettingsPreferencesFake()
        authManager = AuthManagerFake()
        backupManager = BackupManagerFake()
        validateEmailUseCase = ValidateEmailUseCase()
        validatePasswordUseCase = ValidatePasswordUseCase()

        authViewModel = AuthViewModel(settingsPreferences, authManager, backupManager, validateEmailUseCase, validatePasswordUseCase)
    }


    @Test
    fun deleteAllUserData_shouldBeBackupManagerExecuted() = runTest {
        mockkObject(backupManager)
        authViewModel.state.test {
            val firstState = awaitItem()

            authViewModel.onEvent(AuthEvent.DeleteAllUserData)

            val lastState = awaitItem()

            assertThat(firstState.message).isNull()
            assertThat(lastState.message).isNotNull()
            coVerify {
                backupManager.deleteAllLocalUserData(false)
            }
        }
    }

    @Test
    fun loadLastBackup_whenUserNull_shouldNotBeExecuted() = runTest {
        mockkObject(backupManager)
        authManager.returnedCurrentUser = null
        authViewModel.state.test {
            awaitItem()
            authViewModel.onEvent(AuthEvent.LoadLastBackup)
            coVerify(exactly = 0){
                backupManager.downloadLastBackup(user())
            }
        }
    }

    @Test
    fun loadLastBackup_whenErrorExists_shouldMessageSet() = runTest {
        val uiText = UiText.Text("some error")
        backupManager.returnedDownloadLastBackupResponse = Resource.Error(uiText)

        authViewModel.onEvent(AuthEvent.LoadLastBackup)

        advanceUntilIdle()
        val state = authViewModel.state.value
        assertThat(state.message).isEqualTo(uiText)
    }

    @Test
    fun loadLastBackup_whenSucceed_shouldUiActionRefreshApp() = runTest {
        authViewModel.onEvent(AuthEvent.LoadLastBackup)
        advanceUntilIdle()
        val state = authViewModel.state.value
        assertThat(state.uiAction).isEqualTo(AuthUiAction.RefreshApp)
    }

    @Test
    fun loadLastBackup_whenSucceed_shouldLoadingStateExecuteOrder() = runTest {
        authViewModel.state.test {
            awaitItem()//init

            authViewModel.onEvent(AuthEvent.LoadLastBackup)

            val firstLoadingState = awaitItem()
            awaitItem() // update result state
            val lastLoadingState = awaitItem()

            assertThat(firstLoadingState.isLoading).isTrue()
            assertThat(lastLoadingState.isLoading).isFalse()
        }
    }


    @Test
    fun handleSignIn_whenErrorExists_shouldBeErrorHandled() = runTest {
        val errorText = UiText.Text("Error")
        authManager.returnedSignInWithCredentialResponse = Resource.Error(errorText)

        authViewModel.onEvent(AuthEvent.SignInWithCredential(mockk()))

        advanceUntilIdle()
        val lastState = authViewModel.state.value
        assertThat(lastState.message).isEqualTo(errorText)
        assertThat(lastState.isLoading).isFalse()
    }

    @Test
    fun handleSignIn_whenHasBackupMetasAndShowBackupSectionForLoginIsTrue_shouldBeUiActionIsShowBackupSectionForLogin() = runTest {
        authManager.returnedHasBackupMetas = true
        settingsPreferences.updateData { it.copy(showBackupSectionForLogin = true) }

        authViewModel.onEvent(AuthEvent.SignInWithCredential(mockk()))

        advanceUntilIdle()
        val lastState = authViewModel.state.value
        assertThat(lastState.uiAction).isEqualTo(AuthUiAction.ShowBackupSectionForLogin)
    }

    @Test
    fun handleSignIn_whenHasBackupMetasAndShowBackupSectionForLoginIsFalse_shouldBeUiActionIsNull() = runTest {
        authManager.returnedHasBackupMetas = true
        settingsPreferences.updateData { it.copy(showBackupSectionForLogin = false) }

        authViewModel.onEvent(AuthEvent.SignInWithCredential(mockk()))

        advanceUntilIdle()
        val lastState = authViewModel.state.value
        assertThat(lastState.uiAction).isEqualTo(null)
    }

    @Test
    fun handleSignIn_whenHasNotBackupMetasAndShowBackupSectionForLoginIsTrue_shouldBeUiActionIsNull() = runTest {
        authManager.returnedHasBackupMetas = false
        settingsPreferences.updateData { it.copy(showBackupSectionForLogin = true) }

        authViewModel.onEvent(AuthEvent.SignInWithCredential(mockk()))

        advanceUntilIdle()
        val lastState = authViewModel.state.value
        assertThat(lastState.uiAction).isEqualTo(null)
    }

    @Test
    fun signInWithCredential_whenErrorExists_shouldBeErrorHandled() = runTest {
        val error = UiText.Text("Error")
        authManager.returnedSignInWithCredentialResponse = Resource.Error(error)

        authViewModel.onEvent(AuthEvent.SignInWithCredential(mockk()))
        advanceUntilIdle()
        val state = authViewModel.state.value
        assertThat(state.message).isEqualTo(error)
    }

    @Test
    fun signInWithCredential_whenSucceed_shouldBeHasBackupMetasCalled() = runTest {
        val succeed = UiText.Text("succeed")
        authManager.returnedSignInWithCredentialResponse = Resource.Success(succeed)
        mockkObject(authManager)

        authViewModel.onEvent(AuthEvent.SignInWithCredential(mockk()))
        advanceUntilIdle()
        val state = authViewModel.state.value
        assertThat(state.message).isEqualTo(succeed)
        coVerify {
            authManager.hasBackupMetas()
        }
    }


    @Test
    fun signInWithEmail_whenEmailInvalid_shouldBeErrorHandled() = runTest {
        val email = "asdasdad"
        val password = "123456789"
        mockkObject(authManager)

        authViewModel.onEvent(AuthEvent.SignInWithEmail(email,password))

        advanceUntilIdle()
        val state = authViewModel.state.value
        assertThat(state.message).isNotNull()
        coVerify(exactly = 0){
            authManager.signInWithEmail(email,password)
        }
    }

    @Test
    fun signInWithEmail_whenPasswordInvalid_shouldBeErrorHandled() = runTest {
        val email = "example@gmail.com"
        val password = "1234"
        mockkObject(authManager)

        authViewModel.onEvent(AuthEvent.SignInWithEmail(email,password))

        advanceUntilIdle()
        val state = authViewModel.state.value
        assertThat(state.message).isNotNull()
        coVerify(exactly = 0){
            authManager.signInWithEmail(email,password)
        }
    }

    @Test
    fun signInWithEmail_whenHasError_shouldBeErrorHandled() = runTest {
        val email = "example@gmail.com"
        val password = "123456789"
        val error = UiText.Text("Error")
        mockkObject(authManager)
        authManager.returnedSignInWithEmailResponse = Resource.Error(error)
        authViewModel.onEvent(AuthEvent.SignInWithEmail(email,password))

        advanceUntilIdle()
        val state = authViewModel.state.value
        assertThat(state.message).isEqualTo(error)
        coVerify(exactly = 1){
            authManager.signInWithEmail(email,password)
        }
    }

    @Test
    fun signInWithEmail_whenNoError_shouldExecuted() = runTest {
        val email = "example@gmail.com"
        val password = "123456789"
        val succeedMessage = UiText.Text("succeedMessage")
        mockkObject(authManager)
        authManager.returnedSignInWithEmailResponse = Resource.Success(succeedMessage)
        authViewModel.onEvent(AuthEvent.SignInWithEmail(email,password))

        advanceUntilIdle()
        val state = authViewModel.state.value
        assertThat(state.message).isEqualTo(succeedMessage)
        coVerify(exactly = 1){
            authManager.signInWithEmail(email,password)
        }
    }




    @Test
    fun signUpWithEmail_whenEmailInvalid_shouldBeErrorHandled() = runTest {
        val email = "asdasdad"
        val password = "123456789"
        mockkObject(authManager)

        authViewModel.onEvent(AuthEvent.SignUpWithEmail(email,password))

        advanceUntilIdle()
        val state = authViewModel.state.value
        assertThat(state.message).isNotNull()
        coVerify(exactly = 0){
            authManager.signUpWithEmail(email,password)
        }
    }

    @Test
    fun signUpWithEmail_whenPasswordInvalid_shouldBeErrorHandled() = runTest {
        val email = "example@gmail.com"
        val password = "1234"
        mockkObject(authManager)

        authViewModel.onEvent(AuthEvent.SignUpWithEmail(email,password))

        advanceUntilIdle()
        val state = authViewModel.state.value
        assertThat(state.message).isNotNull()
        coVerify(exactly = 0){
            authManager.signUpWithEmail(email,password)
        }
    }

    @Test
    fun signUpWithEmail_whenHasError_shouldBeErrorHandled() = runTest {
        val email = "example@gmail.com"
        val password = "123456789"
        val error = UiText.Text("Error")
        mockkObject(authManager)
        authManager.returnedSignUpWithEmailResponse = Resource.Error(error)
        authViewModel.onEvent(AuthEvent.SignUpWithEmail(email,password))

        advanceUntilIdle()
        val state = authViewModel.state.value
        assertThat(state.message).isEqualTo(error)
        coVerify(exactly = 1){
            authManager.signUpWithEmail(email,password)
        }
    }

    @Test
    fun signUpWithEmail_whenNoError_shouldExecuted() = runTest {
        val email = "example@gmail.com"
        val password = "123456789"
        val succeedMessage = UiText.Text("succeedMessage")
        mockkObject(authManager)
        authManager.returnedSignUpWithEmailResponse = Resource.Success(succeedMessage)
        authViewModel.onEvent(AuthEvent.SignUpWithEmail(email,password))

        advanceUntilIdle()
        val state = authViewModel.state.value
        assertThat(state.message).isEqualTo(succeedMessage)
        coVerify(exactly = 1){
            authManager.signUpWithEmail(email,password)
        }
    }


    @Test
    fun signOut_whenErrorExists_shouldBeErrorHandled() = runTest {
        val error = UiText.Text("Error")
        authManager.returnedSignOutResponse = Resource.Error(error)

        authViewModel.onEvent(AuthEvent.SignOut(false))

        advanceUntilIdle()
        val state = authViewModel.state.value
        assertThat(state.message).isEqualTo(error)
    }

    @Test
    fun signOut_whenNoError_shouldBeExecutedInOrder() = runTest {
        authViewModel.state.test {
            mockkObject(authManager)
            awaitItem()

            authViewModel.onEvent(AuthEvent.SignOut(false))

            val loadingState = awaitItem()
            val responseState = awaitItem()
            val lastLoadingState = awaitItem()

            assertThat(loadingState.isLoading).isTrue()
            assertThat(responseState.message).isNotNull()
            assertThat(lastLoadingState.isLoading).isFalse()
            coVerify(exactly = 1){
                authManager.signOut(false)
            }
        }
    }

    @Test
    fun listenUserFlow_whenUserChange_shouldStateChange() = runTest {
        val userFlow = MutableSharedFlow<User?>(replay = 1)
        val firstUser = user(email = "test@gmail.com")
        val secondUser = user(email = "test2x@gmail.com")

        authManager.returnedUserFlow = userFlow.asSharedFlow()
        authViewModel = AuthViewModel(settingsPreferences, authManager, backupManager, validateEmailUseCase, validatePasswordUseCase)

        userFlow.emit(firstUser)
        authViewModel.state.test {
            val initState = awaitItem()

            val secondState = awaitItem()

            userFlow.emit(secondUser)
            val thirdState = awaitItem()

            assertThat(initState.user).isNull()
            assertThat(secondState.user).isEqualTo(firstUser)
            assertThat(thirdState.user).isEqualTo(secondUser)
        }
    }

}