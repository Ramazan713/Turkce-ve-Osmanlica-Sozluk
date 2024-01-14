package com.masterplus.trdictionary.features.savepoint.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.masterplus.trdictionary.HiltBaseClassForTest
import com.masterplus.trdictionary.MainActivity
import com.masterplus.trdictionary.core.app.AppRobot
import com.masterplus.trdictionary.core.domain.constants.K
import com.masterplus.trdictionary.core.domain.enums.CategoryEnum
import com.masterplus.trdictionary.core.domain.enums.SavePointDestination
import com.masterplus.trdictionary.core.domain.enums.SavePointType
import com.masterplus.trdictionary.core.domain.enums.SubCategoryEnum
import com.masterplus.trdictionary.core.domain.model.SavePoint
import com.masterplus.trdictionary.core.domain.repo.SavePointRepo
import com.masterplus.trdictionary.core.utils.sample_data.savePoint
import com.masterplus.trdictionary.features.savepoint.presentation.constants.SelectSavePointMenuItem
import com.masterplus.trdictionary.features.savepoint.presentation.navigation.navigateToSelectSavePoint
import com.masterplus.trdictionary.features.word_detail.word_category.navigation.RouteListDetailCategoryWords
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking

import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@HiltAndroidTest
class SelectSavePointPageTest: HiltBaseClassForTest() {


    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var savePointRepo: SavePointRepo

    private lateinit var savePointRobot: SelectSavePointPageRobot
    private lateinit var appRobot: AppRobot
    private lateinit var navController: TestNavHostController

    private var savePointType = SavePointType.TrDict
    private var savePointTitle = "sample title"

    override fun setUp() {
        super.setUp()

        appRobot = AppRobot(composeRule)
        savePointRobot = SelectSavePointPageRobot(composeRule)

        navController = TestNavHostController(composeRule.activity.applicationContext)
        navController.navigatorProvider.addNavigator(ComposeNavigator())

        appRobot.init(
            initNavHostController = navController,
            onInitNavigateTo = {
                navController.navigateToSelectSavePoint(
                    typeId = savePointType.typeId,
                    destinationFilters = SavePointDestination.categoryDestinationIds,
                    title = savePointTitle
                )
            }
        )
    }

    @Test
    fun whenSavePointsDoesNotExists_shouldAppearEmptyText(){
        savePointRobot.assertEmptyTextVisible()
    }

    @Test
    fun whenSavePointsExists_shouldBeVisibleAndButtonIsNotEnabled(){
        val title = "savepoint sample"
        insertSavePointWithOverrideType(savePoint = savePoint(title = title))

        savePointRobot
            .assertSavePointIsDisplayed(title)
            .assertSavePointIsSelected(title, false)
            .assertButtonIsEnabled(isEnabled = false)
    }

    @Test
    fun whenSavePointSelected_shouldButtonEnabled(){
        val title = "savepoint sample"
        insertSavePointWithOverrideType(savePoint = savePoint(title = title))

        savePointRobot
            .apply {
                composeRule.onRoot().printToLog("myTreeLog")
            }
            .clickSavePoint(title)
            .apply {
                composeRule.onRoot().printToLog("myTreeLog2XX")
            }
            .assertButtonIsEnabled(true)
            .assertSavePointIsSelected(title, true)
    }

    @Test
    fun whenSelectedSavePointDeleted_shouldButtonIsDisabled(){
        val title = "savepoint sample"
        insertSavePointWithOverrideType(savePoint = savePoint(id = 1, title = title))
        insertSavePointWithOverrideType(savePoint = savePoint(id = 2, title = "title 2"))

        savePointRobot
            .clickSavePoint(title)
            .assertButtonIsEnabled(true)
            .clickDeleteSavePoint(title)
            .approveDialog()
            .assertSavePointIsNotDisplayed(title)
            .assertButtonIsEnabled(false)
    }

    @Test
    fun whenSelectedSavePointTitleUpdated_shouldRemainButtonEnabled(){
        val title = "savepoint sample"
        val updatedTitle = "savepoint updated Title"
        insertSavePointWithOverrideType(savePoint = savePoint(id = 1, title = title))

        savePointRobot
            .clickSavePoint(title)
            .assertButtonIsEnabled(true)
            .updateSavepointTitleInOneTime(title, updatedTitle)
            .assertSavePointIsNotDisplayed(title)
            .assertSavePointIsDisplayed(updatedTitle)
            .assertButtonIsEnabled(true)
    }

    @Test
    fun whenSavePointTypesIsRandomAndFilterIsDifferent_shouldDisappearSavePoint(){
        val title = "savepoint sample"
        insertSavePoint(savePoint(title = title, savePointDestination = SavePointDestination.CategoryRandom(savePointType)))

        savePointRobot
            .assertSavePointIsDisplayed(title)
            .clickDropdownMenu()
            .selectDropdownMenuItem(SelectSavePointMenuItem.CatAlphabetic)
            .assertDropdownMenuSelected(SelectSavePointMenuItem.CatAlphabetic)
            .assertSavePointIsNotDisplayed(title)
            .assertEmptyTextVisible()
    }

    @Test
    fun whenLoadButtonClick_shouldNavigateToWordsDetailCategory(){
        val title = "savepoint sample"
        val pos = 13
        insertSavePointWithOverrideType(savePoint(
            title = title,
            itemPosIndex = pos
        ))
        savePointRobot
            .clickSavePoint(title)
            .assertButtonIsEnabled(true)
            .clickButton()
            .awaitIdle()

        val currentRoute = navController.currentBackStackEntry?.destination?.route
        assertThat(currentRoute).isEqualTo(RouteListDetailCategoryWords)
    }

    private fun insertSavePoint(
        savePoint: SavePoint = savePoint(),
    ){
        runBlocking {
            savePointRepo.insertSavePoint(savePoint)
        }
    }

    private fun insertSavePointWithOverrideType(
        savePoint: SavePoint = savePoint()
    ){
        insertSavePoint(savePoint.copy(savePointDestination = SavePointDestination.CategoryRandom(savePointType)))
    }

}