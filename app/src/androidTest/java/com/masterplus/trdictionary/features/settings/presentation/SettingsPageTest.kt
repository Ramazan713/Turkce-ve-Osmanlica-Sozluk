package com.masterplus.trdictionary.features.settings.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.masterplus.trdictionary.MainActivity
import com.masterplus.trdictionary.core.domain.enums.ThemeEnum
import com.masterplus.trdictionary.features.settings.presentation.navigation.navigateToSettings
import com.masterplus.trdictionary.shared_test.HiltBaseClassForTest
import com.masterplus.trdictionary.shared_test.robots.AppRobot
import dagger.hilt.android.testing.HiltAndroidTest
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.domain.repo.ThemeRepo
import com.masterplus.trdictionary.core.domain.utils.Resource
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.AuthRepoFake
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.data.repo.StorageServiceFake
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.BackupMeta
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.model.User
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.AuthRepo
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.domain.repo.StorageService
import com.masterplus.trdictionary.core.utils.sample_data.backupMeta
import com.masterplus.trdictionary.core.utils.sample_data.user
import com.masterplus.trdictionary.features.settings.domain.enums.BackupLoadSectionEnum
import io.mockk.every
import io.mockk.mockkObject

import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@HiltAndroidTest
class SettingsPageTest: HiltBaseClassForTest() {

    @Inject
    lateinit var themeRepo: ThemeRepo

    @Inject
    lateinit var authRepo: AuthRepo

    @Inject
    lateinit var storageService: StorageService

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var appRobot: AppRobot
    private lateinit var settingsRobot: SettingsPageRobot
    private lateinit var user: User

    override fun setUp() {
        super.setUp()

        appRobot = AppRobot(composeRule)
        settingsRobot = SettingsPageRobot(composeRule)
        user = user(email = "exampl@gmail.com", name = "test name")
    }

    @Test
    fun theme_changeThemeSystemToDark(){
        init()
        settingsRobot
            .assertThemeEnumIsDisplayed(ThemeEnum.System)
            .changeTheme(ThemeEnum.Dark)
            .assertThemeEnumIsDisplayed(ThemeEnum.Dark)
    }

    @Test
    fun searchResultCount_shouldChangeResultCount(){
        init()
        settingsRobot
            .changeSearchResultCount(13)
            .assertTextIsDisplayed("13")
    }

    @Test
    fun searchResultCount_whenSearchResultIsInBoundary_shouldAccept(){
        init()
        settingsRobot
            .changeSearchResultCount(10)
            .assertTextIsDisplayed("10")
            .changeSearchResultCount(100)
            .assertTextIsDisplayed("100")
    }

    @Test
    fun searchResultCount_whenSearchResultIsNotNotBoundary_shouldShowError(){
        init()
        settingsRobot
            .changeSearchResultCount(9)
            .assertTextIsDisplayed(settingsRobot.getText(R.string.error_mismatch_range))
            .clickCancel()
            .changeSearchResultCount(101)
            .assertTextIsDisplayed(settingsRobot.getText(R.string.error_mismatch_range))
    }

    @Test
    fun whenDynamicFeatureSupported_shouldBeVisible(){
        mockkObject(themeRepo)
        every { themeRepo.hasSupportedDynamicTheme() } returns true
        init()

        settingsRobot
            .assertTextIsDisplayed(settingsRobot.getText(R.string.use_dynamic_colors))
    }

    @Test
    fun whenDynamicFeatureIsNotSupported_shouldBeInVisible(){
        mockkObject(themeRepo)
        every { themeRepo.hasSupportedDynamicTheme() } returns false
        init()

        settingsRobot
            .assertTextIsNotDisplayed(settingsRobot.getText(R.string.use_dynamic_colors))
    }

    @Test
    fun whenDynamicFeatureClick_shouldBeEnabled(){
        mockkObject(themeRepo)
        every { themeRepo.hasSupportedDynamicTheme() } returns true
        init()

        settingsRobot
            .assertDynamicColorClicked(isClicked = false)
            .toggleDynamicColorFeature()
            .assertDynamicColorClicked(isClicked = true)
    }

    @Test
    fun whenUseArchiveInListClick_shouldBeEnabled(){
        init()
        settingsRobot
            .assertUseArchiveListClicked(isClicked = false)
            .toggleUseArchiveList()
            .assertUseArchiveListClicked(isClicked = true)
    }

