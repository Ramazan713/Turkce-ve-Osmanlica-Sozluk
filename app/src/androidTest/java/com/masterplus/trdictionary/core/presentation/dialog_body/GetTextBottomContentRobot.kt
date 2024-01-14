package com.masterplus.trdictionary.core.presentation.dialog_body

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTextInput
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.app.AppComponentRule

class GetTextBottomContentRobot constructor(
    private val composeRule: AppComponentRule
) {
    private val context = composeRule.activity.applicationContext

    private fun findTextField(): SemanticsNodeInteraction{
        return composeRule.onNodeWithContentDescription(context.getString(R.string.text_field))
    }

    fun performText(text: String): GetTextBottomContentRobot{
        findTextField()
            .performTextInput(text)
        return this
    }

    fun assertTextIsEqual(text: String): GetTextBottomContentRobot{
        findTextField()
            .assertTextEquals(text)
        return this
    }
}