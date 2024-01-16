package com.masterplus.trdictionary.shared_test.robots

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.masterplus.trdictionary.custom_init.InitRepo
import com.masterplus.trdictionary.shared_test.rules.CustomInitAppComposeRule
import com.masterplus.trdictionary.shared_test.utils.AsyncTimer
import kotlinx.coroutines.runBlocking

class CustomInitAppRobot(
    private val composeRule: CustomInitAppComposeRule,
    private val initRepo: InitRepo
)  {

    private val scenario = composeRule.activityRule.scenario

    fun init(
        windowWidthSizeClass: WindowWidthSizeClass,
        initRoute: String
    ){
        runBlocking<Unit> {
            composeRule.awaitIdle()
            composeRule.runOnUiThread {
                initRepo.init(
                    windowWidthSizeClass = windowWidthSizeClass,
                    destinationRoute = initRoute
                )
            }
        }
    }

    fun navigateTo(destRoute: String){
        runBlocking<Unit> {
            composeRule.awaitIdle()
            composeRule.runOnUiThread {
                initRepo.navigateTo(destRoute)
            }
        }
    }

    fun asyncWait(delay: Long = 1000): CustomInitAppRobot {
        AsyncTimer.start (delay)
        composeRule.waitUntil (
            condition = {AsyncTimer.expired},
            timeoutMillis = delay + 1000
        )
        return this
    }

    fun changeWindowSizeClassAndReCreate(
        windowWidthSizeClass: WindowWidthSizeClass
    ): CustomInitAppRobot{
        initRepo.changeWindowWidthClass(windowWidthSizeClass)
        scenario.recreate()
        return this
    }
}