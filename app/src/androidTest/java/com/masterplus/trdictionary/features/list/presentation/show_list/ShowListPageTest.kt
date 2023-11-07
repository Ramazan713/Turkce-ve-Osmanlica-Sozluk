@file:OptIn(ExperimentalTestApi::class)

package com.masterplus.trdictionary.features.list.presentation.show_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.google.common.truth.Truth.assertThat
import com.masterplus.trdictionary.MainActivity
import com.masterplus.trdictionary.core.di.AppModule
import com.masterplus.trdictionary.features.list.presentation.show_list.navigation.RouteList
import com.masterplus.trdictionary.features.list.presentation.show_list.navigation.showListPage
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.masterplus.trdictionary.R
import com.masterplus.trdictionary.features.list.presentation.archive_list.navigation.RouteArchive
import com.masterplus.trdictionary.features.list.presentation.archive_list.navigation.archiveListPage
import com.masterplus.trdictionary.features.list.presentation.archive_list.navigation.navigateToArchiveList

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@HiltAndroidTest
@UninstallModules(AppModule::class)
class ShowListPageTest {

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule(MainActivity::class.java)

    private lateinit var navController: NavHostController
    private lateinit var activity: MainActivity
    private lateinit var title: String

    @OptIn(ExperimentalMaterial3Api::class)
    @Before
    fun setUp() {
        hiltRule.inject()

        title = "my title"
        activity = composeRule.activity

        composeRule.setContent {
            navController = rememberNavController()

            NavHost(navController, startDestination = RouteList){
                showListPage(
                    onNavigateToArchive = {
                        navController.navigateToArchiveList()
                    },
                    onNavigateToSelectSavePoint = {title,filter,typeId ->},
                    onNavigateToDetailList = {},
                    onNavigateToSettings = {}
                )

                archiveListPage(
                    onNavigateBack = {navController.popBackStack()},
                    onNavigateToDetailList = {}
                )
            }
        }

    }


    @Test
    fun addNewList(){
        //open dialog
        composeRule.onNodeWithContentDescription(activity.getString(R.string.add_list)).performClick()

        //enter some text
        composeRule.onNodeWithText(activity.getString(R.string.enter_title)).assertIsDisplayed()
        composeRule.onNodeWithTag(activity.getString(R.string.edit_text_testTitle)).performTextInput(title)
        composeRule.onNodeWithText(activity.getString(R.string.approve)).performClick()

        //make sure title is visible
        composeRule.onNodeWithText(activity.getString(R.string.enter_title)).assertDoesNotExist()
        composeRule.onNodeWithText(title).assertIsDisplayed()

    }

    @Test
    fun deleteList(){
        addList()

        //open menu
        composeRule.onNodeWithContentDescription(activity.getString(R.string.list_menu_item))
            .performClick()

        //click delete icon
        composeRule.onNodeWithContentDescription(activity.getString(R.string.delete)).assertIsDisplayed()
        composeRule.onNodeWithContentDescription(activity.getString(R.string.delete)).performClick()
        composeRule.onNodeWithContentDescription(activity.getString(R.string.delete)).assertDoesNotExist()

        //press approve button for deleting
        composeRule.onNodeWithText(activity.getString(R.string.approve)).performClick()

        //make sure menu and list not exists
        composeRule.onNodeWithText(title).assertDoesNotExist()

    }

    @Test
    fun renameList(){
        val newTitle = "my New Title"

        addList()

        //open menu
        composeRule.onNodeWithContentDescription(activity.getString(R.string.list_menu_item))
            .performClick()

        //click rename icon
        composeRule.onNodeWithContentDescription(activity.getString(R.string.rename)).performClick()
        composeRule.onNodeWithContentDescription(activity.getString(R.string.rename)).assertDoesNotExist()

        //update title
        composeRule.onNodeWithTag(activity.getString(R.string.edit_text_testTitle)).assertTextContains(title)
        composeRule.onNodeWithTag(activity.getString(R.string.edit_text_testTitle)).performTextReplacement(newTitle)
        composeRule.onNodeWithText(activity.getString(R.string.approve)).performClick()


        composeRule.onNodeWithText(newTitle).assertIsDisplayed()
        composeRule.onNodeWithText(title).assertDoesNotExist()

    }

