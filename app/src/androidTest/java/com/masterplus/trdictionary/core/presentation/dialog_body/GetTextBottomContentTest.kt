package com.masterplus.trdictionary.core.presentation.dialog_body

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performTextInput
import com.masterplus.trdictionary.R
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
class GetTextBottomContentTest {

    @get: Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var restorationTester: StateRestorationTester
    private lateinit var context: Context
    private lateinit var textRobot: GetTextBottomContentRobot

    @Before
    fun setUp() {
        context = composeRule.activity.applicationContext
        restorationTester = StateRestorationTester(composeRule)
        textRobot = GetTextBottomContentRobot(composeRule)
    }


    @Test
    fun shouldRestoreChangedText(){
        restorationTester.setContent {
            ShowGetTextDialog(
                title = "sample",
                onClosed = {},
                onApproved = {}
            )
        }
        val enteredText = "sampleText"

        textRobot.performText(enteredText)

        restorationTester.emulateSavedInstanceStateRestore()

        textRobot.assertTextIsEqual(enteredText)
    }

    @Test
    fun whenDefaultTextExists_shouldBeVisibleAndRestore(){
        val defaultText = "defaultText"
        restorationTester.setContent {
            ShowGetTextDialog(
                title = "sample",
                content = defaultText,
                onClosed = {},
                onApproved = {}
            )
        }
        textRobot.assertTextIsEqual(defaultText)
        restorationTester.emulateSavedInstanceStateRestore()
        textRobot.assertTextIsEqual(defaultText)
    }

    @Test
    fun whenDefaultTextExistsAndStateRestore_shouldBeTextRestore(){
        val defaultText = "defaultText"
        restorationTester.setContent {
            ShowGetTextDialog(
                title = "sample",
                content = defaultText,
                onClosed = {},
                onApproved = {}
            )
        }
        val enteredText = "sampleText"
        val expectedText = defaultText + enteredText

        textRobot.
            performText(enteredText)
            .assertTextIsEqual(expectedText)

        restorationTester.emulateSavedInstanceStateRestore()
        textRobot.assertTextIsEqual(expectedText)
    }


}