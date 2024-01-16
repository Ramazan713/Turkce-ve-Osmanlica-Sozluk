package com.masterplus.trdictionary.shared_test.rules

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.masterplus.trdictionary.MainActivity

typealias AppComponentRule = AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>