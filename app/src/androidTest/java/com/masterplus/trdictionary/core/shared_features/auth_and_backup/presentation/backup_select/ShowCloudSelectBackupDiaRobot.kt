package com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.backup_select

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.masterplus.trdictionary.shared_test.rules.AppComposeRule
import com.masterplus.trdictionary.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
class ShowCloudSelectBackupDiaRobot constructor(
    private val composeRule: AppComposeRule
) {

    fun openBackupDia(): ShowCloudSelectBackupDiaRobot{
        composeRule.onNodeWithText(getText(R.string.cloud_backup))
            .performClick()
        composeRule.onNode(
            hasText(getText(R.string.download_from_cloud)).and(
                hasClickAction()
            )
        ).performClick()
        return this
    }

    fun clickRefreshButton(): ShowCloudSelectBackupDiaRobot{
        composeRule
            .onNode(hasText(getText(R.string.refresh)).and(hasClickAction()))
            .performClick()
        return this
    }

    fun assertRefreshButtonIsEnabled(isEnabled: Boolean): ShowCloudSelectBackupDiaRobot{
        composeRule
            .onNode(hasText(getText(R.string.refresh)).and(hasClickAction())).apply {
                if(isEnabled) assertIsEnabled()
                else assertIsNotEnabled()
            }
        return this
    }

    fun assertNBackupItemsDisplay(n: Int): ShowCloudSelectBackupDiaRobot {
        (0..<n).forEach { index->
            composeRule
                .onAllNodes(
                    hasText("Backup",substring = true).and(hasClickAction())
                )[index]
                .assertIsDisplayed()
        }
        return this
    }

    fun clickNBackupItem(pos: Int): ShowCloudSelectBackupDiaRobot{
        composeRule
            .onAllNodes(
                hasText("Backup",substring = true).and(hasClickAction())
            )[pos]
            .performClick()
        return this
    }

    fun assertNBackupItemIsSelected(pos: Int, isSelected: Boolean): ShowCloudSelectBackupDiaRobot{
        composeRule
            .onAllNodes(
                hasText("Backup",substring = true).and(hasClickAction())
            )[pos]
            .apply {
                if(isSelected) assertIsSelected()
                else assertIsNotSelected()
            }
        return this
    }


    fun assertOverrideButtonIsEnabled(isEnabled: Boolean): ShowCloudSelectBackupDiaRobot{
        composeRule.onNode(
            hasText(getText(R.string.override)).and(hasClickAction())
        ).apply {
            if(isEnabled) assertIsEnabled()
            else assertIsNotEnabled()
        }
        return this
    }

    fun assertAddOnButtonIsEnabled(isEnabled: Boolean): ShowCloudSelectBackupDiaRobot{
        composeRule.onNode(
            hasText(getText(R.string.add_on)).and(hasClickAction())
        ).apply {
            if(isEnabled) assertIsEnabled()
            else assertIsNotEnabled()
        }
        return this
    }

    fun assertEmptyContentIsDisplayed(): ShowCloudSelectBackupDiaRobot{
        assertTextIsDisplayed(R.string.empty_backup_select)
        return this
    }

    fun assertTextIsDisplayed(resId: Int): ShowCloudSelectBackupDiaRobot{
        composeRule.onNodeWithText(getText(resId))
            .assertIsDisplayed()
        return this
    }

    fun waitForIdle(): ShowCloudSelectBackupDiaRobot{
        composeRule.waitForIdle()
        return this
    }

    fun getText(resId: Int, vararg args: String): String{
        return composeRule.activity.getString(resId, *args)
    }
}