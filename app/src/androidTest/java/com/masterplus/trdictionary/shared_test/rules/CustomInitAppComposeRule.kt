package com.masterplus.trdictionary.shared_test.rules

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.masterplus.trdictionary.custom_init.CustomInitTestActivity

typealias CustomInitAppComposeRule = AndroidComposeTestRule<ActivityScenarioRule<CustomInitTestActivity>, CustomInitTestActivity>