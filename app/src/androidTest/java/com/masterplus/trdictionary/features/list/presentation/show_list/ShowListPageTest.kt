package com.masterplus.trdictionary.features.list.presentation.show_list

import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.masterplus.trdictionary.HiltBaseClassForTest
import com.masterplus.trdictionary.MainActivity
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.core.app.AppRobot
import com.masterplus.trdictionary.core.data.local.mapper.toListEntity
import com.masterplus.trdictionary.core.utils.sample_data.listModel
import com.masterplus.trdictionary.features.app.domain.model.AppNavRoute
import com.masterplus.trdictionary.features.app.presentation.MyApp
import com.masterplus.trdictionary.features.list.presentation.show_list.constants.ShowListBottomMenuEnum
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking

import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@HiltAndroidTest
class ShowListPageTest: HiltBaseClassForTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var appRobot: AppRobot
    private lateinit var listRobot: ShowListPageRobot

    override fun setUp() {
        super.setUp()
        appRobot = AppRobot(composeRule)
        listRobot = ShowListPageRobot(composeRule)

        appRobot.init(initAppNavRoute = AppNavRoute.List)
    }

    @Test
    fun whenNewListAdded_shouldBeVisibleInTheList(){
        val listName = "my list x"
        listRobot
            .addNewList(listName)
            .assertListExists(listName)
    }

    @Test
    fun renameExistedListName(){
        val listName = "list name"
        val newListName = "new list name"
        runBlocking {
            db.listDao().insertList(listEntity = listModel(name = listName).toListEntity())
        }
        listRobot
            .assertListExists(listName)
            .openItemMenu(listName)
            .clickMenuItem(ShowListBottomMenuEnum.Rename)
            .assertEditTextHasText(listName)
            .renameItemName(newListName)
            .approveDialog()
            .assertListExists(newListName)
            .assertListIsNotDisplayed(listName)
    }

    @Test
    fun deleteExistedList(){
        val listName = "list name"
        runBlocking {
            db.listDao().insertList(listEntity = listModel(name = listName).toListEntity())
        }
        listRobot
            .openItemMenu(listName)
            .clickMenuItem(ShowListBottomMenuEnum.Delete)
            .approveDialog()
            .assertListIsNotDisplayed(listName)
    }

    @Test
    fun copyExistedList(){
        val listName = "list name"
        runBlocking {
            db.listDao().insertList(listEntity = listModel(name = listName).toListEntity())
        }
        listRobot
            .openItemMenu(listName)
            .clickMenuItem(ShowListBottomMenuEnum.Copy)
            .approveDialog()
            .assertListCount(listName, 2)
    }

    @Test
    fun whenArchiveList_shouldBeInvisible(){
        val listName = "list name"
        runBlocking {
            db.listDao().insertList(listEntity = listModel(name = listName).toListEntity())
        }
        listRobot
            .openItemMenu(listName)
            .clickMenuItem(ShowListBottomMenuEnum.Archive)
            .approveDialog()
            .assertListIsNotDisplayed(listName)
    }

    @Test
    fun favoriteListShouldHaveCopyAndRenameMenuItems(){
        val listName = "list name"
        runBlocking {
            db.listDao().insertList(listEntity = listModel(name = listName, isRemovable = false).toListEntity())
        }
        listRobot
            .openItemMenu(listName)
            .assertOnlyMenuItemsExists(
                listOf(ShowListBottomMenuEnum.Copy, ShowListBottomMenuEnum.Rename)
            )
    }
}