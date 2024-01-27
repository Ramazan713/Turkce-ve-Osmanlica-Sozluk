package com.masterplus.trdictionary.features.settings.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.masterplus.trdictionary.core.domain.enums.ThemeEnum
import com.masterplus.trdictionary.shared_test.rules.AppComposeRule
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.features.settings.domain.enums.BackupLoadSectionEnum

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
class SettingsPageRobot constructor(
    private val composeRule: AppComposeRule
) {
    private val context = composeRule.activity.applicationContext

    fun changeTheme(themeEnum: ThemeEnum): SettingsPageRobot{
        composeRule.onNode(
            hasText(getText(R.string.theme_mode)).and(
                hasClickAction()
            )
        ).performClick()
        composeRule.onNode(
            hasText(themeEnum.title.asString(context)).and(
                hasClickAction()
            )
        ).performClick()
        clickApprove()
        return this
    }

    fun assertThemeEnumIsDisplayed(themeEnum: ThemeEnum): SettingsPageRobot{
        assertTextIsDisplayed(themeEnum.title.asString(context))
        return this
    }

    fun changeSearchResultCount(number: Int): SettingsPageRobot{
        composeRule.onNodeWithText(getText(R.string.search_result_counts))
            .performClick()
        composeRule.onNodeWithContentDescription(getText(R.string.text_field)).apply {
            performTextClearance()
            performTextInput(number.toString())
        }
        clickApprove()
        return this
    }



    fun toggleUseArchiveList(): SettingsPageRobot{
        composeRule.onNode(hasText(getText(R.string.use_archive_in_select)).and(hasClickAction()))
            .performClick()
        return this
    }
    fun assertUseArchiveListClicked(isClicked: Boolean): SettingsPageRobot{
        composeRule.onNode(hasText(getText(R.string.use_archive_in_select)).and(hasClickAction())).apply {
            if(isClicked)
                assertIsOn()
            else
                assertIsOff()
        }
        return this
    }

    fun assertDynamicColorClicked(isClicked: Boolean): SettingsPageRobot {
        composeRule.onNode(hasText(getText(R.string.use_dynamic_colors))).apply {
            if(isClicked)
                assertIsOn()
            else
                assertIsOff()
        }
        return this
    }

    fun toggleDynamicColorFeature(): SettingsPageRobot{
        composeRule.onNode(
            hasText(getText(R.string.use_dynamic_colors)).and(hasClickAction())
        ).performClick()
        return this
    }

    fun clickDefaultSettings(): SettingsPageRobot {
        composeRule.onNodeWithText(getText(R.string.reset_default_setting))
            .performClick()
        return this
    }

    fun assertBackupSectionVisible(exists: Boolean): SettingsPageRobot {
        composeRule.onNodeWithText(getText(R.string.backup_n)).apply {
            if(exists) assertIsDisplayed()
            else assertIsNotDisplayed()
        }
        return this
    }

    fun scrollToText(text: String): SettingsPageRobot{
        composeRule.onNode(hasScrollAction())
            .performScrollToNode(hasText(text))
        return this
    }

    fun signIn(
        email: String = "example@gmail.com",
        password: String = "12345657"
    ): SettingsPageRobot{
        clickSignIn()
        composeRule.onNodeWithContentDescription(getText(R.string.email))
            .performTextInput(email)
        composeRule.onNodeWithContentDescription(getText(R.string.password))
            .performTextInput(password)

        composeRule.onNode(
            hasText(getText(R.string.sign_in_c)).and(
                hasClickAction()
            )
        ).performClick()

        return this
    }

    fun clickSignIn(): SettingsPageRobot{
        composeRule.onNodeWithText(getText(R.string.sign_in))
            .performClick()
        return this
    }

    fun scrollToTop(): SettingsPageRobot{
        composeRule.onNode(hasScrollAction()).apply {
            performScrollToIndex(0)
        }
        return this
    }

    fun scrollToBottom(): SettingsPageRobot{
        composeRule.onNode(hasScrollAction()).apply {
            performScrollToNode(hasText(getText(R.string.application)))
        }
        return this
    }


    fun clickSignOut(): SettingsPageRobot{
        composeRule.onNodeWithText(getText(R.string.sign_out))
            .performClick()
        return this
    }

    fun clickNotBackupButton(): SettingsPageRobot {
        composeRule.onNode(
            hasText(getText(R.string.not_backup)).and(hasClickAction())
        ).performClick()
        return this
    }

    fun scrollAndSignOutInOneTime(): SettingsPageRobot{
        scrollToBottom()
            .clickSignOut()
            .clickApprove()
            .waitForIdle()
            .clickNotBackupButton()
            .waitForIdle()
        return this
    }

    fun waitForIdle(): SettingsPageRobot{
        composeRule.waitForIdle()
        return this
    }

    @OptIn(ExperimentalTestApi::class)
    fun waitForOneTextIsVisible(text: String, subString: Boolean = false): SettingsPageRobot{
        composeRule.waitUntilExactlyOneExists(hasText(text, substring = subString))
        return this
    }

    fun assertSignOutButtonVisible(exists: Boolean): SettingsPageRobot {
        composeRule.onNodeWithText(getText(R.string.sign_out)).apply {
            if(exists) assertIsDisplayed()
            else assertIsNotDisplayed()
        }
        return this
    }

    fun assertBackupOperationsDiaOpen(isDisplayed: Boolean): SettingsPageRobot {
        composeRule.onNodeWithText(getText(R.string.operations_download_cloud_backup)).apply {
            if(isDisplayed) assertIsDisplayed()
            else assertIsNotDisplayed()
        }
        return this
    }

    fun clickBackupOperation(action: BackupLoadSectionEnum): SettingsPageRobot{
        composeRule.onNode(
            hasText(action.title.asString(context)).and(
                hasClickAction()
            )
        ).performClick()
        return this
    }



    fun assertTextIsDisplayed(text: String, subString: Boolean = false): SettingsPageRobot{
        composeRule.onNodeWithText(text, substring = subString)
            .assertIsDisplayed()
        return this
    }

    fun assertTextIsNotDisplayed(text: String): SettingsPageRobot{
        composeRule.onNodeWithText(text)
            .assertIsNotDisplayed()
        return this
    }


    fun clickApprove(): SettingsPageRobot{
        composeRule.onNode(
            hasText(getText(R.string.approve)).and(
                hasClickAction()
            )
        ).performClick()
        return this
    }

    fun clickCancel(): SettingsPageRobot{
        composeRule.onNode(
            hasText(getText(R.string.cancel)).and(
                hasClickAction()
            )
        ).performClick()
        return this
    }

    fun getText(resId: Int, vararg args: String): String{
        return context.getString(resId, *args)
    }

}