    @Test
    fun whenClickResetDefaultSettings_shouldResetSettings(){
        mockkObject(themeRepo)
        every { themeRepo.hasSupportedDynamicTheme() } returns true
        init()

        settingsRobot
            .assertThemeEnumIsDisplayed(ThemeEnum.System)
            .assertTextIsDisplayed("10")
            .assertDynamicColorClicked(isClicked = false)
            .assertUseArchiveListClicked(isClicked = false)

        //change state
        settingsRobot
            .apply {
                composeRule.waitForIdle()
            }
            .changeSearchResultCount(20)
            .toggleDynamicColorFeature()
            .toggleUseArchiveList()
            .changeTheme(ThemeEnum.Dark)

        //assert new changed state
        settingsRobot
            .assertThemeEnumIsDisplayed(ThemeEnum.Dark)
            .assertTextIsDisplayed("20")
            .assertDynamicColorClicked(isClicked = true)
            .assertUseArchiveListClicked(isClicked = true)

        //click reset
        settingsRobot
            .clickDefaultSettings()
            .clickApprove()

        //assert all state in init
        settingsRobot
            .assertThemeEnumIsDisplayed(ThemeEnum.System)
            .assertTextIsDisplayed("10")
            .assertDynamicColorClicked(isClicked = false)
            .assertUseArchiveListClicked(isClicked = false)
    }


    @Test
    fun whenNoUserLoggedIn_shouldBeInVisibleBackupSection(){
        init()
        settingsRobot
            .assertBackupSectionVisible(false)
    }

    @Test
    fun whenNoUserLoggedIn_shouldBeInVisibleSignOut(){
        init()
        settingsRobot
            .scrollToBottom()
            .assertSignOutButtonVisible(false)
    }

    @Test
    fun signIn_whenUserSignIn_shouldBeVisibleBackupSignOutAndUserInfo(){
        initForAuth()

        settingsRobot
            .signIn(email = user.email!!)
            .scrollToText(settingsRobot.getText(R.string.backup_n))
            .assertBackupSectionVisible(true)
            .assertTextIsDisplayed(user.email!!)
            .assertTextIsDisplayed(user.name!!)
            .scrollToBottom()
            .assertSignOutButtonVisible(true)
    }

    @Test
    fun whenUserSignInAndHasBackupMeta_shouldOpenBackupOperationsDia(){
        initForAuth(backupMetas = listOf(backupMeta()))

        settingsRobot
            .signIn(email = user.email!!)
            .assertBackupOperationsDiaOpen(isDisplayed = true)
    }


    @Test
    fun signIn_whenClickShowBackupFiles_shouldOpenBackupDia(){
        initForAuth(backupMetas = listOf(backupMeta()))

        settingsRobot
            .signIn(email = user.email!!)
            .clickBackupOperation(BackupLoadSectionEnum.ShowBackupFiles)
            .waitForOneTextIsVisible("Backup",subString = true)
            .assertTextIsDisplayed("Backup",subString = true)
    }


    @Test
    fun signIn_whenClickLoadLastBackup_shouldNavigateToHome(){
        initForAuth(backupMetas = listOf(backupMeta()))

        settingsRobot
            .signIn(email = user.email!!)
            .clickBackupOperation(BackupLoadSectionEnum.LoadLastBackup)
            .waitForIdle()
            .assertTextIsDisplayed(settingsRobot.getText(R.string.home))
    }


    @Test
    fun signIn_whenClickNotShowAgain_shouldNotVisibleAfterSignIn(){
        initForAuth(backupMetas = listOf(backupMeta()))

        settingsRobot
            .signIn(email = user.email!!)
            .clickBackupOperation(BackupLoadSectionEnum.NotShowAgain)
            .scrollAndSignOutInOneTime()
            .scrollToTop()
            .signIn(email = user.email!!)
            .assertBackupOperationsDiaOpen(false)
    }


    private fun initForAuth(
        backupMetas: List<BackupMeta> = emptyList()
    ){
        (authRepo as AuthRepoFake).apply {
            returnedSignInWithEmailResponse = Resource.Success(user)
        }
        (storageService as StorageServiceFake).apply {
            getFilesResponse = Resource.Success(backupMetas)
        }
        init()
    }

    private fun init(){
        appRobot.init {
            it.navigateToSettings()
        }
    }

}