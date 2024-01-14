package com.masterplus.trdictionary.core.app

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.masterplus.trdictionary.MainActivity

typealias AppComponentRule = AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>