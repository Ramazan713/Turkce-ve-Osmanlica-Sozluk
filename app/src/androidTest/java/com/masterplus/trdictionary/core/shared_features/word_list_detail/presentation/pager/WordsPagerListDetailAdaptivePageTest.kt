package com.masterplus.trdictionary.core.shared_features.word_list_detail.presentation.pager

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.masterplus.trdictionary.core.data.local.services.TestWordsDao
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.DictType
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.core.utils.sample_data.toEntity
import com.masterplus.trdictionary.core.utils.sample_data.word
import com.masterplus.trdictionary.shared_test.CustomInitHiltBaseClassForTest
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking

import org.junit.Test


@HiltAndroidTest
class WordsPagerListDetailAdaptivePageTest: CustomInitHiltBaseClassForTest() {

    lateinit var wordsTestDoa: TestWordsDao
    lateinit var wordsRobot: WordsPagerListDetailAdaptivePageRobot

    override fun setUp() {
        super.setUp()
        wordsTestDoa = db.wordsTestDao()
        wordsRobot = WordsPagerListDetailAdaptivePageRobot(composeRule)
    }

    @Test
    fun expandedMode_whenListNavigateToPos_shouldNavigateDetailToo(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Expanded)
        val destPos = 75
        wordsRobot
            .navigateTo(destPos,false)
            .assertPosInListAndDetailVisible(destPos)
    }

    @Test
    fun expandedMode_whenListScroll_shouldScrollDetailToo(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Expanded)
        val destPos = 75
        wordsRobot
            .scrollToLazyList(index = destPos - 1)
            .waitForPager(destPos)
            .assertPosInListAndDetailVisible(destPos)
    }

    @Test
    fun expandedMode_whenDetailNavigateToPos_shouldNavigateListToo(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Expanded)
        val destPos = 75
        wordsRobot
            .navigateTo(destPos,true)
            .assertPosInListAndDetailVisible(destPos)
    }

    @Test
    fun expandedMode_whenDetailScroll_shouldScrollListToo(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Expanded)
        val destPos = 75
        wordsRobot
            .scrollToPager(index = destPos - 1)
            .waitForLazyList(destPos)
            .assertPosInListAndDetailVisible(destPos)
    }

    @Test
    fun expandedMode_whenListItemClick_shouldChangeCurrentDetail(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Expanded)
        val destPos = 2
        val destWord = "word $destPos"

        wordsRobot
            .clickListWord(destWord)
            .assertPosInListAndDetailVisible(destPos)
    }

    @Test
    fun compactMode_whenListNavigateToPos_shouldBeVisibleInList(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Compact)
        val destPos = 75

        wordsRobot
            .navigateTo(destPos,false)
            .assertPosVisibleInList(destPos)
    }

    @Test
    fun compactMode_whenClickListItem_shouldOpenDetailPage(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Compact)
        val destPos = 75
        val destWord = "word $destPos"

        wordsRobot
            .navigateTo(destPos,false)
            .clickListWord(destWord)
            .assertPosVisibleInDetail(destPos)
            .assertWordVisible(destWord)
    }


    @Test
    fun compactMode_whenDetailNavigateToAndComeBack_shouldNavigateListToo(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Compact)
        val initPos = 20
        val initWord = "word $initPos"

        val destPos = 50
        val destWord = "word $destPos"

        wordsRobot
            .navigateTo(initPos,false)
            .clickListWord(initWord)
            .navigateTo(destPos, true)
            .waitForPager(destPos)
            .assertPosVisibleInDetail(destPos)
            .assertWordVisible(destWord)
            .clickNavigationBackIcon()
            .assertPosVisibleInList(destPos)
            .assertWordVisible(destWord)
    }


    @Test
    fun compactMode_whenDetailScrollAndBack_shouldNavigateList(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Compact)
        val initPos = 20
        val initWord = "word $initPos"

        val destPos = 50
        val destWord = "word $destPos"

        wordsRobot
            .navigateTo(initPos,false)
            .clickListWord(initWord)
            .scrollToPager(index = destPos - 1)
            .clickNavigationBackIcon()
            .assertPosVisibleInList(destPos)
            .assertWordVisible(destWord)
    }


    @Test
    fun compactMode_whenDetailClickBackTwoTimesAndBack_shouldNavigateList(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Compact)
        val initPos = 20
        val initWord = "word $initPos"
        val destPos = 18
        val destWord = "word $destPos"

        wordsRobot
            .navigateTo(initPos,false)
            .clickListWord(initWord)
            .clickPrevButton()
            .clickPrevButton()
            .clickNavigationBackIcon()
            .assertPosVisibleInList(destPos)
            .assertWordVisible(destWord)
    }

    @Test
    fun compactMode_whenDetailClickNextTwoTimesAndBack_shouldNavigateList(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Compact)
        val initPos = 20
        val initWord = "word $initPos"
        val destPos = 22
        val destWord = "word $destPos"

        wordsRobot
            .navigateTo(initPos,false)
            .clickListWord(initWord)
            .clickNextButton()
            .clickNextButton()
            .clickNavigationBackIcon()
            .assertPosVisibleInList(destPos)
            .assertWordVisible(destWord)
    }

    @Test
    fun changeMode_whenListNavigateTo_shouldNavigateToPagerToo(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Compact)
        val destPos = 22
        val destWord = "word $destPos"
        wordsRobot
            .navigateTo(destPos,false)
            .assertPosVisibleInList(destPos)
            .apply {
                appRobot.changeWindowSizeClassAndReCreate(WindowWidthSizeClass.Expanded)
            }
            .assertPosInListAndDetailVisible(destPos)
            .assertListAndDetailWordVisible(destWord)
    }

    @Test
    fun changeMode_whenListScrollTo_shouldNavigateToPager() {
        init(windowWidthSizeClass = WindowWidthSizeClass.Compact)
        val destPos = 22
        val destWord = "word $destPos"
        wordsRobot
            .waitForIdle()
            .scrollToLazyList(index = destPos - 1)
            .assertPosVisibleInList(destPos)
            .apply {
                appRobot
                    .asyncWait(500)
                    .changeWindowSizeClassAndReCreate(WindowWidthSizeClass.Expanded)
            }
            .waitForPager(destPos)
            .assertPosInListAndDetailVisible(destPos)
            .assertListAndDetailWordVisible(destWord)
    }

    @Test
    fun changeMode_whenDetailNavigateTo_shouldNavigateToList(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Compact)
        val destPos = 50
        val destWord = "word $destPos"

        wordsRobot
            .clickListWord("word 2")
            .navigateTo(destPos, true)
            .assertPosVisibleInDetail(destPos)
            .apply {
                appRobot
                    .asyncWait(500)
                    .changeWindowSizeClassAndReCreate(WindowWidthSizeClass.Expanded)
            }
            .waitForLazyList(destPos)
            .assertPosInListAndDetailVisible(destPos)
            .assertListAndDetailWordVisible(destWord)
    }

    @Test
    fun changeMode_whenDetailScrollTo_shouldNavigateToList(){
        init(windowWidthSizeClass = WindowWidthSizeClass.Compact)
        val destPos = 70
        val destWord = "word $destPos"
        wordsRobot
            .clickListWord("word 2")
            .scrollToPager(index = destPos - 1)
            .assertPosVisibleInDetail(destPos)
            .apply {
                appRobot
                    .asyncWait(500)
                    .changeWindowSizeClassAndReCreate(WindowWidthSizeClass.Expanded)
            }
            .waitForLazyList(destPos)
            .assertPosInListAndDetailVisible(destPos)
            .assertListAndDetailWordVisible(destWord)
    }


    fun init(
        windowWidthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Expanded,
        wordSize: Int = 100,
        initPos: Int = 0
    ){
        appRobot.init(
            windowWidthSizeClass = windowWidthSizeClass,
            initRoute = "listDetailCategoryWords/${CategoryEnum.TrDict.catId}/${SubCategoryEnum.Random.subCatId}/${K.defaultCategoryAlphaChar}/${initPos}"
        )
        val words = (1..wordSize).map {
            word(id = it, dictType = DictType.TR).toEntity()
        }
        runBlocking {
            wordsTestDoa.insertWords(words)
        }
        composeRule.waitForIdle()
    }

}

