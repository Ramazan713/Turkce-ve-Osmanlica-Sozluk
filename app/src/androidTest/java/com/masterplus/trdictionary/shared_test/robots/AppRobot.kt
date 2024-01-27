package com.masterplus.trdictionary.shared_test.robots

import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.window.layout.DisplayFeature
import com.masterplus.trdictionary.features.app.domain.model.AppNavRoute
import com.masterplus.trdictionary.features.app.presentation.MyApp
import com.masterplus.trdictionary.shared_test.rules.AppComposeRule
import com.masterplus.trdictionary.shared_test.utils.AsyncTimer
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class, ObsoleteCoroutinesApi::class
)
class AppRobot(
    private val composeRule: AppComposeRule,
    private val windowWidthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    private val displayFeatures: List<DisplayFeature> = listOf()
)  {
    private lateinit var navController: NavHostController

    fun init(
        initNavHostController: NavHostController? = null,
        onInitNavigateTo: (NavController) -> Unit
    ){
        setNavHostController(initNavHostController)
        initPage()
        navigateTo(onInitNavigateTo)
    }

    fun init(
        initNavHostController: NavHostController? = null,
        initAppNavRoute: AppNavRoute
    ){
        setNavHostController(initNavHostController)
        initPage()
        navigateTo(initAppNavRoute)
    }

    fun navigateTo(onAction: (NavController) -> Unit){
        runBlocking<Unit> {
            composeRule.awaitIdle()
            composeRule.runOnUiThread {
                onAction(navController)
            }
        }
    }

    fun asyncWait(delay: Long = 1000): AppRobot {
        AsyncTimer.start (delay)
        composeRule.waitUntil (
            condition = { AsyncTimer.expired},
            timeoutMillis = delay + 1000
        )
        return this
    }

    fun navigateTo(appNavRoute: AppNavRoute, popUp: Boolean = true){
        runBlocking<Unit> {
            composeRule.awaitIdle()
            composeRule.runOnUiThread {
                navController.navigate(appNavRoute.route){
                    if(popUp){
                        this.popUpTo(appNavRoute.route) {
                            inclusive = false
                        }
                    }
                }
            }
        }
    }


    private fun initPage(){
        composeRule.activity.setContent {
            MyApp(
                navHostController = navController,
                displayFeatures = displayFeatures,
                windowSizeClass = windowWidthSizeClass
            )
        }
    }

    private fun setNavHostController(initNavHostController: NavHostController?){
        if(initNavHostController != null){
            navController = initNavHostController
        }else{
            navController = TestNavHostController(composeRule.activity.applicationContext)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
        }
    }
}