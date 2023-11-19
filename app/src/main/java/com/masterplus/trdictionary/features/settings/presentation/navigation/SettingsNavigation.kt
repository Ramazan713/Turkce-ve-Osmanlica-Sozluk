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
import com.masterplus.trdictionary.core.shared_features.auth_and_backup.presentation.auth.AuthViewModel
import com.masterplus.trdictionary.core.shared_features.premium.PremiumViewModel
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
        val authViewModel = hiltViewModel<AuthViewModel>()

        val premiumProduct by premiumViewModel.firstPremiumProduct.collectAsStateWithLifecycle()

        val authState by authViewModel.state.collectAsStateWithLifecycle()
        val state by settingViewModel.state.collectAsStateWithLifecycle()
        val premiumState by premiumViewModel.state.collectAsStateWithLifecycle()


        SettingsPage(
            onNavigateBack = onNavigateBack,
            state = state,
            onEvent = settingViewModel::onEvent,
            premiumState = premiumState,
            onPremiumEvent = premiumViewModel::onEvent,
            premiumProduct = premiumProduct,
            windowWidthSizeClass = windowWidthSizeClass,
            authState = authState,
            onAuthEvent = authViewModel::onEvent
        )
    }
}