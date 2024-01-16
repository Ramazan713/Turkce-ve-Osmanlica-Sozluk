package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.shared_test.rules.CustomInitAppComposeRule
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class, ExperimentalTestApi::class
)
class WordsPagerListDetailAdaptivePageRobot constructor(
    private val composeRule: CustomInitAppComposeRule
) {
    private val context = composeRule.activity.applicationContext

    fun navigateTo(pos: Int, isDetail: Boolean): WordsPagerListDetailAdaptivePageRobot{
        clickTopBar(isDetail)
        composeRule.onNodeWithContentDescription(getText(R.string.text_field)).apply {
            performTextClearance()
            performTextInput(pos.toString())
        }
        approveDialog()
        return this
    }


    fun approveDialog(): WordsPagerListDetailAdaptivePageRobot{
        composeRule.onNodeWithText(getText(R.string.approve))
            .performClick()
        return this
    }

    fun clickListWord(word: String): WordsPagerListDetailAdaptivePageRobot{
        composeRule.onNode(hasText(word).and(hasClickAction())).performClick()
        return this
    }

    fun clickNavigationBackIcon(): WordsPagerListDetailAdaptivePageRobot{
        composeRule.onNodeWithContentDescription(getText(R.string.navigation_back))
            .performClick()
        return this
    }

    fun clickNextButton(): WordsPagerListDetailAdaptivePageRobot{
        composeRule.onNode(hasText(getText(R.string.next)).and(hasClickAction()))
            .performClick()
        return this
    }

    fun clickPrevButton(): WordsPagerListDetailAdaptivePageRobot{
        composeRule.onNode(hasText(getText(R.string.previous)).and(hasClickAction()))
            .performClick()
        return this
    }

    fun assertPosInListAndDetailVisible(pos: Int): WordsPagerListDetailAdaptivePageRobot{
        return assertPosVisibleInList(pos)
            .assertPosVisibleInDetail(pos)
    }

    fun assertPosVisibleInList(pos: Int): WordsPagerListDetailAdaptivePageRobot{
        composeRule.onNode(getListPosSemanticMatcher(pos))
            .assertIsDisplayed()
        return this
    }

    fun assertWordVisible(word: String): WordsPagerListDetailAdaptivePageRobot{
        composeRule.onNodeWithText(word)
            .isDisplayed()
        return this
    }

    fun assertListAndDetailWordVisible(word: String): WordsPagerListDetailAdaptivePageRobot{
        composeRule.onAllNodesWithText(word)
            .assertCountEquals(2)
        return this
    }

    fun assertPosVisibleInDetail(pos: Int): WordsPagerListDetailAdaptivePageRobot{
        composeRule.onNode(getDetailPosSemanticMatcher(pos))
            .assertIsDisplayed()
        return this
    }

    fun waitForPager(pos: Int): WordsPagerListDetailAdaptivePageRobot{
        composeRule.waitUntilExactlyOneExists(getDetailPosSemanticMatcher(pos))
        return this
    }

    fun waitForLazyList(pos: Int): WordsPagerListDetailAdaptivePageRobot{
        composeRule.waitUntilExactlyOneExists(getListPosSemanticMatcher(pos))
        return this
    }

    fun scrollToLazyList(index: Int): WordsPagerListDetailAdaptivePageRobot{
        composeRule.onNodeWithContentDescription(getText(R.string.lazy_vertical_list))
            .performScrollToIndex(index)
        return this
    }

    fun scrollToPager(index: Int): WordsPagerListDetailAdaptivePageRobot{
        composeRule.onNodeWithContentDescription(getText(R.string.horizontal_pager))
            .performScrollToIndex(index)
        return this
    }

    fun waitForIdle(): WordsPagerListDetailAdaptivePageRobot{
        runBlocking {
            composeRule.waitForIdle()
        }
        return this
    }

    private fun clickTopBar(isDetail: Boolean){
        val topBarRes = if(isDetail) R.string.detail_top_bar else R.string.list_top_bar
        composeRule.onNode(
            hasContentDescription(getText(R.string.navigator)).and(
                hasAnyAncestor(hasContentDescription(getText(topBarRes)))
            )
        ).performClick()
    }

    private fun getDetailPosSemanticMatcher(pos: Int): SemanticsMatcher{
        return hasText("$pos /", substring = true)
    }

    private fun getListPosSemanticMatcher(pos: Int): SemanticsMatcher{
        return hasText("$pos")
    }

    private fun getText(resId: Int, vararg args: String): String{
        return context.getString(resId,*args)
    }
}