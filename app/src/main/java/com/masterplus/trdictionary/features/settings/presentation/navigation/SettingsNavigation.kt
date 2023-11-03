package com.masterplus.trdictionary.features.settings.presentation.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.composable
import com.masterplus.trdictionary.core.presentation.features.premium.PremiumViewModel
import com.masterplus.trdictionary.features.settings.presentation.SettingViewModel
import com.masterplus.trdictionary.features.settings.presentation.SettingsPage

private const val routeName = "settings"

fun NavController.navigateToSettings(){
    this.navigate(routeName)
}

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
fun NavGraphBuilder.settingsPage(
    onNavigateBack: ()->Unit,
    premiumViewModel: PremiumViewModel,
    windowWidthSizeClass: WindowWidthSizeClass,
){
    composable(
        routeName
    ){
        val settingViewModel: SettingViewModel = hiltViewModel()

        val premiumProduct by premiumViewModel.firstPremiumProduct.collectAsStateWithLifecycle()

        SettingsPage(
            onNavigateBack = onNavigateBack,
            state = settingViewModel.state,
            onEvent = settingViewModel::onEvent,
            premiumState = premiumViewModel.state,
            onPremiumEvent = premiumViewModel::onEvent,
            premiumProduct = premiumProduct,
            windowWidthSizeClass = windowWidthSizeClass
        )
    }
}