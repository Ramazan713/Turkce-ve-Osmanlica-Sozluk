package com.masterplus.trdictionary.features.savepoint.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.shared_test.rules.AppComposeRule
import com.masterplus.trdictionary.features.savepoint.presentation.constants.SelectSavePointMenuItem
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
class SelectSavePointPageRobot constructor(
    private val composeRule: AppComposeRule
) {

    private val context = composeRule.activity.applicationContext

    fun assertEmptyTextVisible(): SelectSavePointPageRobot{
        composeRule.onNodeWithText(getText(R.string.empty_savepoint))
            .assertIsDisplayed()
        return this
    }

    fun assertButtonIsEnabled(isEnabled: Boolean): SelectSavePointPageRobot{
        composeRule.onNode(
            hasText(getText(R.string.load_and_go)).and(
                hasClickAction()
            )
        ).apply {
            if(isEnabled)
                assertIsEnabled()
            else
                assertIsNotEnabled()
        }
        return this
    }

    fun clickButton(): SelectSavePointPageRobot{
        composeRule.onNode(
            hasText(getText(R.string.load_and_go)).and(
                hasClickAction()
            )
        ).performClick()
        return this
    }

    fun assertSavePointIsDisplayed(text: String): SelectSavePointPageRobot{
        getSavepointNode(text)
            .isDisplayed()
        return this
    }

    fun assertSavePointIsNotDisplayed(text: String): SelectSavePointPageRobot{
        getSavepointNode(text)
            .isNotDisplayed()
        return this
    }

    fun clickSavePoint(title: String): SelectSavePointPageRobot{
        getSavepointNode(title)
            .performClick()
        return this
    }

    fun assertSavePointIsSelected(title: String, isSelected: Boolean): SelectSavePointPageRobot{
        getSavepointNode(title)
            .apply {
                if(isSelected){
                    assertIsSelected()
                }else{
                    assertIsNotSelected()
                }
            }
        return this
    }

    fun clickDeleteSavePoint(title: String): SelectSavePointPageRobot{
        composeRule
            .onNode(
                hasContentDescription(getText(R.string.delete)).and( // has delete button
                    hasAnyAncestor(hasText(title).and(hasClickAction())) // has parent Savepoint
                )
            )
            .performClick()
        return this
    }

    fun clickEditTitleSavePoint(title: String): SelectSavePointPageRobot{
        composeRule
            .onNode(
                hasContentDescription(getText(R.string.edit_title)).and( // has delete button
                    hasAnyAncestor(hasText(title).and(hasClickAction())) // has parent Savepoint
                )
            )
            .performClick()
        return this
    }


    fun approveDialog(): SelectSavePointPageRobot{
        composeRule
            .onNode(
                hasText(getText(R.string.approve)).and(
                    hasClickAction())
            )
            .performClick()
        return this
    }

    fun updateSavepointTitleInOneTime(oldTitle: String, newTitle: String): SelectSavePointPageRobot{
        clickEditTitleSavePoint(oldTitle)
        composeRule.onNodeWithContentDescription(getText(R.string.text_field)).apply {
            performTextClearance()
            performTextInput(newTitle)
        }
        approveDialog()
        return this
    }


    fun clickDropdownMenu(): SelectSavePointPageRobot{
        composeRule
            .onNodeWithContentDescription(getText(R.string.dropdown_menu_text))
            .performClick()
        return this
    }

    fun selectDropdownMenuItem(menuItem: SelectSavePointMenuItem): SelectSavePointPageRobot{
        composeRule
            .onNodeWithContentDescription(getText(R.string.n_menu_item,menuItem.title.asString(context)))
            .performClick()
        return this
    }

    fun assertDropdownMenuSelected(menuItem: SelectSavePointMenuItem): SelectSavePointPageRobot{
        composeRule
            .onNode(
                hasContentDescription(getText(R.string.dropdown_menu_text)).and(
                    hasText(menuItem.title.asString(context))
                )
            ).isDisplayed()
        return this
    }

    fun awaitIdle(){
        runBlocking {
            composeRule.awaitIdle()
        }
    }



    private fun getSavepointNode(title: String): SemanticsNodeInteraction {
        return composeRule.onNode(
            hasText(title).and(hasClickAction()),
            useUnmergedTree = false
        )
    }



    private fun getText(resId: Int, vararg args: String): String{
        return context.getString(resId, *args)
    }

}