    @Test
    fun archiveList(){
        addList()

        //open menu
        composeRule.onNodeWithContentDescription(activity.getString(R.string.list_menu_item))
            .performClick()

        //click archive icon
        composeRule.onNodeWithContentDescription(activity.getString(R.string.archive_v)).performClick()
        composeRule.onNodeWithContentDescription(activity.getString(R.string.archive_v)).assertDoesNotExist()

        //press approve button for archiving
        composeRule.onNodeWithText(activity.getString(R.string.approve)).performClick()

        composeRule.onNodeWithText(title).assertDoesNotExist()
    }

    @Test
    fun copyList(){
        addList()

        //open menu
        composeRule.onNodeWithContentDescription(activity.getString(R.string.list_menu_item))
            .performClick()

        //click copy icon
        composeRule.onNodeWithContentDescription(activity.getString(R.string.copy_make)).performClick()
        composeRule.onNodeWithContentDescription(activity.getString(R.string.copy_make)).assertDoesNotExist()

        //press approve button for archiving
        composeRule.onNodeWithText(activity.getString(R.string.approve)).performClick()

        composeRule.onAllNodesWithText(text = title, substring = true).assertCountEquals(2)
    }

    @Test
    fun clickArchiveIcon_navigateToArchivePage(){

        // go to archive
        composeRule.onNodeWithContentDescription(activity.getString(R.string.archive_n)).performClick()
        val goToArchiveRoute = navController.currentBackStackEntry?.destination?.route

        // navigate back
        composeRule.onNodeWithContentDescription(activity.getString(R.string.navigation_back)).performClick()
        val navigateBackRoute = navController.currentBackStackEntry?.destination?.route

        assertThat(RouteArchive).isEqualTo(goToArchiveRoute)
        assertThat(RouteList).isEqualTo(navigateBackRoute)
    }


    @Test
    fun whenDialogShowingRotateDevice_shouldShowDialog() {

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        uiDevice.waitForIdle()
        uiDevice.findObject(UiSelector().textContains(activity.getString(R.string.list))).click()

        addList()

        //open menu
        composeRule.onNodeWithContentDescription(activity.getString(R.string.list_menu_item)).performClick()

        composeRule.onNodeWithContentDescription(activity.getString(R.string.copy_make)).assertIsDisplayed()

        uiDevice.setOrientationLeft()
        uiDevice.setOrientationRight()

        composeRule.onNodeWithContentDescription(activity.getString(R.string.copy_make)).assertIsDisplayed()
    }


    @Test
    fun whenDialogShowingRotateDevice_shouldShowDialog2() {

        val restorationTester = androidx.compose.ui.test.junit4.StateRestorationTester(composeRule)

        restorationTester.setContent {
            navController = rememberNavController()

            NavHost(navController, startDestination = RouteList){
                showListPage(
                    onNavigateToArchive = {
                        navController.navigateToArchiveList()
                    },
                    onNavigateToSelectSavePoint = {title,filter,typeId ->},
                    onNavigateToDetailList = {},
                    onNavigateToSettings = {}
                )

                archiveListPage(
                    onNavigateBack = {navController.popBackStack()},
                    onNavigateToDetailList = {}
                )
            }
        }
        addList()

        //open menu
        composeRule.onNodeWithContentDescription(activity.getString(R.string.list_menu_item)).performClick()

        composeRule.onNodeWithContentDescription(activity.getString(R.string.copy_make)).assertIsDisplayed()

        restorationTester.emulateSavedInstanceStateRestore()

        composeRule.onNodeWithContentDescription(activity.getString(R.string.copy_make)).assertIsDisplayed()
    }

    private fun addList(titleParam: String = title){
        //add list
        composeRule.onNodeWithContentDescription(activity.getString(R.string.add_list)).performClick()
        composeRule.onNodeWithTag(activity.getString(R.string.edit_text_testTitle)).performTextInput(titleParam)
        composeRule.onNodeWithText(activity.getString(R.string.approve)).performClick()
        composeRule.onNodeWithText(titleParam).assertIsDisplayed()
    }

}