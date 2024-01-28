package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.backup_select

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.masterplus.trdictionary.MainActivity
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.mapper.toBackupMetaEntity
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.AuthRepoFake
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.StorageServiceFake
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.BackupMeta
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.AuthRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.StorageService
import com.masterplus.trdictionary.core.utils.sample_data.backupMeta
import com.masterplus.trdictionary.core.utils.sample_data.user
import com.masterplus.trdictionary.features.settings.presentation.navigation.navigateToSettings
import com.masterplus.trdictionary.shared_test.HiltBaseClassForTest
import com.masterplus.trdictionary.shared_test.robots.AppRobot
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking

import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@HiltAndroidTest
class ShowCloudSelectBackupDiaTest: HiltBaseClassForTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var storageService: StorageService

    @Inject
    lateinit var authRepo: AuthRepo

    private lateinit var diaRobot: ShowCloudSelectBackupDiaRobot
    private lateinit var appRobot: AppRobot


    override fun setUp() {
        super.setUp()
        diaRobot = ShowCloudSelectBackupDiaRobot(composeRule)
        appRobot = AppRobot(composeRule)
    }

    @Test
    fun whenNoBackupMeta_shouldBeVisibleEmptyText(){
        init()
        diaRobot
            .assertEmptyContentIsDisplayed()
    }

    @Test
    fun whenNoItemSelected_shouldBeButtonsDisabled(){
        insertBackupMetas(backupMeta())
        init()

        diaRobot
            .assertOverrideButtonIsEnabled(isEnabled = false)
            .assertAddOnButtonIsEnabled(isEnabled = false)
    }

    @Test
    fun whenItemSelected_shouldBeButtonsEnabled(){
        insertBackupMetas(backupMeta())
        init()

        diaRobot
            .clickNBackupItem(0)
            .assertNBackupItemIsSelected(0,isSelected = true)
            .assertOverrideButtonIsEnabled(isEnabled = true)
            .assertAddOnButtonIsEnabled(isEnabled = true)
    }

    @Test
    fun refresh_whenRefreshClick_shouldBeDisabled(){
        init()

        diaRobot
            .clickRefreshButton()
            .assertRefreshButtonIsEnabled(isEnabled = false)
    }

    @Test
    fun refresh_whenRefreshClick_shouldDownloadBackupMetas(){
        setServerResponseItems(listOf(backupMeta(id = 10), backupMeta(id = 11), backupMeta(id = 12)))
        insertBackupMetas(backupMeta(id = 1))
        init()

        diaRobot
            .assertNBackupItemsDisplay(1)
            .clickRefreshButton()
            .waitForIdle()
            .assertNBackupItemsDisplay(3)
    }




    private fun insertBackupMetas(vararg backupMetas: BackupMeta){
        runBlocking {
            db.backupMetaDao()
                .insertBackupMetas(backupMetas.map { it.toBackupMetaEntity() })
        }
    }

    private fun setServerResponseItems(backupMetas: List<BackupMeta>){
        (storageService as StorageServiceFake).apply {
            getFilesResponse = Resource.Success(backupMetas)
        }
    }

    private fun init(){
        (authRepo as AuthRepoFake).apply {
            val user = user()
            returnedUserFlow = MutableSharedFlow<User?>(replay = 1).apply { tryEmit(user) }
            currentUser = user
        }
        appRobot.init {
            it.navigateToSettings()
        }
        diaRobot.openBackupDia()
    }
}