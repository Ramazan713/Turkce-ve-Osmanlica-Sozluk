package com.masterplus.trdictionary.shared_test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.masterplus.trdictionary.custom_init.CustomInitTestActivity
import com.masterplus.trdictionary.custom_init.InitRepo
import com.masterplus.trdictionary.shared_test.robots.CustomInitAppRobot
import org.junit.Rule
import javax.inject.Inject

abstract class CustomInitHiltBaseClassForTest: HiltBaseClassForTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<CustomInitTestActivity>()

    @Inject
    lateinit var initRepo: InitRepo

    protected lateinit var appRobot: CustomInitAppRobot

    override fun setUp() {
        super.setUp()
        appRobot = CustomInitAppRobot(composeRule, initRepo)
    }

}