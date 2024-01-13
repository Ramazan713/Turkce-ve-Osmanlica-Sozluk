package com.masterplus.trdictionary.features.list.presentation.show_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.masterplus.trdictionary.core.app.AppComposeRule
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.features.list.presentation.show_list.constants.ShowListBottomMenuEnum

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
class ShowListPageRobot constructor(
    private val composeRule: AppComposeRule
) {

    fun addNewList(listName: String): ShowListPageRobot{
        composeRule.onNodeWithContentDescription(getText(R.string.add_list))
            .performClick()

        composeRule.onNodeWithContentDescription(getText(R.string.text_field))
            .performTextInput(listName)

        approveDialog()
        return this
    }

    fun assertListExists(listName: String): ShowListPageRobot{
        assertTextExists(listName)
        return this
    }

    fun assertTextExists(text: String): ShowListPageRobot{
        composeRule.onNodeWithText(text).isDisplayed()
        return this
    }

    fun assertListIsNotDisplayed(listName: String): ShowListPageRobot{
        assertTextIsNotDisplayed(listName)
        return this
    }

    fun assertTextIsNotDisplayed(text: String): ShowListPageRobot{
        composeRule.onNodeWithText(text).assertIsNotDisplayed()
        return this
    }

    fun openItemMenu(listName: String): ShowListPageRobot{
        composeRule.onNodeWithContentDescription(getText(R.string.menu_n, listName)).performClick()
        return this
    }

    fun assertEditTextHasText(text: String): ShowListPageRobot{
        composeRule.onNodeWithContentDescription(getText(R.string.text_field))
            .assertTextEquals(text)
        return this
    }

    fun clickMenuItem(menuItem: ShowListBottomMenuEnum): ShowListPageRobot{
        composeRule.onNodeWithText(menuItem.title.asString(composeRule.activity.applicationContext)).performClick()
        return this
    }

    fun assertOnlyMenuItemsExists(items: List<ShowListBottomMenuEnum>): ShowListPageRobot{
        val otherMenuItems = ShowListBottomMenuEnum.entries.toTypedArray().toMutableList().apply {
            removeAll(items)
        }
        val context = composeRule.activity.applicationContext

        items.forEach {
            assertTextExists(it.title.asString(context))
        }
        otherMenuItems.forEach {
            assertTextIsNotDisplayed(it.title.asString(context))
        }
        return this
    }

    fun renameItemName(newName: String): ShowListPageRobot{
        composeRule.onNodeWithContentDescription(getText(R.string.text_field)).apply {
            performTextClearance()
            performTextInput(newName)
        }
        return this
    }

    fun assertListCount(listName: String, count: Int): ShowListPageRobot{
        composeRule.onAllNodesWithText(listName,true).assertCountEquals(count)
        return this
    }


    fun approveDialog(): ShowListPageRobot{
        composeRule.onNode(
            hasText(getText(R.string.approve)).and(hasClickAction())
        ).performClick()
        return this
    }

    private fun getText(resId: Int, vararg formatArgs: String): String{
        return composeRule.activity.applicationContext.getString(resId, *formatArgs)
    }
}